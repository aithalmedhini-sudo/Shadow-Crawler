package com.dungeoncrawler.entity;

public enum EnemyType {
    SKELETON_WARRIOR("Skeleton Warrior", 50f, 15f, 2, 3.2f, 35, 25),
    GOBLIN_SKULKER("Goblin Skulker", 38f, 12f, 1, 4.3f, 30, 20),
    GIANT_SPIDER("Giant Spider", 48f, 16f, 2, 4.6f, 45, 30),
    DUNGEON_GUARD("Dungeon Guard", 85f, 22f, 6, 2.8f, 70, 50),
    ARMORED_KNIGHT("Armored Knight", 130f, 28f, 10, 2.5f, 110, 85),
    DARK_KING_BOSS("The Dark King", 450f, 40f, 14, 3.0f, 750, 400);

    private final String displayName;
    private final float maxHealth;
    private final float attackPower;
    private final int defense;
    private final float moveSpeed;
    private final int xpReward;
    private final int goldReward;

    EnemyType(String displayName, float maxHealth, float attackPower, int defense,
              float moveSpeed, int xpReward, int goldReward) {
        this.displayName = displayName;
        this.maxHealth = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
        this.moveSpeed = moveSpeed;
        this.xpReward = xpReward;
        this.goldReward = goldReward;
    }

    public String getDisplayName() { return displayName; }
    public float getMaxHealth() { return maxHealth; }
    public float getAttackPower() { return attackPower; }
    public int getDefense() { return defense; }
    public float getMoveSpeed() { return moveSpeed; }
    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }
}
