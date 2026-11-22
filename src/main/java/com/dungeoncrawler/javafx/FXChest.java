package com.dungeoncrawler.javafx;

import java.util.ArrayList;
import java.util.List;
import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

public class FXChest {
    private final Group spatial;
    private final Group lidHinge;
    private final Point3D position;
    private final int gold;
    private final List<Item> items = new ArrayList<>();

    private boolean isOpened = false;
    private double currentAngle = 0;
    private double targetAngle = 0;
    private final Rotate xRotate = new Rotate(0, Rotate.X_AXIS);

    public FXChest(Group spatial, Point3D pos, int gold, Item... initialItems) {
        this.spatial = spatial;
        this.position = pos;
        this.gold = gold;

        Group foundLid = null;
        for (javafx.scene.Node n : spatial.getChildren()) {
            if ("LidHinge".equals(n.getId())) {
                foundLid = (Group) n;
                break;
            }
        }
        this.lidHinge = (foundLid != null) ? foundLid : spatial;
        this.lidHinge.getTransforms().add(xRotate);

        if (initialItems != null) {
            for (Item it : initialItems) {
                if (it != null) this.items.add(it);
            }
        }
    }

    public boolean interact(Inventory inv, PlayerStats stats) {
        if (isOpened) {
            stats.setNotification("Chest is already empty.");
            return false;
        }

        isOpened = true;
        targetAngle = -75.0;
        AudioManager.getInstance().playSFX(SoundType.CHEST_OPEN);
        stats.recordChestOpened();

        StringBuilder sb = new StringBuilder("Found: ");
        if (gold > 0) {
            stats.addGold(gold);
            sb.append(gold).append(" Gold! ");
        }
        for (Item it : items) {
            inv.addItem(it);
            sb.append("[").append(it.getName()).append("] ");
        }

        stats.setNotification(sb.toString());
        return true;
    }

    public void update(double dt) {
        if (Math.abs(currentAngle - targetAngle) > 0.5) {
            currentAngle += (targetAngle - currentAngle) * dt * 6.0;
            xRotate.setAngle(currentAngle);
        }
    }

    public boolean isOpened() { return isOpened; }
    public Point3D getPosition() { return position; }
    public Group getSpatial() { return spatial; }
}
