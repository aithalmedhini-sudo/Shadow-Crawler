package com.dungeoncrawler.javafx;

import com.dungeoncrawler.GameState;
import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.PlayerStats;
import com.dungeoncrawler.save.SaveData;
import com.dungeoncrawler.save.SaveManager;
import javafx.geometry.Point3D;
import javafx.scene.Group;

public class FXLevelManager {
    public static final int TOTAL_STAGES = 5;

    private final Group root3D;
    private final FXPlayer player;
    private final PlayerStats stats;
    private final Inventory inventory;

    private int currentLevelIndex = 1;
    private FXDungeonLevel currentLevel = null;

    // Baseline stats for stage clear summary
    private int stageStartKills = 0;
    private int stageStartChests = 0;
    private int stageStartGold = 0;

    public FXLevelManager(Group root3D, FXPlayer player, PlayerStats stats, Inventory inventory) {
        this.root3D = root3D;
        this.player = player;
        this.stats = stats;
        this.inventory = inventory;
    }

    public void loadLevel(int levelIndex) {
        if (currentLevel != null) {
            root3D.getChildren().remove(currentLevel.getLevelGroup());
            currentLevel.cleanup();
        }

        this.currentLevelIndex = levelIndex;
        this.currentLevel = FXDungeonGenerator.buildLevel(levelIndex);
        root3D.getChildren().add(currentLevel.getLevelGroup());

        player.setPosition(currentLevel.getPlayerSpawn());

        // Snapshot baseline stats for stage summary
        stageStartKills = stats.getEnemiesDefeated();
        stageStartChests = stats.getChestsOpened();
        stageStartGold = stats.getGold();

        if (levelIndex == 1) {
            AudioManager.getInstance().playMusic(SoundType.BGM_CRYPT);
        } else if (levelIndex == 2) {
            AudioManager.getInstance().playMusic(SoundType.BGM_CAVERNS);
        } else if (levelIndex == 3) {
            AudioManager.getInstance().playMusic(SoundType.BGM_CRYPT);
        } else if (levelIndex == 4) {
            AudioManager.getInstance().playMusic(SoundType.BGM_CAVERNS);
        } else if (levelIndex == 5) {
            AudioManager.getInstance().playMusic(SoundType.BGM_BOSS);
        }

        stats.setNotification("Entered: " + currentLevel.getName());
        saveProgression();
    }

    public boolean checkLevelExit() {
        if (currentLevel == null || currentLevel.getExitPortalPos() == null) return false;
        Point3D p = player.getPosition();
        Point3D x = currentLevel.getExitPortalPos();
        double dx = p.getX() - x.getX();
        double dz = p.getZ() - x.getZ();
        return (dx * dx + dz * dz < 2.8 * 2.8);
    }

    public GameState proceedToNextLevel() {
        if (currentLevelIndex < TOTAL_STAGES) {
            loadLevel(currentLevelIndex + 1);
            // Reward player with health and stamina replenishment upon clearing a stage!
            stats.heal(stats.getMaxHP());
            stats.setCurrentStamina(stats.getMaxStamina());
            AudioManager.getInstance().playSFX(SoundType.LEVEL_UP);
            return GameState.PLAYING;
        } else {
            AudioManager.getInstance().playMusic(SoundType.BGM_VICTORY);
            return GameState.VICTORY;
        }
    }

    public void restartCurrentLevel() {
        stats.setCurrentHP(stats.getMaxHP());
        stats.setCurrentStamina(stats.getMaxStamina());
        loadLevel(currentLevelIndex);
    }

    public int getStageKills() {
        return Math.max(0, stats.getEnemiesDefeated() - stageStartKills);
    }

    public int getStageChests() {
        return Math.max(0, stats.getChestsOpened() - stageStartChests);
    }

    public int getStageGold() {
        return Math.max(0, stats.getGold() - stageStartGold);
    }

    public void saveProgression() {
        SaveData data = new SaveData();
        data.currentLevelIndex = currentLevelIndex;
        data.playerLevel = stats.getLevel();
        data.currentXP = stats.getCurrentXP();
        data.currentHP = stats.getCurrentHP();
        data.maxHP = stats.getMaxHP();
        data.currentStamina = stats.getCurrentStamina();
        data.maxStamina = stats.getMaxStamina();
        data.gold = stats.getGold();
        data.equippedWeaponId = (inventory.getEquippedWeapon() != null) ? inventory.getEquippedWeapon().getId() : "rusty_sword";
        data.equippedShieldId = (inventory.getEquippedShield() != null) ? inventory.getEquippedShield().getId() : "wood_buckler";
        data.enemiesDefeated = stats.getEnemiesDefeated();
        data.chestsOpened = stats.getChestsOpened();

        SaveManager.saveGame(data);
    }

    public FXDungeonLevel getCurrentLevel() { return currentLevel; }
    public int getCurrentLevelIndex() { return currentLevelIndex; }
}
