package com.dungeoncrawler.save;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Handles persistent game state saving and loading.
 */
public class SaveManager {
    private static final String SAVE_FILE = "savegame.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean hasSave() {
        File file = new File(SAVE_FILE);
        return file.exists() && file.length() > 0;
    }

    public static boolean hasSaveGame() {
        return hasSave();
    }

    public static boolean saveGame(SaveData data) {
        data.timestamp = System.currentTimeMillis();
        try (Writer writer = Files.newBufferedWriter(Paths.get(SAVE_FILE))) {
            GSON.toJson(data, writer);
            System.out.println("Game saved successfully to " + SAVE_FILE);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
            return false;
        }
    }

    public static SaveData loadGame() {
        if (!hasSave()) {
            return null;
        }
        try (Reader reader = Files.newBufferedReader(Paths.get(SAVE_FILE))) {
            SaveData data = GSON.fromJson(reader, SaveData.class);
            System.out.println("Game loaded successfully from " + SAVE_FILE);
            return data;
        } catch (Exception e) {
            System.err.println("Failed to load savegame: " + e.getMessage());
            return null;
        }
    }

    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
