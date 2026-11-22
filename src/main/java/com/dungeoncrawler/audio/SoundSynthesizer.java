package com.dungeoncrawler.audio;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/**
 * Procedurally generates high-quality 44.1kHz 16-bit PCM audio waveforms
 * for all sound effects and background musical themes.
 */
public class SoundSynthesizer {
    public static final float SAMPLE_RATE = 44100f;
    private static final AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);

    public static AudioInputStream generate(SoundType type) {
        byte[] data;
        switch (type) {
            case SWORD_SWING:
                data = genSwordSwing();
                break;
            case SWORD_HIT:
                data = genSwordHit();
                break;
            case SHIELD_BLOCK:
                data = genShieldBlock();
                break;
            case PLAYER_HURT:
                data = genPlayerHurt();
                break;
            case PLAYER_STEP:
                data = genPlayerStep();
                break;
            case ENEMY_HURT:
                data = genEnemyHurt();
                break;
            case ENEMY_ATTACK:
                data = genEnemyAttack();
                break;
            case ENEMY_DEATH:
                data = genEnemyDeath();
                break;
            case CHEST_OPEN:
                data = genChestOpen();
                break;
            case DOOR_OPEN:
                data = genDoorOpen();
                break;
            case DOOR_LOCKED:
                data = genDoorLocked();
                break;
            case POTION_DRINK:
                data = genPotionDrink();
                break;
            case LEVEL_UP:
                data = genLevelUp();
                break;
            case TRAP_TRIGGER:
                data = genTrapTrigger();
                break;
            case BOSS_ROAR:
                data = genBossRoar();
                break;
            case BOSS_SLAM:
                data = genBossSlam();
                break;
            case BUTTON_CLICK:
                data = genButtonClick();
                break;
            case BGM_MENU:
                data = genBgmMenu();
                break;
            case BGM_CRYPT:
                data = genBgmCrypt();
                break;
            case BGM_CAVERNS:
                data = genBgmCaverns();
                break;
            case BGM_BOSS:
                data = genBgmBoss();
                break;
            case BGM_VICTORY:
                data = genBgmVictory();
                break;
            case BGM_DEFEAT:
                data = genBgmDefeat();
                break;
            default:
                data = new byte[100];
                break;
        }
        return new AudioInputStream(new ByteArrayInputStream(data), FORMAT, data.length / 2);
    }

    private static byte[] toBytes(short[] samples) {
        byte[] bytes = new byte[samples.length * 2];
        for (int i = 0; i < samples.length; i++) {
            short s = samples[i];
            bytes[i * 2] = (byte) (s & 0xFF);
            bytes[i * 2 + 1] = (byte) ((s >> 8) & 0xFF);
        }
        return bytes;
    }

    private static short clamp(double val) {
        val = Math.max(-1.0, Math.min(1.0, val));
        return (short) (val * 32767);
    }

    private static byte[] genSwordSwing() {
        int len = (int) (SAMPLE_RATE * 0.28);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(42);
        for (int i = 0; i < len; i++) {
            double t = (double) i / len;
            double env = Math.sin(t * Math.PI);
            double freq = 1800 - 1200 * t;
            double noise = (rnd.nextDouble() * 2.0 - 1.0);
            double tone = Math.sin(2 * Math.PI * freq * (double) i / SAMPLE_RATE);
            s[i] = clamp((noise * 0.6 + tone * 0.4) * env * 0.85);
        }
        return toBytes(s);
    }

    private static byte[] genSwordHit() {
        int len = (int) (SAMPLE_RATE * 0.35);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(1337);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 14.0);
            double clang = Math.sin(2 * Math.PI * 880 * t) * 0.4 +
                           Math.sin(2 * Math.PI * 1320 * t) * 0.3 +
                           Math.sin(2 * Math.PI * 2640 * t) * 0.15;
            double thud = Math.sin(2 * Math.PI * 110 * t) * Math.exp(-t * 22.0) * 0.5;
            double noise = (rnd.nextDouble() * 2.0 - 1.0) * Math.exp(-t * 30.0) * 0.3;
            s[i] = clamp((clang + thud + noise) * env * 0.9);
        }
        return toBytes(s);
    }

    private static byte[] genShieldBlock() {
        int len = (int) (SAMPLE_RATE * 0.4);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(999);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 10.0);
            double ironRing = Math.sin(2 * Math.PI * 330 * t) * 0.5 +
                              Math.sin(2 * Math.PI * 660 * t) * 0.3 +
                              Math.sin(2 * Math.PI * 990 * t) * 0.15;
            double thud = Math.sin(2 * Math.PI * 90 * t) * Math.exp(-t * 18.0) * 0.6;
            double metalCrack = (rnd.nextDouble() * 2.0 - 1.0) * Math.exp(-t * 40.0) * 0.4;
            s[i] = clamp((ironRing + thud + metalCrack) * env);
        }
        return toBytes(s);
    }

    private static byte[] genPlayerHurt() {
        int len = (int) (SAMPLE_RATE * 0.3);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 9.0);
            double pitch = 150 - 60 * (t / 0.3);
            double voice = Math.sin(2 * Math.PI * pitch * t) + 0.4 * Math.sin(2 * Math.PI * (pitch * 2) * t);
            double thud = Math.sin(2 * Math.PI * 75 * t) * 0.5;
            s[i] = clamp((voice * 0.6 + thud * 0.4) * env * 0.85);
        }
        return toBytes(s);
    }

    private static byte[] genPlayerStep() {
        int len = (int) (SAMPLE_RATE * 0.1);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(System.nanoTime());
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 45.0);
            double thud = Math.sin(2 * Math.PI * 95 * t);
            double gravel = (rnd.nextDouble() * 2.0 - 1.0) * 0.4;
            s[i] = clamp((thud * 0.7 + gravel * 0.3) * env * 0.6);
        }
        return toBytes(s);
    }

    private static byte[] genEnemyHurt() {
        int len = (int) (SAMPLE_RATE * 0.25);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(77);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 12.0);
            double boneCrack = (rnd.nextDouble() * 2.0 - 1.0) * Math.exp(-t * 35.0);
            double squeal = Math.sin(2 * Math.PI * (400 - 150 * t) * t) * 0.5;
            s[i] = clamp((boneCrack * 0.6 + squeal * 0.4) * env * 0.8);
        }
        return toBytes(s);
    }

    private static byte[] genEnemyAttack() {
        int len = (int) (SAMPLE_RATE * 0.3);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / len;
            double env = Math.sin(t * Math.PI);
            double growl = Math.sin(2 * Math.PI * 90 * (i / SAMPLE_RATE)) * (1.0 + 0.3 * Math.sin(2 * Math.PI * 18 * (i / SAMPLE_RATE)));
            s[i] = clamp(growl * env * 0.7);
        }
        return toBytes(s);
    }

    private static byte[] genEnemyDeath() {
        int len = (int) (SAMPLE_RATE * 0.7);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(555);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 4.5);
            double groan = Math.sin(2 * Math.PI * (120 - 70 * (t / 0.7)) * t);
            double crumble = (rnd.nextDouble() * 2.0 - 1.0) * 0.3;
            s[i] = clamp((groan * 0.7 + crumble * 0.3) * env * 0.9);
        }
        return toBytes(s);
    }

    private static byte[] genChestOpen() {
        int len = (int) (SAMPLE_RATE * 0.8);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double val = 0;
            // Wooden creak (0 to 0.3s)
            if (t < 0.35) {
                double creakEnv = Math.sin((t / 0.35) * Math.PI);
                double creakFreq = 140 + 70 * (t / 0.35);
                val += Math.sin(2 * Math.PI * creakFreq * t) * creakEnv * 0.4;
            }
            // Golden chime arpeggio (starting at 0.25s)
            if (t > 0.2) {
                double chimeT = t - 0.2;
                double chimeEnv = Math.exp(-chimeT * 6.0);
                double note1 = Math.sin(2 * Math.PI * 1046 * chimeT); // C6
                double note2 = (chimeT > 0.1) ? Math.sin(2 * Math.PI * 1318 * (chimeT - 0.1)) * Math.exp(-(chimeT - 0.1) * 6.0) : 0; // E6
                double note3 = (chimeT > 0.2) ? Math.sin(2 * Math.PI * 1568 * (chimeT - 0.2)) * Math.exp(-(chimeT - 0.2) * 5.0) : 0; // G6
                double note4 = (chimeT > 0.3) ? Math.sin(2 * Math.PI * 2093 * (chimeT - 0.3)) * Math.exp(-(chimeT - 0.3) * 4.0) : 0; // C7
                val += (note1 * 0.3 + note2 * 0.3 + note3 * 0.3 + note4 * 0.4) * chimeEnv;
            }
            s[i] = clamp(val * 0.85);
        }
        return toBytes(s);
    }

    private static byte[] genDoorOpen() {
        int len = (int) (SAMPLE_RATE * 0.7);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(88);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.sin((t / 0.7) * Math.PI);
            double grind = (rnd.nextDouble() * 2.0 - 1.0) * 0.35;
            double stoneHone = Math.sin(2 * Math.PI * (80 + 30 * t) * t) * 0.5;
            s[i] = clamp((grind + stoneHone) * env * 0.75);
        }
        return toBytes(s);
    }

    private static byte[] genDoorLocked() {
        int len = (int) (SAMPLE_RATE * 0.3);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double clank1 = Math.sin(2 * Math.PI * 550 * t) * Math.exp(-t * 30.0);
            double clank2 = (t > 0.12) ? Math.sin(2 * Math.PI * 650 * (t - 0.12)) * Math.exp(-(t - 0.12) * 30.0) : 0;
            s[i] = clamp((clank1 + clank2) * 0.7);
        }
        return toBytes(s);
    }

    private static byte[] genPotionDrink() {
        int len = (int) (SAMPLE_RATE * 0.65);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double val = 0;
            // 3 gulp pulses
            for (int g = 0; g < 3; g++) {
                double gt = t - g * 0.15;
                if (gt >= 0 && gt < 0.14) {
                    double genv = Math.sin((gt / 0.14) * Math.PI);
                    double freq = 380 - 180 * (gt / 0.14);
                    val += Math.sin(2 * Math.PI * freq * gt) * genv * 0.45;
                }
            }
            // Magic sparkle at end
            if (t > 0.4) {
                double st = t - 0.4;
                val += Math.sin(2 * Math.PI * 1760 * st) * Math.exp(-st * 8.0) * 0.3;
            }
            s[i] = clamp(val * 0.85);
        }
        return toBytes(s);
    }

    private static byte[] genLevelUp() {
        int len = (int) (SAMPLE_RATE * 1.4);
        short[] s = new short[len];
        double[] freqs = { 523.25, 659.25, 783.99, 1046.50 }; // C5, E5, G5, C6
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double val = 0;
            for (int n = 0; n < freqs.length; n++) {
                double noteStartTime = n * 0.18;
                if (t >= noteStartTime) {
                    double nt = t - noteStartTime;
                    double env = Math.exp(-nt * (n == freqs.length - 1 ? 2.5 : 5.0));
                    double f = freqs[n];
                    double brass = Math.sin(2 * Math.PI * f * nt) + 0.3 * Math.sin(2 * Math.PI * f * 2 * nt);
                    val += brass * env * 0.28;
                }
            }
            s[i] = clamp(val * 0.9);
        }
        return toBytes(s);
    }

    private static byte[] genTrapTrigger() {
        int len = (int) (SAMPLE_RATE * 0.35);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(111);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double click = (t < 0.05) ? Math.sin(2 * Math.PI * 1200 * t) * Math.exp(-t * 80.0) : 0;
            double thrust = (t >= 0.04) ? Math.sin(2 * Math.PI * 450 * (t - 0.04)) * Math.exp(-(t - 0.04) * 15.0) : 0;
            double scrape = (rnd.nextDouble() * 2.0 - 1.0) * Math.exp(-t * 20.0) * 0.4;
            s[i] = clamp((click * 0.5 + thrust * 0.5 + scrape) * 0.85);
        }
        return toBytes(s);
    }

    private static byte[] genBossRoar() {
        int len = (int) (SAMPLE_RATE * 1.5);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(666);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.sin((t / 1.5) * Math.PI);
            double mod = 1.0 + 0.3 * Math.sin(2 * Math.PI * 25 * t);
            double f = 65 + 35 * Math.sin(t * 3.0);
            double wave = Math.sin(2 * Math.PI * f * t) * mod;
            double noise = (rnd.nextDouble() * 2.0 - 1.0) * 0.35;
            s[i] = clamp((wave * 0.7 + noise) * env * 0.95);
        }
        return toBytes(s);
    }

    private static byte[] genBossSlam() {
        int len = (int) (SAMPLE_RATE * 0.8);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(321);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 5.0);
            double sub = Math.sin(2 * Math.PI * 45 * t);
            double rumble = (rnd.nextDouble() * 2.0 - 1.0) * 0.5;
            s[i] = clamp((sub * 0.7 + rumble * 0.4) * env);
        }
        return toBytes(s);
    }

    private static byte[] genButtonClick() {
        int len = (int) (SAMPLE_RATE * 0.06);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double env = Math.exp(-t * 70.0);
            s[i] = clamp(Math.sin(2 * Math.PI * 750 * t) * env * 0.6);
        }
        return toBytes(s);
    }

    // Background Themes (Loops 8.0 seconds)
    private static byte[] genBgmMenu() {
        int len = (int) (SAMPLE_RATE * 8.0);
        short[] s = new short[len];
        // Dark mystic pad in A minor (A2, C3, E3, A3)
        double[][] chords = {
            { 110.0, 130.81, 164.81, 220.0 }, // Am
            { 87.31, 130.81, 174.61, 220.0 }, // F
            { 98.00, 123.47, 146.83, 196.0 }, // G
            { 82.41, 123.47, 164.81, 196.0 }  // Em
        };
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            int chordIdx = ((int) (t / 2.0)) % 4;
            double chordT = t % 2.0;
            double env = Math.sin((chordT / 2.0) * Math.PI);
            double[] c = chords[chordIdx];
            double sample = 0;
            for (double freq : c) {
                sample += Math.sin(2 * Math.PI * freq * t) + 0.25 * Math.sin(2 * Math.PI * (freq * 2) * t);
            }
            s[i] = clamp(sample * 0.15 * env);
        }
        return toBytes(s);
    }

    private static byte[] genBgmCrypt() {
        int len = (int) (SAMPLE_RATE * 8.0);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(902);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            // Eerie low drone at 55Hz (A1)
            double drone = Math.sin(2 * Math.PI * 55 * t) + 0.3 * Math.sin(2 * Math.PI * 110 * t);
            // Atmospheric cavern wind
            double wind = (rnd.nextDouble() * 2.0 - 1.0) * 0.08 * (1.0 + 0.5 * Math.sin(t * 1.5));
            // Intermittent bell chime at t = 2.0 and t = 6.0
            double bell = 0;
            if (t >= 2.0 && t < 4.0) {
                double bt = t - 2.0;
                bell += Math.sin(2 * Math.PI * 880 * bt) * Math.exp(-bt * 3.0) * 0.2;
            }
            if (t >= 6.0 && t < 8.0) {
                double bt = t - 6.0;
                bell += Math.sin(2 * Math.PI * 784 * bt) * Math.exp(-bt * 3.0) * 0.2;
            }
            s[i] = clamp((drone * 0.3 + wind + bell) * 0.7);
        }
        return toBytes(s);
    }

    private static byte[] genBgmCaverns() {
        int len = (int) (SAMPLE_RATE * 8.0);
        short[] s = new short[len];
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            // Pulsing low bass rhythm (quarter notes at 120 bpm = every 0.5s)
            double beatT = t % 0.5;
            double bassEnv = Math.exp(-beatT * 12.0);
            double bassFreq = (t < 4.0) ? 65.41 : 58.27; // C2 then Bb1
            double bass = Math.sin(2 * Math.PI * bassFreq * beatT) * bassEnv * 0.4;
            // Echoing flute-like melody
            double mel = 0;
            double melFreq = (t < 2.0) ? 392.0 : (t < 4.0) ? 440.0 : (t < 6.0) ? 349.2 : 329.6;
            mel = Math.sin(2 * Math.PI * melFreq * t) * 0.15 * (1.0 + 0.2 * Math.sin(t * 10));
            s[i] = clamp((bass + mel) * 0.7);
        }
        return toBytes(s);
    }

    private static byte[] genBgmBoss() {
        int len = (int) (SAMPLE_RATE * 8.0);
        short[] s = new short[len];
        java.util.Random rnd = new java.util.Random(404);
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            // Urgent drum hits (every 0.25s)
            double drumT = t % 0.25;
            double drumEnv = Math.exp(-drumT * 20.0);
            double drum = Math.sin(2 * Math.PI * 80 * drumT) * 0.5 + (rnd.nextDouble() * 2.0 - 1.0) * 0.2;
            // Aggressive saw-tooth bass in D minor
            double sawT = (t * 73.42) % 1.0; // D2
            double saw = (sawT * 2.0 - 1.0) * 0.3;
            // Tension strings
            double brass = Math.sin(2 * Math.PI * 293.66 * t) * 0.25 + Math.sin(2 * Math.PI * 349.23 * t) * 0.25;
            s[i] = clamp((drum * drumEnv + saw + brass) * 0.65);
        }
        return toBytes(s);
    }

    private static byte[] genBgmVictory() {
        int len = (int) (SAMPLE_RATE * 5.0);
        short[] s = new short[len];
        double[] notes = { 293.66, 369.99, 440.00, 587.33 }; // D major arpeggio
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double val = 0;
            for (int n = 0; n < notes.length; n++) {
                double st = n * 0.35;
                if (t >= st) {
                    double dt = t - st;
                    double env = Math.exp(-dt * 0.9);
                    val += Math.sin(2 * Math.PI * notes[n] * dt) * env * 0.25;
                }
            }
            s[i] = clamp(val * 0.85);
        }
        return toBytes(s);
    }

    private static byte[] genBgmDefeat() {
        int len = (int) (SAMPLE_RATE * 4.0);
        short[] s = new short[len];
        double[] notes = { 220.0, 196.0, 174.6, 146.8 }; // Descending mournful notes
        for (int i = 0; i < len; i++) {
            double t = (double) i / SAMPLE_RATE;
            double val = 0;
            for (int n = 0; n < notes.length; n++) {
                double st = n * 0.7;
                if (t >= st) {
                    double dt = t - st;
                    val += Math.sin(2 * Math.PI * notes[n] * dt) * Math.exp(-dt * 1.5) * 0.3;
                }
            }
            s[i] = clamp(val * 0.75);
        }
        return toBytes(s);
    }
}
