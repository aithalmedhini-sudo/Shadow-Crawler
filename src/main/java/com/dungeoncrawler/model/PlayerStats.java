package com.dungeoncrawler.model;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;

/**
 * Tracks player attributes, health, stamina, leveling, and combat stats.
 */
public class PlayerStats {
    private int level = 1;
    private int currentXP = 0;
    private int xpToNextLevel = 100;

    private float currentHP = 100f;
    private float maxHP = 100f;

    private float currentStamina = 100f;
    private float maxStamina = 100f;
    private float staminaRegenRate = 22f; // Per second

    private int baseAttack = 10;
    private int baseDefense = 2;
    private int gold = 0;

    private int enemiesDefeated = 0;
    private int chestsOpened = 0;

    // Callbacks or event flag for UI notification
    private String lastNotification = null;

    public PlayerStats() {
    }

    public void update(float tpf, boolean isSprinting, boolean isBlocking) {
        // Stamina logic
        if (isSprinting) {
            consumeStamina(20f * tpf);
        } else if (isBlocking) {
            // Stamina regenerates at half speed while holding block
            if (currentStamina < maxStamina) {
                currentStamina = Math.min(maxStamina, currentStamina + (staminaRegenRate * 0.4f) * tpf);
            }
        } else {
            // Normal stamina regeneration
            if (currentStamina < maxStamina) {
                currentStamina = Math.min(maxStamina, currentStamina + staminaRegenRate * tpf);
            }
        }
    }

    public boolean consumeStamina(float amount) {
        if (currentStamina >= amount) {
            currentStamina -= amount;
            return true;
        }
        return false;
    }

    public void restoreStamina(float amount) {
        currentStamina = Math.min(maxStamina, currentStamina + amount);
    }

    public void heal(float amount) {
        currentHP = Math.min(maxHP, currentHP + amount);
    }

    /**
     * Applies incoming damage to the player.
     * @param rawDamage The raw attack damage
     * @param isBlocking Whether player is holding active shield block
     * @param shieldDef Defense bonus of equipped shield
     * @return Actual damage taken
     */
    public float takeDamage(float rawDamage, boolean isBlocking, int shieldDef) {
        float effectiveDamage;
        if (isBlocking && currentStamina > 10f) {
            // Block absorbs 75% plus shield defense, costs stamina
            consumeStamina(15f + rawDamage * 0.3f);
            float blockedRatio = 0.75f + (shieldDef * 0.005f);
            blockedRatio = Math.min(0.95f, blockedRatio);
            effectiveDamage = (rawDamage * (1f - blockedRatio)) - (baseDefense * 0.5f);
            AudioManager.getInstance().playSFX(SoundType.SHIELD_BLOCK);
        } else {
            effectiveDamage = rawDamage - (baseDefense + shieldDef * 0.2f);
            AudioManager.getInstance().playSFX(SoundType.PLAYER_HURT);
        }
        effectiveDamage = Math.max(2f, effectiveDamage);
        currentHP = Math.max(0f, currentHP - effectiveDamage);
        return effectiveDamage;
    }

    public void addXP(int amount) {
        currentXP += amount;
        setNotification("+" + amount + " XP");
        while (currentXP >= xpToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        currentXP -= xpToNextLevel;
        level++;
        xpToNextLevel = (int) (xpToNextLevel * 1.5f);
        maxHP += 18f;
        currentHP = maxHP; // Full heal on level up
        maxStamina += 12f;
        currentStamina = maxStamina;
        baseAttack += 4;
        baseDefense += 2;
        AudioManager.getInstance().playSFX(SoundType.LEVEL_UP);
        setNotification("LEVEL UP! Reached Level " + level + "!");
    }

    public void addGold(int amount) {
        gold += amount;
        setNotification("+" + amount + " Gold");
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void recordEnemyDefeated() {
        enemiesDefeated++;
    }

    public void recordChestOpened() {
        chestsOpened++;
    }

    public boolean isAlive() {
        return currentHP > 0.001f;
    }

    // Getters and Setters
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getCurrentXP() { return currentXP; }
    public void setCurrentXP(int currentXP) { this.currentXP = currentXP; }

    public int getXpToNextLevel() { return xpToNextLevel; }
    public void setXpToNextLevel(int xpToNextLevel) { this.xpToNextLevel = xpToNextLevel; }

    public float getCurrentHP() { return currentHP; }
    public void setCurrentHP(float currentHP) { this.currentHP = currentHP; }

    public float getMaxHP() { return maxHP; }
    public void setMaxHP(float maxHP) { this.maxHP = maxHP; }

    public float getCurrentStamina() { return currentStamina; }
    public void setCurrentStamina(float currentStamina) { this.currentStamina = currentStamina; }

    public float getMaxStamina() { return maxStamina; }
    public void setMaxStamina(float maxStamina) { this.maxStamina = maxStamina; }

    public int getBaseAttack() { return baseAttack; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }

    public int getBaseDefense() { return baseDefense; }
    public void setBaseDefense(int baseDefense) { this.baseDefense = baseDefense; }

    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }

    public int getEnemiesDefeated() { return enemiesDefeated; }
    public void setEnemiesDefeated(int enemiesDefeated) { this.enemiesDefeated = enemiesDefeated; }

    public int getChestsOpened() { return chestsOpened; }
    public void setChestsOpened(int chestsOpened) { this.chestsOpened = chestsOpened; }

    public String getLastNotification() { return lastNotification; }
    public String getNotification() { return lastNotification; }
    public void setNotification(String notification) { this.lastNotification = notification; }
    public void clearNotification() { this.lastNotification = null; }

    public void reset() {
        level = 1;
        currentXP = 0;
        xpToNextLevel = 100;
        maxHP = 100f;
        currentHP = 100f;
        maxStamina = 100f;
        currentStamina = 100f;
        baseAttack = 10;
        baseDefense = 2;
        gold = 0;
        enemiesDefeated = 0;
        chestsOpened = 0;
        lastNotification = null;
    }
}
