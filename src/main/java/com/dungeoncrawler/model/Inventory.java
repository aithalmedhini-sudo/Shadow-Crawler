package com.dungeoncrawler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;

/**
 * Manages player equipment, consumables, keys, and item capacity.
 */
public class Inventory {
    public static final int MAX_SLOTS = 16;

    private final List<Item> items = new ArrayList<>();
    private Item equippedWeapon;
    private Item equippedShield;

    public Inventory() {
        // Default starter loadout
        this.equippedWeapon = Item.getById("rusty_sword");
        this.equippedShield = Item.getById("wood_buckler");
        // Start with 2 minor health potions and 1 stamina elixir
        addItem(Item.getById("minor_health_potion"));
        addItem(Item.getById("minor_health_potion"));
        addItem(Item.getById("stamina_potion"));
    }

    public boolean addItem(Item item) {
        if (item == null) return false;
        if (items.size() >= MAX_SLOTS) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean equipWeapon(Item item) {
        if (item != null && item.getType() == Item.ItemType.WEAPON) {
            this.equippedWeapon = item;
            AudioManager.getInstance().playSFX(SoundType.SWORD_SWING);
            return true;
        }
        return false;
    }

    public boolean equipShield(Item item) {
        if (item != null && item.getType() == Item.ItemType.SHIELD) {
            this.equippedShield = item;
            AudioManager.getInstance().playSFX(SoundType.SHIELD_BLOCK);
            return true;
        }
        return false;
    }

    public boolean hasKey(String keyId) {
        for (Item item : items) {
            if (item.getType() == Item.ItemType.KEY && item.getId().equals(keyId)) {
                return true;
            }
        }
        return false;
    }

    public int getPotionCount() {
        int count = 0;
        for (Item item : items) {
            if (item.getType() == Item.ItemType.POTION) {
                count++;
            }
        }
        return count;
    }

    public boolean useQuickPotion(PlayerStats stats) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getType() == Item.ItemType.POTION && item.getHealAmount() > 0) {
                if (stats.getCurrentHP() >= stats.getMaxHP()) {
                    stats.setNotification("Health is already full!");
                    return false;
                }
                stats.heal(item.getHealAmount());
                stats.setNotification("Used " + item.getName() + " (+" + (int)item.getHealAmount() + " HP)");
                AudioManager.getInstance().playSFX(SoundType.POTION_DRINK);
                items.remove(i);
                return true;
            }
        }
        stats.setNotification("No health potions available!");
        return false;
    }

    public boolean useItem(Item item, PlayerStats stats) {
        if (item == null) return false;
        if (item.getType() == Item.ItemType.WEAPON) {
            return equipWeapon(item);
        } else if (item.getType() == Item.ItemType.SHIELD) {
            return equipShield(item);
        } else if (item.getType() == Item.ItemType.POTION) {
            if (item.getHealAmount() > 0) {
                stats.heal(item.getHealAmount());
            }
            if (item.getStaminaAmount() > 0) {
                stats.restoreStamina(item.getStaminaAmount());
            }
            stats.setNotification("Consumed " + item.getName());
            AudioManager.getInstance().playSFX(SoundType.POTION_DRINK);
            items.remove(item);
            return true;
        }
        return false;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }

    public Item getEquippedShield() {
        return equippedShield;
    }

    public void setEquippedWeapon(Item weapon) {
        this.equippedWeapon = weapon;
    }

    public void setEquippedShield(Item shield) {
        this.equippedShield = shield;
    }

    public void clear() {
        items.clear();
    }
}
