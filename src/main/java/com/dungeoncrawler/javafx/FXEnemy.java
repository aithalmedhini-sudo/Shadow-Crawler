package com.dungeoncrawler.javafx;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.entity.EnemyType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class FXEnemy {
    public enum State { PATROL, CHASE, ATTACK_WINDUP, ATTACKING, HURT, DEAD }

    protected final EnemyType type;
    protected final Group spatial;
    protected final Point3D spawnPos;
    protected final Point3D patrolPos;

    protected float health;
    protected float maxHealth;
    protected float attackPower;
    protected int defense;
    protected double moveSpeed;

    protected State currentState = State.PATROL;
    protected double stateTimer = 0;
    protected double attackCooldown = 0;
    protected double detectionRange = 12.0;
    protected double attackRange = 2.2;

    protected double posX;
    protected double posZ;
    protected double yaw = 0;
    protected final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

    // 3D Health Bar
    protected Group hpBarGroup;
    protected Box hpBarFill;
    protected Item dropItem = null;

    public FXEnemy(EnemyType type, Group spatial, Point3D spawnPos, Point3D patrolPos) {
        this.type = type;
        this.spatial = spatial;
        this.spawnPos = spawnPos;
        this.patrolPos = (patrolPos != null) ? patrolPos : new Point3D(spawnPos.getX() + 3, 0, spawnPos.getZ() + 3);

        this.maxHealth = type.getMaxHealth();
        this.health = maxHealth;
        this.attackPower = type.getAttackPower();
        this.defense = type.getDefense();
        this.moveSpeed = type.getMoveSpeed();

        this.posX = spawnPos.getX();
        this.posZ = spawnPos.getZ();

        spatial.setTranslateX(posX);
        spatial.setTranslateY(0);
        spatial.setTranslateZ(posZ);
        spatial.getTransforms().add(yRotate);

        initHealthBar();
    }

    private void initHealthBar() {
        hpBarGroup = new Group();
        double barY = (type == EnemyType.DARK_KING_BOSS) ? -3.8 : -2.2;
        hpBarGroup.setTranslateY(barY);

        Box bg = new Box(1.0, 0.1, 0.02);
        bg.setMaterial(new PhongMaterial(Color.rgb(20, 20, 20)));
        hpBarGroup.getChildren().add(bg);

        hpBarFill = new Box(0.96, 0.08, 0.03);
        hpBarFill.setMaterial(new PhongMaterial(Color.rgb(220, 30, 30)));
        hpBarGroup.getChildren().add(hpBarFill);

        spatial.getChildren().add(hpBarGroup);
    }

    public void setDropItem(Item it) {
        this.dropItem = it;
    }

    public void update(double dt, Point3D playerPos, PlayerStats playerStats, Inventory inventory, FXPlayer player) {
        if (currentState == State.DEAD) return;

        if (attackCooldown > 0) {
            attackCooldown -= dt;
        }

        double dx = playerPos.getX() - posX;
        double dz = playerPos.getZ() - posZ;
        double dist = Math.sqrt(dx * dx + dz * dz);

        switch (currentState) {
            case PATROL:
                if (dist <= detectionRange) {
                    currentState = State.CHASE;
                } else {
                    double targetX = (Math.sin(System.currentTimeMillis() * 0.0005) > 0) ? patrolPos.getX() : spawnPos.getX();
                    double targetZ = (Math.sin(System.currentTimeMillis() * 0.0005) > 0) ? patrolPos.getZ() : spawnPos.getZ();
                    double tdx = targetX - posX;
                    double tdz = targetZ - posZ;
                    double tdist = Math.sqrt(tdx * tdx + tdz * tdz);
                    if (tdist > 0.5) {
                        posX += (tdx / tdist) * moveSpeed * 0.45 * dt;
                        posZ += (tdz / tdist) * moveSpeed * 0.45 * dt;
                        lookAt(playerPos);
                    }
                }
                break;

            case CHASE:
                if (dist > detectionRange * 1.8) {
                    currentState = State.PATROL;
                } else if (dist <= attackRange) {
                    if (attackCooldown <= 0) {
                        currentState = State.ATTACK_WINDUP;
                        stateTimer = 0;
                    }
                } else {
                    posX += (dx / dist) * moveSpeed * dt;
                    posZ += (dz / dist) * moveSpeed * dt;
                    lookAt(playerPos);
                }
                break;

            case ATTACK_WINDUP:
                stateTimer += dt;
                lookAt(playerPos);
                if (stateTimer >= 0.45) {
                    currentState = State.ATTACKING;
                    stateTimer = 0;
                    AudioManager.getInstance().playSFX(SoundType.ENEMY_ATTACK);
                }
                break;

            case ATTACKING:
                stateTimer += dt;
                if (stateTimer >= 0.15) {
                    if (dist <= attackRange * 1.3) {
                        boolean isBlocking = player.isBlocking();
                        playerStats.takeDamage(attackPower, isBlocking, player.getEquippedShieldDef());
                        player.addCameraShake(0.35);
                    }
                    attackCooldown = 1.4;
                    currentState = State.CHASE;
                    stateTimer = 0;
                }
                break;

            case HURT:
                stateTimer += dt;
                if (stateTimer >= 0.3) {
                    currentState = State.CHASE;
                    stateTimer = 0;
                }
                break;
        }

        spatial.setTranslateX(posX);
        spatial.setTranslateZ(posZ);
        yRotate.setAngle(yaw);
    }

    public void takeDamage(float damage, boolean isCrit, PlayerStats playerStats, Inventory inventory, FXPlayer player) {
        if (currentState == State.DEAD) return;

        float effective = Math.max(1f, damage - (defense * 0.5f));
        health -= effective;
        AudioManager.getInstance().playSFX(SoundType.ENEMY_HURT);

        double ratio = Math.max(0.01, health / maxHealth);
        hpBarFill.setScaleX(ratio);

        if (health <= 0) {
            die(playerStats, inventory);
        } else {
            currentState = State.HURT;
            stateTimer = 0;
        }
    }

    protected void die(PlayerStats stats, Inventory inv) {
        currentState = State.DEAD;
        AudioManager.getInstance().playSFX(SoundType.ENEMY_DEATH);
        spatial.getChildren().remove(hpBarGroup);

        spatial.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        spatial.setTranslateY(0.2);

        stats.addXP(type.getXpReward());
        stats.addGold(type.getGoldReward());
        stats.recordEnemyDefeated();

        if (dropItem != null) {
            inv.addItem(dropItem);
            stats.setNotification("Enemy dropped: " + dropItem.getName() + "!");
        }
    }

    protected void lookAt(Point3D target) {
        double dx = target.getX() - posX;
        double dz = target.getZ() - posZ;
        this.yaw = Math.toDegrees(Math.atan2(dx, dz));
    }

    public boolean isAlive() { return currentState != State.DEAD; }
    public Point3D getPosition() { return new Point3D(posX, 0, posZ); }
    public EnemyType getType() { return type; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public Group getSpatial() { return spatial; }
}
