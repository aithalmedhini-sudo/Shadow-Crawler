package com.dungeoncrawler.save;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for serializing game state to disk.
 */
public class SaveData {
    public int currentLevelIndex = 1;
    public int playerLevel = 1;
    public int currentXP = 0;
    public float currentHP = 100f;
    public float maxHP = 100f;
    public float currentStamina = 100f;
    public float maxStamina = 100f;
    public int gold = 0;
    public List<String> inventoryItemIds = new ArrayList<>();
    public String equippedWeaponId = "rusty_sword";
    public String equippedShieldId = "wood_buckler";
    public List<String> keys = new ArrayList<>();
    public int enemiesDefeated = 0;
    public int chestsOpened = 0;
    public long timestamp = System.currentTimeMillis();

    public SaveData() {
    }
}
