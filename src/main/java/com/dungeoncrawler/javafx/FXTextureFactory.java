package com.dungeoncrawler.javafx;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;

/**
 * Procedurally generates high-resolution textures and PhongMaterials
 * for JavaFX 3D environments, first-person weapons, props, and hazards.
 */
public class FXTextureFactory {
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    private static final Map<String, PhongMaterial> MATERIAL_CACHE = new HashMap<>();

    public static Image getImage(String name) {
        if (IMAGE_CACHE.containsKey(name)) {
            return IMAGE_CACHE.get(name);
        }
        BufferedImage img;
        switch (name) {
            case "stone_wall":
                img = createStoneWall(256, 256, new Color(45, 45, 48), new Color(75, 75, 78));
                break;
            case "crypt_wall":
                img = createStoneWall(256, 256, new Color(32, 34, 38), new Color(60, 65, 72));
                break;
            case "cavern_wall":
                img = createCavernWall(256, 256);
                break;
            case "fortress_wall":
                img = createStoneWall(256, 256, new Color(20, 18, 22), new Color(48, 42, 50));
                break;
            case "stone_floor":
                img = createFloor(256, 256, new Color(70, 70, 72), new Color(30, 30, 32));
                break;
            case "cavern_floor":
                img = createFloor(256, 256, new Color(45, 40, 36), new Color(22, 20, 18));
                break;
            case "ceiling":
                img = createCeiling(256, 256);
                break;
            case "wood_planks":
                img = createWood(128, 128);
                break;
            case "metal_steel":
                img = createSteel(128, 128);
                break;
            case "metal_gold":
                img = createGold(128, 128);
                break;
            case "metal_rust":
                img = createRust(128, 128);
                break;
            case "water":
                img = createWater(128, 128);
                break;
            case "lava":
                img = createLava(128, 128);
                break;
            default:
                img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
                break;
        }

        Image fxImage = SwingFXUtils.toFXImage(img, null);
        IMAGE_CACHE.put(name, fxImage);
        return fxImage;
    }

    public static PhongMaterial getMaterial(String name) {
        if (MATERIAL_CACHE.containsKey(name)) {
            return MATERIAL_CACHE.get(name);
        }
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(getImage(name));
        if (name.contains("steel") || name.contains("gold")) {
            mat.setSpecularColor(javafx.scene.paint.Color.WHITE);
            mat.setSpecularPower(32.0);
        } else if (name.equals("lava")) {
            mat.setSpecularColor(javafx.scene.paint.Color.DARKORANGE);
        } else {
            mat.setSpecularColor(javafx.scene.paint.Color.rgb(40, 40, 40));
            mat.setSpecularPower(8.0);
        }
        MATERIAL_CACHE.put(name, mat);
        return mat;
    }

    public static PhongMaterial getColorMaterial(javafx.scene.paint.Color diffuse) {
        PhongMaterial mat = new PhongMaterial(diffuse);
        mat.setSpecularColor(javafx.scene.paint.Color.rgb(60, 60, 60));
        return mat;
    }

    private static BufferedImage createStoneWall(int w, int h, Color base, Color mortar) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(base);
        g.fillRect(0, 0, w, h);

        int rows = 8;
        int cols = 4;
        int bh = h / rows;
        int bw = w / cols;
        java.util.Random rnd = new java.util.Random(101);

        for (int r = 0; r < rows; r++) {
            int offset = (r % 2 == 0) ? 0 : bw / 2;
            for (int c = -1; c <= cols; c++) {
                int bx = c * bw + offset;
                int by = r * bh;
                int shade = 65 + rnd.nextInt(35);
                g.setColor(new Color(Math.min(255, base.getRed() + shade - 50),
                                     Math.min(255, base.getGreen() + shade - 50),
                                     Math.min(255, base.getBlue() + shade - 50)));
                g.fillRect(bx + 2, by + 2, bw - 4, bh - 4);
            }
        }
        g.dispose();
        return img;
    }

    private static BufferedImage createCavernWall(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(30, 26, 24));
        g.fillRect(0, 0, w, h);

        java.util.Random rnd = new java.util.Random(202);
        for (int i = 0; i < 400; i++) {
            int x = rnd.nextInt(w);
            int y = rnd.nextInt(h);
            int rw = 20 + rnd.nextInt(40);
            int rh = 15 + rnd.nextInt(30);
            int shade = 35 + rnd.nextInt(40);
            g.setColor(new Color(shade + 5, shade, shade - 5));
            g.fillRoundRect(x, y, rw, rh, 8, 8);
        }
        g.dispose();
        return img;
    }

    private static BufferedImage createFloor(int w, int h, Color tile, Color grout) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(grout);
        g.fillRect(0, 0, w, h);

        int tileSize = w / 4;
        java.util.Random rnd = new java.util.Random(303);
        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {
                int shade = rnd.nextInt(25);
                g.setColor(new Color(Math.max(0, tile.getRed() - shade),
                                     Math.max(0, tile.getGreen() - shade),
                                     Math.max(0, tile.getBlue() - shade)));
                g.fillRect(x + 2, y + 2, tileSize - 4, tileSize - 4);
            }
        }
        g.dispose();
        return img;
    }

    private static BufferedImage createCeiling(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(24, 24, 28));
        g.fillRect(0, 0, w, h);
        g.dispose();
        return img;
    }

    private static BufferedImage createWood(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(80, 48, 25));
        g.fillRect(0, 0, w, h);

        int plankH = h / 4;
        for (int p = 0; p < 4; p++) {
            g.setColor(new Color(95, 58, 30));
            g.fillRect(0, p * plankH + 2, w, plankH - 4);
        }
        g.dispose();
        return img;
    }

    private static BufferedImage createSteel(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        GradientPaint gp = new GradientPaint(0, 0, new Color(200, 205, 215), w, h, new Color(130, 135, 145));
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return img;
    }

    private static BufferedImage createGold(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 215, 0), w, h, new Color(180, 130, 10));
        g.setPaint(gp);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return img;
    }

    private static BufferedImage createRust(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(75, 45, 30));
        g.fillRect(0, 0, w, h);
        g.dispose();
        return img;
    }

    private static BufferedImage createWater(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(20, 60, 120));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(40, 100, 180));
        for (int i = 0; i < 20; i++) {
            g.drawOval((i * 13) % w, (i * 17) % h, 40, 20);
        }
        g.dispose();
        return img;
    }

    private static BufferedImage createLava(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(180, 40, 10));
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(255, 160, 20));
        for (int i = 0; i < 30; i++) {
            g.fillOval((i * 19) % w, (i * 23) % h, 25, 15);
        }
        g.dispose();
        return img;
    }
}
