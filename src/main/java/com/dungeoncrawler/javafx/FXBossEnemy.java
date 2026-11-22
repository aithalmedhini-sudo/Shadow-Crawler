package com.dungeoncrawler.javafx;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.entity.EnemyType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;

public class FXBossEnemy extends FXEnemy {
    private boolean enraged = false;
    private double slamCooldown = 6.0;

    public FXBossEnemy(Group spatial, Point3D spawnPos) {
        super(EnemyType.DARK_KING_BOSS, spatial, spawnPos, new Point3D(spawnPos.getX() + 4, 0, spawnPos.getZ() + 4));
        this.detectionRange = 25.0;
        this.attackRange = 3.2;
    }

    @Override
    public void update(double dt, Point3D playerPos, PlayerStats playerStats, Inventory inventory, FXPlayer player) {
        super.update(dt, playerPos, playerStats, inventory, player);
        if (currentState == State.DEAD) return;

        if (slamCooldown > 0) slamCooldown -= dt;

        if (!enraged && health <= maxHealth * 0.5f) {
            enraged = true;
            this.moveSpeed *= 1.35;
            this.attackPower *= 1.25;
            AudioManager.getInstance().playSFX(SoundType.BOSS_ROAR);
            player.addCameraShake(0.8);
            playerStats.setNotification("THE DARK KING ENRAGES! 'You shall not leave this crypt alive!'");
        }
    }

    @Override
    protected void die(PlayerStats stats, Inventory inv) {
        super.die(stats, inv);
        AudioManager.getInstance().playSFX(SoundType.BGM_VICTORY);
        stats.setNotification("THE DARK KING HAS FALLEN! The Throne Gate is unlocked!");
    }
}
