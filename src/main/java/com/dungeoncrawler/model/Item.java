package com.dungeoncrawler.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a weapon, shield, consumable, key, or treasure item.
 */
public class Item {
    public enum ItemType {
        WEAPON,
        SHIELD,
        POTION,
        KEY,
        TREASURE
    }

    private final String id;
    private final String name;
    private final String description;
    private final ItemType type;
    private final int value; // Gold value
    private final int attackPower;
    private final int defensePower;
    private final float healAmount;
    private final float staminaAmount;
    private final float attackSpeed; // Swings per second (default 1.5)

    public Item(String id, String name, String description, ItemType type, int value,
                int attackPower, int defensePower, float healAmount, float staminaAmount, float attackSpeed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.value = value;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.healAmount = healAmount;
        this.staminaAmount = staminaAmount;
        this.attackSpeed = attackSpeed;
    }

    // Static registry of predefined items
    private static final Map<String, Item> REGISTRY = new HashMap<>();

    static {
        // Weapons
        register(new Item("rusty_sword", "Rusty Shortsword", "An old, chipped blade found in the crypt.",
                ItemType.WEAPON, 15, 14, 0, 0, 0, 1.8f));
        register(new Item("steel_longsword", "Steel Longsword", "A well-balanced blade forged for knights.",
                ItemType.WEAPON, 60, 26, 2, 0, 0, 1.6f));
        register(new Item("flaming_axe", "Battleaxe of Embers", "A brutal war axe glowing with trapped fire.",
                ItemType.WEAPON, 120, 42, 0, 0, 0, 1.2f));
        register(new Item("shadow_dagger", "Shadowfang Dagger", "A swift obsidian dagger with lethal precision.",
                ItemType.WEAPON, 85, 22, 0, 0, 0, 2.4f));
        register(new Item("dungeon_slayer", "Ancient Greatsword", "A legendary runic sword designed to slay dark lords.",
                ItemType.WEAPON, 250, 60, 5, 0, 0, 1.4f));

        // Shields
        register(new Item("wood_buckler", "Wooden Buckler", "A sturdy wooden shield reinforced with iron rim.",
                ItemType.SHIELD, 20, 0, 8, 0, 0, 1.0f));
        register(new Item("knight_shield", "Knight's Crest Shield", "A polished steel heater shield that deflects heavy blows.",
                ItemType.SHIELD, 80, 0, 18, 0, 0, 1.0f));
        register(new Item("dragon_shield", "Dragonplate Aegis", "A mythical shield forged in dragonfire.",
                ItemType.SHIELD, 200, 0, 30, 0, 0, 1.0f));

        // Potions
        register(new Item("minor_health_potion", "Minor Health Potion", "Restores 40 Health Points.",
                ItemType.POTION, 25, 0, 0, 40f, 0, 1.0f));
        register(new Item("greater_health_potion", "Greater Health Potion", "Restores 90 Health Points.",
                ItemType.POTION, 55, 0, 0, 90f, 0, 1.0f));
        register(new Item("stamina_potion", "Stamina Elixir", "Instantly restores 70 Stamina.",
                ItemType.POTION, 30, 0, 0, 0, 70f, 1.0f));

        // Keys
        register(new Item("crypt_key", "Crypt Gate Key", "An ornate brass key that opens the Crypt Vault.",
                ItemType.KEY, 0, 0, 0, 0, 0, 1.0f));
        register(new Item("cavern_key", "Shadow Cavern Key", "An obsidian key engraved with subterranean runes.",
                ItemType.KEY, 0, 0, 0, 0, 0, 1.0f));
        register(new Item("catacomb_key", "Catacomb Seal Key", "An ancient iron key found in the sunken tombs.",
                ItemType.KEY, 0, 0, 0, 0, 0, 1.0f));
        register(new Item("fortress_key", "Royal Fortress Key", "A gilded key bearing the crest of the fortress commander.",
                ItemType.KEY, 0, 0, 0, 0, 0, 1.0f));
        register(new Item("boss_key", "Dark King's Crest Key", "A menacing blood-iron key unlocking the throne room.",
                ItemType.KEY, 0, 0, 0, 0, 0, 1.0f));

        // Treasures
        register(new Item("ruby_gem", "Blood Ruby", "A sparkling crimson gemstone prized by collectors.",
                ItemType.TREASURE, 75, 0, 0, 0, 0, 1.0f));
        register(new Item("gold_chalice", "Royal Golden Chalice", "An ancient goblet inlaid with sapphires.",
                ItemType.TREASURE, 150, 0, 0, 0, 0, 1.0f));
        register(new Item("ancient_crown", "Crown of the Forgotten King", "A gilded relic of immense historical value.",
                ItemType.TREASURE, 300, 0, 0, 0, 0, 1.0f));
    }

    public static void register(Item item) {
        REGISTRY.put(item.getId(), item);
    }

    public static Item getById(String id) {
        return REGISTRY.get(id);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ItemType getType() { return type; }
    public int getValue() { return value; }
    public int getAttackPower() { return attackPower; }
    public int getDefensePower() { return defensePower; }
    public float getHealAmount() { return healAmount; }
    public float getStaminaAmount() { return staminaAmount; }
    public float getAttackSpeed() { return attackSpeed; }
}
