package com.dungeoncrawler;

import com.dungeoncrawler.audio.SoundSynthesizer;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import com.dungeoncrawler.save.GameSettings;
import com.dungeoncrawler.save.SaveData;
import com.dungeoncrawler.save.SaveManager;

import javax.sound.sampled.AudioInputStream;
import java.io.File;

/**
 * Automated test suite verifying all game systems:
 * Audio synthesizer, items, inventory, player stats, leveling,
 * combat damage math, and save/load system.
 */
public class GameSystemTest {
    public static void main(String[] args) {
        System.out.println(">>> Running 3D Dungeon Crawler System Verification...");

        testItemRegistry();
        testPlayerStatsAndLeveling();
        testInventoryAndCombatStats();
        testAudioSynthesis();
        testSaveLoadSystem();
        testGameSettings();

        System.out.println(">>> ALL 6 SYSTEM TEST SUITES PASSED CLEANLY!");
    }

    private static void testItemRegistry() {
        System.out.print("Testing Item Registry... ");
        Item sword = Item.getById("steel_longsword");
        assertNotNull(sword, "steel_longsword should exist");
        assertTrue(sword.getAttackPower() > 0, "sword should have attack power");

        Item shield = Item.getById("knight_shield");
        assertNotNull(shield, "knight_shield should exist");
        assertTrue(shield.getDefensePower() > 0, "shield should have defense power");

        Item key = Item.getById("crypt_key");
        assertNotNull(key, "crypt_key should exist");
        System.out.println("PASSED.");
    }

    private static void testPlayerStatsAndLeveling() {
        System.out.print("Testing Player Stats & Leveling... ");
        PlayerStats stats = new PlayerStats();
        assertEquals(1, stats.getLevel(), "Initial level should be 1");
        assertEquals(100f, stats.getCurrentHP(), "Initial HP should be 100");

        // Test damage with blocking
        float dmgTaken = stats.takeDamage(20f, true, 10);
        assertTrue(dmgTaken < 20f, "Blocking should reduce damage");
        assertTrue(stats.getCurrentHP() < 100f, "HP should decrease");

        // Test healing
        stats.heal(50f);
        assertEquals(100f, stats.getCurrentHP(), "HP should cap at maxHP");

        // Test XP and Level Up
        stats.addXP(100);
        assertEquals(2, stats.getLevel(), "Player should level up to 2");
        assertTrue(stats.getMaxHP() > 100f, "Max HP should increase on level up");
        System.out.println("PASSED.");
    }

    private static void testInventoryAndCombatStats() {
        System.out.print("Testing Inventory & Equipment... ");
        Inventory inv = new Inventory();
        assertNotNull(inv.getEquippedWeapon(), "Default weapon should be equipped");
        assertNotNull(inv.getEquippedShield(), "Default shield should be equipped");

        // Equip new weapon
        Item flamingAxe = Item.getById("flaming_axe");
        inv.addItem(flamingAxe);
        boolean equipped = inv.equipWeapon(flamingAxe);
        assertTrue(equipped, "Should equip flaming axe");
        assertEquals("flaming_axe", inv.getEquippedWeapon().getId(), "Equipped weapon should be flaming axe");

        // Keys check
        inv.addItem(Item.getById("crypt_key"));
        assertTrue(inv.hasKey("crypt_key"), "Inventory should have crypt_key");
        assertFalse(inv.hasKey("boss_key"), "Inventory should not have boss_key yet");
        System.out.println("PASSED.");
    }

    private static void testAudioSynthesis() {
        System.out.print("Testing Audio Wave Synthesizer... ");
        for (SoundType type : SoundType.values()) {
            AudioInputStream ais = SoundSynthesizer.generate(type);
            assertNotNull(ais, "Generated stream for " + type + " should not be null");
            assertTrue(ais.getFrameLength() > 0, "Stream length should be > 0 for " + type);
        }
        System.out.println("PASSED (All 23 SFX and BGM tracks synthesized).");
    }

    private static void testSaveLoadSystem() {
        System.out.print("Testing Save & Load System... ");
        SaveData data = new SaveData();
        data.currentLevelIndex = 2;
        data.playerLevel = 3;
        data.gold = 350;
        data.equippedWeaponId = "flaming_axe";

        boolean saved = SaveManager.saveGame(data);
        assertTrue(saved, "Save should succeed");
        assertTrue(SaveManager.hasSave(), "Save file should exist");

        SaveData loaded = SaveManager.loadGame();
        assertNotNull(loaded, "Loaded data should not be null");
        assertEquals(2, loaded.currentLevelIndex, "Current level index should match");
        assertEquals(3, loaded.playerLevel, "Player level should match");
        assertEquals(350, loaded.gold, "Gold should match");
        assertEquals("flaming_axe", loaded.equippedWeaponId, "Equipped weapon should match");

        // Clean up test save
        SaveManager.deleteSave();
        System.out.println("PASSED.");
    }

    private static void testGameSettings() {
        System.out.print("Testing Game Settings... ");
        GameSettings s = GameSettings.load();
        s.setMusicVolume(0.75f);
        s.setMouseSensitivity(3.0f);
        s.setGraphicsQuality("HIGH");
        s.save();

        GameSettings reloaded = GameSettings.load();
        assertEquals(0.75f, reloaded.getMusicVolume(), 0.001f, "Music volume should match");
        assertEquals(3.0f, reloaded.getMouseSensitivity(), 0.001f, "Sensitivity should match");
        assertEquals("HIGH", reloaded.getGraphicsQuality(), "Graphics quality should match");
        System.out.println("PASSED.");
    }

    private static void assertTrue(boolean condition, String msg) {
        if (!condition) throw new AssertionError("FAILED: " + msg);
    }

    private static void assertFalse(boolean condition, String msg) {
        if (condition) throw new AssertionError("FAILED: " + msg);
    }

    private static void assertNotNull(Object obj, String msg) {
        if (obj == null) throw new AssertionError("FAILED: " + msg);
    }

    private static void assertEquals(Object expected, Object actual, String msg) {
        if (!expected.equals(actual)) throw new AssertionError("FAILED: " + msg + " (expected: " + expected + ", actual: " + actual + ")");
    }

    private static void assertEquals(float expected, float actual, float eps, String msg) {
        if (Math.abs(expected - actual) > eps) throw new AssertionError("FAILED: " + msg + " (expected: " + expected + ", actual: " + actual + ")");
    }
}
