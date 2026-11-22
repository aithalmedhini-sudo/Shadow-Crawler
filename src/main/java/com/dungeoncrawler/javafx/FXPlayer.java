package com.dungeoncrawler.javafx;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class FXPlayer {
    private final PerspectiveCamera camera;
    private final Group playerGroup = new Group();
    private final Group viewmodel = new Group();
    private final PlayerStats stats;
    private final Inventory inventory;

    private double posX = 8.0;
    private double posY = -1.7; // In JavaFX 3D: Floor is Y=0, -Y is UP (eye height at -1.7)
    private double posZ = 8.0;

    private double yaw = 0.0;
    private double pitch = 0.0;
    private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    private final Translate positionTranslate = new Translate();

    private boolean isBlocking = false;
    private boolean isSprinting = false;
    private boolean isAttacking = false;
    private double swingTimer = 0.0;
    private double headBobTimer = 0.0;
    private double stepTimer = 0.0;
    private double cameraShake = 0.0;

    private Group weaponMesh;
    private Group shieldMesh;
    private final Rotate weaponSwingRotate = new Rotate(0, Rotate.X_AXIS);

    private Object targetedInteractable = null;
    private String interactionPrompt = "";

    public FXPlayer(PerspectiveCamera camera, PlayerStats stats, Inventory inventory) {
        this.camera = camera;
        this.stats = stats;
        this.inventory = inventory;

        camera.setNearClip(0.05);
        camera.setFarClip(250.0);
        camera.setFieldOfView(75.0);

        playerGroup.getTransforms().addAll(positionTranslate, yRotate, xRotate);
        playerGroup.getChildren().add(camera);

        initViewmodel();
        playerGroup.getChildren().add(viewmodel);
    }

    private void initViewmodel() {
        refreshEquippedMeshes();
    }

    public void refreshEquippedMeshes() {
        viewmodel.getChildren().clear();

        Item wep = inventory.getEquippedWeapon();
        if (wep != null && wep.getId().contains("axe")) {
            weaponMesh = FXMeshFactory.createAxeFP();
        } else {
            weaponMesh = FXMeshFactory.createSwordFP();
        }
        weaponMesh.setTranslateX(0.38);
        weaponMesh.setTranslateY(0.35); // in camera space, +Y is lower screen
        weaponMesh.setTranslateZ(0.65);
        weaponMesh.getTransforms().add(weaponSwingRotate);
        viewmodel.getChildren().add(weaponMesh);

        shieldMesh = FXMeshFactory.createShieldFP();
        shieldMesh.setTranslateX(-0.38);
        shieldMesh.setTranslateY(0.36);
        shieldMesh.setTranslateZ(0.62);
        viewmodel.getChildren().add(shieldMesh);
    }

    public void setPosition(Point3D p) {
        this.posX = p.getX();
        this.posY = -1.7; // Floor is at Y=0, player eye is at -1.7
        this.posZ = p.getZ();
        updateTransform();
    }

    public void addLook(double deltaYaw, double deltaPitch) {
        this.yaw += deltaYaw;
        this.pitch = Math.max(-85.0, Math.min(85.0, this.pitch + deltaPitch));
        yRotate.setAngle(yaw);
        xRotate.setAngle(pitch);
    }

    public void update(double dt, boolean fwd, boolean back, boolean left, boolean right, FXDungeonLevel level) {
        stats.update((float) dt, isSprinting, isBlocking);

        double speed = (isSprinting && stats.getCurrentStamina() > 5) ? 9.5 : (isBlocking ? 3.5 : 6.0);
        double moveX = 0;
        double moveZ = 0;

        if (fwd) moveZ += 1;
        if (back) moveZ -= 1;
        if (left) moveX -= 1;
        if (right) moveX += 1;

        if (moveX != 0 || moveZ != 0) {
            double len = Math.sqrt(moveX * moveX + moveZ * moveZ);
            moveX /= len;
            moveZ /= len;

            double rad = Math.toRadians(yaw);
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);

            double dx = (moveX * cos + moveZ * sin) * speed * dt;
            double dz = (-moveX * sin + moveZ * cos) * speed * dt;

            attemptMove(dx, dz, level);

            headBobTimer += dt * (isSprinting ? 14 : 9);
            stepTimer += dt * (isSprinting ? 2.5 : 1.7);
            if (stepTimer >= 1.0) {
                stepTimer = 0;
                AudioManager.getInstance().playSFX(SoundType.PLAYER_STEP);
            }
        }

        // Camera Shake
        double shakeX = 0, shakeY = 0;
        if (cameraShake > 0) {
            cameraShake = Math.max(0, cameraShake - dt * 2.5);
            shakeX = (Math.random() - 0.5) * cameraShake * 0.15;
            shakeY = (Math.random() - 0.5) * cameraShake * 0.15;
        }

        double bobOffset = Math.sin(headBobTimer) * (isSprinting ? 0.08 : 0.045);
        positionTranslate.setX(posX + shakeX);
        positionTranslate.setY(posY + bobOffset + shakeY);
        positionTranslate.setZ(posZ);

        updateViewmodelAnimation(dt);
        checkInteractionRaycast(level);
    }

    private void attemptMove(double dx, double dz, FXDungeonLevel level) {
        double radius = 0.45;
        if (!level.isColliding(posX + dx, posZ, radius)) {
            posX += dx;
        }
        if (!level.isColliding(posX, posZ + dz, radius)) {
            posZ += dz;
        }
    }

    private void updateViewmodelAnimation(double dt) {
        if (isAttacking) {
            swingTimer += dt * 5.0;
            if (swingTimer >= Math.PI) {
                isAttacking = false;
                swingTimer = 0;
                weaponSwingRotate.setAngle(0);
                weaponMesh.setTranslateX(0.38);
                weaponMesh.setTranslateY(0.35);
            } else {
                double swing = Math.sin(swingTimer);
                weaponSwingRotate.setAngle(swing * 55.0);
                weaponMesh.setTranslateX(0.38 - swing * 0.15);
                weaponMesh.setTranslateY(0.35 + swing * 0.15);
            }
        }

        if (shieldMesh != null) {
            if (isBlocking) {
                shieldMesh.setTranslateX(-0.15);
                shieldMesh.setTranslateY(0.12);
                shieldMesh.setTranslateZ(0.45);
            } else {
                shieldMesh.setTranslateX(-0.38);
                shieldMesh.setTranslateY(0.36);
                shieldMesh.setTranslateZ(0.62);
            }
        }
    }

    private void checkInteractionRaycast(FXDungeonLevel level) {
        targetedInteractable = null;
        interactionPrompt = "";

        // Check Doors
        for (FXDoor door : level.getDoors()) {
            double dx = door.getPosition().getX() - posX;
            double dz = door.getPosition().getZ() - posZ;
            if (dx * dx + dz * dz < 3.0 * 3.0) {
                targetedInteractable = door;
                if (!door.isOpen()) {
                    interactionPrompt = door.isLocked() ? "[E] Unlock " + door.getName() : "[E] Open " + door.getName();
                }
                return;
            }
        }

        // Check Chests
        for (FXChest chest : level.getChests()) {
            double dx = chest.getPosition().getX() - posX;
            double dz = chest.getPosition().getZ() - posZ;
            if (dx * dx + dz * dz < 2.5 * 2.5) {
                targetedInteractable = chest;
                if (!chest.isOpened()) {
                    interactionPrompt = "[E] Open Treasure Chest";
                }
                return;
            }
        }

        // Check Exit Portal
        if (level.getExitPosition() != null) {
            double dx = level.getExitPosition().getX() - posX;
            double dz = level.getExitPosition().getZ() - posZ;
            if (dx * dx + dz * dz < 2.8 * 2.8) {
                targetedInteractable = "PORTAL";
                interactionPrompt = "[E] Descend to Next Stage";
            }
        }
    }

    public void interact() {
        if (targetedInteractable instanceof FXDoor) {
            ((FXDoor) targetedInteractable).interact(inventory, stats);
        } else if (targetedInteractable instanceof FXChest) {
            boolean opened = ((FXChest) targetedInteractable).interact(inventory, stats);
            if (opened) {
                // Auto-equip superior weapons/shields if found
                Item currentWep = inventory.getEquippedWeapon();
                for (Item it : inventory.getItems()) {
                    if (it != null && it.getType() == Item.ItemType.WEAPON) {
                        if (currentWep == null || it.getAttackPower() > currentWep.getAttackPower()) {
                            inventory.equipWeapon(it);
                            currentWep = it;
                            stats.setNotification("Equipped superior weapon: [" + it.getName() + "]!");
                            refreshEquippedMeshes();
                        }
                    } else if (it != null && it.getType() == Item.ItemType.SHIELD) {
                        Item currentSh = inventory.getEquippedShield();
                        if (currentSh == null || it.getDefensePower() > currentSh.getDefensePower()) {
                            inventory.equipShield(it);
                            stats.setNotification("Equipped superior shield: [" + it.getName() + "]!");
                            refreshEquippedMeshes();
                        }
                    }
                }
            }
        }
    }

    public boolean triggerAttack() {
        if (!isAttacking && !isBlocking && stats.getCurrentStamina() >= 12) {
            isAttacking = true;
            swingTimer = 0;
            stats.consumeStamina(12);
            AudioManager.getInstance().playSFX(SoundType.SWORD_SWING);
            return true;
        }
        return false;
    }

    public int getEquippedShieldDef() {
        Item sh = inventory.getEquippedShield();
        return (sh != null) ? sh.getDefensePower() : 0;
    }

    public void addCameraShake(double amount) {
        triggerCameraShake(amount);
    }

    public void triggerCameraShake(double amount) {
        this.cameraShake = Math.max(this.cameraShake, amount);
    }

    public void setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }

    public void setSprinting(boolean sprinting) {
        this.isSprinting = sprinting;
    }

    private void updateTransform() {
        positionTranslate.setX(posX);
        positionTranslate.setY(posY);
        positionTranslate.setZ(posZ);
        yRotate.setAngle(yaw);
        xRotate.setAngle(pitch);
    }

    public Point3D getPosition() { return new Point3D(posX, posY, posZ); }
    public double getYaw() { return yaw; }
    public double getPitch() { return pitch; }
    public boolean isBlocking() { return isBlocking; }
    public boolean isAttacking() { return isAttacking; }
    public Group getPlayerGroup() { return playerGroup; }
    public String getInteractionPrompt() { return interactionPrompt; }
}
