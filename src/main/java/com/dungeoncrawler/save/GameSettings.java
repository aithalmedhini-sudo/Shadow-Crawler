package com.dungeoncrawler.save;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Stores configurable game settings and handles serialization.
 */
public class GameSettings {
    private static final String SETTINGS_FILE = "settings.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private float masterVolume = 0.8f;
    private float musicVolume = 0.6f;
    private float sfxVolume = 0.9f;
    private float mouseSensitivity = 2.5f;
    private float fov = 75f;
    private String graphicsQuality = "HIGH"; // LOW, MEDIUM, HIGH
    private boolean fullscreen = false;
    private int screenWidth = 1280;
    private int screenHeight = 720;

    public GameSettings() {
    }

    public static GameSettings load() {
        File file = new File(SETTINGS_FILE);
        if (file.exists()) {
            try (Reader reader = Files.newBufferedReader(Paths.get(SETTINGS_FILE))) {
                GameSettings settings = GSON.fromJson(reader, GameSettings.class);
                if (settings != null) {
                    return settings;
                }
            } catch (Exception e) {
                System.err.println("Could not load settings: " + e.getMessage());
            }
        }
        GameSettings defaultSettings = new GameSettings();
        defaultSettings.save();
        return defaultSettings;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(Paths.get(SETTINGS_FILE))) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            System.err.println("Could not save settings: " + e.getMessage());
        }
    }

    public float getMasterVolume() { return masterVolume; }
    public void setMasterVolume(float masterVolume) { this.masterVolume = Math.max(0f, Math.min(1f, masterVolume)); }

    public float getMusicVolume() { return musicVolume; }
    public void setMusicVolume(float musicVolume) { this.musicVolume = Math.max(0f, Math.min(1f, musicVolume)); }

    public float getSfxVolume() { return sfxVolume; }
    public void setSfxVolume(float sfxVolume) { this.sfxVolume = Math.max(0f, Math.min(1f, sfxVolume)); }

    public float getMouseSensitivity() { return mouseSensitivity; }
    public void setMouseSensitivity(float mouseSensitivity) { this.mouseSensitivity = Math.max(0.5f, Math.min(8f, mouseSensitivity)); }

    public float getFov() { return fov; }
    public void setFov(float fov) { this.fov = Math.max(60f, Math.min(100f, fov)); }

    public String getGraphicsQuality() { return graphicsQuality; }
    public void setGraphicsQuality(String graphicsQuality) { this.graphicsQuality = graphicsQuality; }

    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    public int getScreenWidth() { return screenWidth; }
    public void setScreenWidth(int screenWidth) { this.screenWidth = screenWidth; }

    public int getScreenHeight() { return screenHeight; }
    public void setScreenHeight(int screenHeight) { this.screenHeight = screenHeight; }
}
