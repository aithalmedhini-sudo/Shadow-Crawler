package com.dungeoncrawler.javafx;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;

public class FXTrap {
    private final Group spatial;
    private final Group spikesAssembly;
    private final Point3D position;
    private final double triggerRadius = 1.3;
    private final float damage = 22f;

    private boolean active = false;
    private double stateTimer = 0;
    private double cooldown = 0;

    public FXTrap(Group spatial, Point3D pos) {
        this.spatial = spatial;
        this.position = pos;

        Group foundSpikes = null;
        for (javafx.scene.Node n : spatial.getChildren()) {
            if ("Spikes".equals(n.getId()) || "SpikesAssembly".equals(n.getId())) {
                foundSpikes = (Group) n;
                break;
            }
        }
        this.spikesAssembly = foundSpikes;
    }

    public void update(double dt, Point3D playerPos, PlayerStats playerStats) {
        if (cooldown > 0) {
            cooldown -= dt;
        }

        if (!active && cooldown <= 0) {
            double dx = playerPos.getX() - position.getX();
            double dz = playerPos.getZ() - position.getZ();
            if (dx * dx + dz * dz < triggerRadius * triggerRadius) {
                active = true;
                stateTimer = 0;
                AudioManager.getInstance().playSFX(SoundType.TRAP_TRIGGER);
                playerStats.takeDamage(damage, false, 0);
                playerStats.setNotification("Triggered Spike Trap! (-" + (int)damage + " HP)");
            }
        }

        if (active) {
            stateTimer += dt;
            if (spikesAssembly != null) {
                // In JavaFX 3D, floor is Y=0, -Y is UP
                if (stateTimer < 0.15) {
                    double progress = stateTimer / 0.15;
                    spikesAssembly.setTranslateY(0.55 - progress * 0.55); // pops up from below floor
                } else if (stateTimer > 1.2 && stateTimer < 1.6) {
                    double progress = (stateTimer - 1.2) / 0.4;
                    spikesAssembly.setTranslateY(progress * 0.55); // retracts below floor
                } else if (stateTimer >= 1.6) {
                    spikesAssembly.setTranslateY(0.55);
                    active = false;
                    cooldown = 2.0;
                }
            } else if (stateTimer >= 1.6) {
                active = false;
                cooldown = 2.0;
            }
        }
    }

    public boolean isActive() { return active; }
    public Point3D getPosition() { return position; }
    public Group getSpatial() { return spatial; }
}
