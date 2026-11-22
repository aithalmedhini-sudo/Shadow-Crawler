package com.dungeoncrawler.audio;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.*;
import com.dungeoncrawler.save.GameSettings;

/**
 * Handles audio playback for sound effects and background music.
 */
public class AudioManager {
    private static AudioManager instance;
    private final ExecutorService sfxPool = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<SoundType, byte[]> audioCache = new ConcurrentHashMap<>();
    
    private Clip currentMusicClip;
    private SoundType currentMusicType;
    private float masterVolume = 0.8f;
    private float musicVolume = 0.6f;
    private float sfxVolume = 0.9f;

    private AudioManager() {
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void init(GameSettings settings) {
        if (settings != null) {
            this.masterVolume = settings.getMasterVolume();
            this.musicVolume = settings.getMusicVolume();
            this.sfxVolume = settings.getSfxVolume();
        }
        // Warm up critical sound caches in background
        sfxPool.submit(() -> {
            preload(SoundType.SWORD_SWING);
            preload(SoundType.SWORD_HIT);
            preload(SoundType.PLAYER_STEP);
            preload(SoundType.BUTTON_CLICK);
        });
    }

    private byte[] getAudioBytes(SoundType type) {
        return audioCache.computeIfAbsent(type, k -> {
            try {
                AudioInputStream ais = SoundSynthesizer.generate(k);
                byte[] data = ais.readAllBytes();
                ais.close();
                return data;
            } catch (Exception e) {
                System.err.println("Error generating sound " + k + ": " + e.getMessage());
                return new byte[0];
            }
        });
    }

    public void preload(SoundType type) {
        getAudioBytes(type);
    }

    public void playSFX(SoundType type) {
        if (masterVolume <= 0.001f || sfxVolume <= 0.001f) return;
        sfxPool.submit(() -> {
            try {
                byte[] data = getAudioBytes(type);
                if (data.length == 0) return;

                AudioInputStream ais = new AudioInputStream(
                        new java.io.ByteArrayInputStream(data),
                        new AudioFormat(SoundSynthesizer.SAMPLE_RATE, 16, 1, true, false),
                        data.length / 2
                );
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                applyVolume(clip, masterVolume * sfxVolume);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (Exception e) {
                // Silently handle audio driver constraints
            }
        });
    }

    public synchronized void playMusic(SoundType type) {
        if (currentMusicType == type && currentMusicClip != null && currentMusicClip.isRunning()) {
            return;
        }
        stopMusic();
        this.currentMusicType = type;

        if (masterVolume <= 0.001f || musicVolume <= 0.001f) return;

        try {
            byte[] data = getAudioBytes(type);
            if (data.length == 0) return;

            AudioInputStream ais = new AudioInputStream(
                    new java.io.ByteArrayInputStream(data),
                    new AudioFormat(SoundSynthesizer.SAMPLE_RATE, 16, 1, true, false),
                    data.length / 2
            );
            currentMusicClip = AudioSystem.getClip();
            currentMusicClip.open(ais);
            applyVolume(currentMusicClip, masterVolume * musicVolume);
            currentMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentMusicClip.start();
        } catch (Exception e) {
            System.err.println("Could not play music: " + e.getMessage());
        }
    }

    public synchronized void stopMusic() {
        if (currentMusicClip != null) {
            try {
                currentMusicClip.stop();
                currentMusicClip.close();
            } catch (Exception ignored) {}
            currentMusicClip = null;
            currentMusicType = null;
        }
    }

    public void setVolumes(float master, float music, float sfx) {
        this.masterVolume = Math.max(0f, Math.min(1f, master));
        this.musicVolume = Math.max(0f, Math.min(1f, music));
        this.sfxVolume = Math.max(0f, Math.min(1f, sfx));

        if (currentMusicClip != null && currentMusicClip.isOpen()) {
            applyVolume(currentMusicClip, masterVolume * musicVolume);
        }
    }

    public void setMusicVolume(float vol) { setVolumes(this.masterVolume, vol, this.sfxVolume); }
    public void setSfxVolume(float vol) { setVolumes(this.masterVolume, this.musicVolume, vol); }
    public void setMasterVolume(float vol) { setVolumes(vol, this.musicVolume, this.sfxVolume); }

    private void applyVolume(Clip clip, float vol) {
        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (vol > 0.0001f) ? (float) (20.0 * Math.log10(vol)) : -80.0f;
                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(dB);
            }
        } catch (Exception ignored) {}
    }

    public float getMasterVolume() { return masterVolume; }
    public float getMusicVolume() { return musicVolume; }
    public float getSfxVolume() { return sfxVolume; }
}
