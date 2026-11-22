package com.dungeoncrawler.javafx;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

public class FXDoor {
    private final Group spatial;
    private final Group hinge;
    private final Point3D position;
    private final boolean isLocked;
    private final String requiredKey;
    private final String name;

    private boolean isOpen = false;
    private double currentAngle = 0;
    private double targetAngle = 0;
    private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

    public FXDoor(Group spatial, Point3D pos, boolean isLocked, String requiredKey, String name) {
        this.spatial = spatial;
        this.position = pos;
        this.isLocked = isLocked;
        this.requiredKey = requiredKey;
        this.name = name;

        Group foundHinge = null;
        for (javafx.scene.Node n : spatial.getChildren()) {
            if ("LeafHinge".equals(n.getId()) || "HingeGroup".equals(n.getId())) {
                foundHinge = (Group) n;
                break;
            }
        }
        this.hinge = (foundHinge != null) ? foundHinge : spatial;
        this.hinge.getTransforms().add(yRotate);
    }

    public boolean interact(Inventory inv, PlayerStats stats) {
        if (isOpen) return false;

        if (isLocked) {
            if (requiredKey != null && !inv.hasKey(requiredKey)) {
                AudioManager.getInstance().playSFX(SoundType.DOOR_LOCKED);
                stats.setNotification("Locked! Requires " + name + " Key.");
                return false;
            }
        }

        isOpen = true;
        targetAngle = 90.0;
        AudioManager.getInstance().playSFX(SoundType.DOOR_OPEN);
        stats.setNotification("Opened " + name + ".");
        return true;
    }

    public void update(double dt) {
        if (Math.abs(currentAngle - targetAngle) > 0.5) {
            currentAngle += (targetAngle - currentAngle) * dt * 5.0;
            yRotate.setAngle(currentAngle);
        }
    }

    public boolean isOpen() { return isOpen; }
    public boolean isLocked() { return isLocked; }
    public String getRequiredKey() { return requiredKey; }
    public String getName() { return name; }
    public Point3D getPosition() { return position; }
    public Group getSpatial() { return spatial; }
}
