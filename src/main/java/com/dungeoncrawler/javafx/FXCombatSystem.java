package com.dungeoncrawler.javafx;

import java.util.List;
import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;

public class FXCombatSystem {
    private final FXPlayer player;
    private final PlayerStats stats;
    private final Inventory inventory;

    public FXCombatSystem(FXPlayer player, PlayerStats stats, Inventory inventory) {
        this.player = player;
        this.stats = stats;
        this.inventory = inventory;
    }

    public void executePlayerAttack(FXDungeonLevel level) {
        if (!player.triggerAttack()) {
            return;
        }

        Point3D pPos = player.getPosition();
        double yawRad = Math.toRadians(player.getYaw());
        // Forward vector based on yaw: dx = sin(yaw), dz = cos(yaw)
        double fwdX = Math.sin(yawRad);
        double fwdZ = Math.cos(yawRad);

        Item weapon = inventory.getEquippedWeapon();
        float weaponAtk = (weapon != null) ? weapon.getAttackPower() : 8f;
        float baseDmg = stats.getBaseAttack() + weaponAtk;

        List<FXEnemy> enemies = level.getEnemies();
        for (FXEnemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            Point3D ePos = enemy.getPosition();
            double dx = ePos.getX() - pPos.getX();
            double dz = ePos.getZ() - pPos.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            if (dist <= 3.0) {
                // Check dot product with forward facing vector
                double dot = (dx / dist) * fwdX + (dz / dist) * fwdZ;
                if (dot > 0.5) { // ~60 degree forward attack cone
                    boolean isCrit = Math.random() < 0.2;
                    float dmg = baseDmg * (isCrit ? 1.8f : (0.9f + (float) Math.random() * 0.2f));

                    enemy.takeDamage(dmg, isCrit, stats, inventory, player);
                    AudioManager.getInstance().playSFX(SoundType.SWORD_HIT);
                    player.addCameraShake(isCrit ? 0.45 : 0.25);
                }
            }
        }
    }
}
