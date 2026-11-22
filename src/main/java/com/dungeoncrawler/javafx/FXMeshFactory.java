package com.dungeoncrawler.javafx;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * Procedurally creates detailed JavaFX 3D models for first-person viewmodels,
 * dungeon fixtures, interactables, and enemies.
 * All coordinates adhere to JavaFX 3D: Floor is Y=0, -Y is UP, +Y is DOWN.
 */
public class FXMeshFactory {

    // First-Person Viewmodel: Steel Sword
    public static Group createSwordFP() {
        Group sword = new Group();
        sword.setId("FPSword");

        PhongMaterial steelMat = FXTextureFactory.getMaterial("metal_steel");
        PhongMaterial goldMat = FXTextureFactory.getMaterial("metal_gold");
        PhongMaterial gripMat = FXTextureFactory.getMaterial("wood_planks");

        // Blade (double-edged, extends up in negative Y)
        Box blade = new Box(0.05, 0.9, 0.015);
        blade.setMaterial(steelMat);
        blade.setTranslateY(-0.45);
        sword.getChildren().add(blade);

        // Crossguard
        Box guard = new Box(0.24, 0.035, 0.04);
        guard.setMaterial(goldMat);
        guard.setTranslateY(0.02);
        sword.getChildren().add(guard);

        // Grip
        Cylinder grip = new Cylinder(0.022, 0.18);
        grip.setMaterial(gripMat);
        grip.setTranslateY(0.12);
        sword.getChildren().add(grip);

        // Pommel
        Sphere pommel = new Sphere(0.035);
        pommel.setMaterial(goldMat);
        pommel.setTranslateY(0.23);
        sword.getChildren().add(pommel);

        return sword;
    }

    // First-Person Viewmodel: Battleaxe of Embers
    public static Group createAxeFP() {
        Group axe = new Group();
        axe.setId("FPAxe");

        PhongMaterial ironMat = FXTextureFactory.getMaterial("metal_rust");
        PhongMaterial fireMat = FXTextureFactory.getColorMaterial(Color.rgb(255, 100, 20));
        PhongMaterial woodMat = FXTextureFactory.getMaterial("wood_planks");

        // Shaft
        Cylinder shaft = new Cylinder(0.026, 0.95);
        shaft.setMaterial(woodMat);
        shaft.setTranslateY(-0.25);
        axe.getChildren().add(shaft);

        // Crescent Axe Blade
        Box blade = new Box(0.28, 0.38, 0.03);
        blade.setMaterial(ironMat);
        blade.setTranslateX(0.14);
        blade.setTranslateY(-0.58);
        axe.getChildren().add(blade);

        // Glowing Core
        Box glow = new Box(0.12, 0.22, 0.04);
        glow.setMaterial(fireMat);
        glow.setTranslateX(0.14);
        glow.setTranslateY(-0.58);
        axe.getChildren().add(glow);

        return axe;
    }

    // First-Person Viewmodel: Shield
    public static Group createShieldFP() {
        Group shield = new Group();
        shield.setId("FPShield");

        PhongMaterial woodMat = FXTextureFactory.getMaterial("wood_planks");
        PhongMaterial ironMat = FXTextureFactory.getMaterial("metal_steel");
        PhongMaterial goldMat = FXTextureFactory.getMaterial("metal_gold");

        // Main Board
        Box board = new Box(0.48, 0.65, 0.04);
        board.setMaterial(woodMat);
        shield.getChildren().add(board);

        // Iron Rim
        Box rimT = new Box(0.50, 0.03, 0.05);
        rimT.setMaterial(ironMat);
        rimT.setTranslateY(-0.32);
        shield.getChildren().add(rimT);

        Box rimB = new Box(0.50, 0.03, 0.05);
        rimB.setMaterial(ironMat);
        rimB.setTranslateY(0.32);
        shield.getChildren().add(rimB);

        Box rimL = new Box(0.03, 0.65, 0.05);
        rimL.setMaterial(ironMat);
        rimL.setTranslateX(-0.24);
        shield.getChildren().add(rimL);

        Box rimR = new Box(0.03, 0.65, 0.05);
        rimR.setMaterial(ironMat);
        rimR.setTranslateX(0.24);
        shield.getChildren().add(rimR);

        // Center Iron Umbo Boss
        Sphere boss = new Sphere(0.08);
        boss.setMaterial(goldMat);
        boss.setTranslateZ(-0.04);
        shield.getChildren().add(boss);

        return shield;
    }

    // 3D Treasure Chest (rests at Y=0, lid opens upwards into -Y)
    public static Group createChest() {
        Group chest = new Group();
        chest.setId("TreasureChest");

        PhongMaterial woodMat = FXTextureFactory.getMaterial("wood_planks");
        PhongMaterial goldMat = FXTextureFactory.getMaterial("metal_gold");

        // Base tub (rests on floor Y=0, top at Y=-0.5)
        Box base = new Box(0.9, 0.5, 0.6);
        base.setMaterial(woodMat);
        base.setTranslateY(-0.25);
        chest.getChildren().add(base);

        // Gold Trim
        Box trimBottom = new Box(0.92, 0.06, 0.62);
        trimBottom.setMaterial(goldMat);
        trimBottom.setTranslateY(-0.03);
        chest.getChildren().add(trimBottom);

        Box trimTop = new Box(0.92, 0.06, 0.62);
        trimTop.setMaterial(goldMat);
        trimTop.setTranslateY(-0.47);
        chest.getChildren().add(trimTop);

        // Lock clasp
        Box lock = new Box(0.12, 0.12, 0.06);
        lock.setMaterial(goldMat);
        lock.setTranslateY(-0.35);
        lock.setTranslateZ(-0.31);
        chest.getChildren().add(lock);

        // Hinged Lid (pivot at back edge: Y=-0.5, Z=0.3)
        Group lidHinge = new Group();
        lidHinge.setId("LidHinge");
        lidHinge.setTranslateY(-0.5);
        lidHinge.setTranslateZ(0.3);

        Box lid = new Box(0.92, 0.16, 0.62);
        lid.setMaterial(woodMat);
        lid.setTranslateY(-0.08);
        lid.setTranslateZ(-0.3);
        lidHinge.getChildren().add(lid);

        Box lidTrim = new Box(0.94, 0.04, 0.64);
        lidTrim.setMaterial(goldMat);
        lidTrim.setTranslateY(-0.16);
        lidTrim.setTranslateZ(-0.3);
        lidHinge.getChildren().add(lidTrim);

        chest.getChildren().add(lidHinge);
        return chest;
    }

    // 3D Hinged Door (stands at Y=0, height 3.6m into -Y)
    public static Group createDoor(String wallTex) {
        Group doorRoot = new Group();
        doorRoot.setId("DoorRoot");

        PhongMaterial wallMat = FXTextureFactory.getMaterial(wallTex);
        PhongMaterial woodMat = FXTextureFactory.getMaterial("wood_planks");
        PhongMaterial ironMat = FXTextureFactory.getMaterial("metal_rust");

        // Door Frame Left (height 3.6, from Y=0 to Y=-3.6)
        Box frameL = new Box(0.25, 3.6, 0.35);
        frameL.setMaterial(wallMat);
        frameL.setTranslateX(-1.5);
        frameL.setTranslateY(-1.8);
        doorRoot.getChildren().add(frameL);

        // Door Frame Right
        Box frameR = new Box(0.25, 3.6, 0.35);
        frameR.setMaterial(wallMat);
        frameR.setTranslateX(1.5);
        frameR.setTranslateY(-1.8);
        doorRoot.getChildren().add(frameR);

        // Door Frame Top Lintle
        Box frameT = new Box(3.25, 0.35, 0.35);
        frameT.setMaterial(wallMat);
        frameT.setTranslateY(-3.75);
        doorRoot.getChildren().add(frameT);

        // Hinged Door Leaf (pivot at left frame: X=-1.35, Y=-1.8)
        Group leafHinge = new Group();
        leafHinge.setId("LeafHinge");
        leafHinge.setTranslateX(-1.35);
        leafHinge.setTranslateY(-1.8);

        Box leaf = new Box(2.7, 3.5, 0.15);
        leaf.setMaterial(woodMat);
        leaf.setTranslateX(1.35);
        leafHinge.getChildren().add(leaf);

        // Iron Bands
        Box band1 = new Box(2.72, 0.18, 0.18);
        band1.setMaterial(ironMat);
        band1.setTranslateX(1.35);
        band1.setTranslateY(-0.9);
        leafHinge.getChildren().add(band1);

        Box band2 = new Box(2.72, 0.18, 0.18);
        band2.setMaterial(ironMat);
        band2.setTranslateX(1.35);
        band2.setTranslateY(0.9);
        leafHinge.getChildren().add(band2);

        // Ring Handle
        Cylinder handle = new Cylinder(0.06, 0.04);
        handle.setMaterial(ironMat);
        handle.setRotate(90);
        handle.setTranslateX(2.3);
        handle.setTranslateY(0.0);
        handle.setTranslateZ(-0.12);
        leafHinge.getChildren().add(handle);

        doorRoot.getChildren().add(leafHinge);
        return doorRoot;
    }

    // 3D Floor Spike Trap (rests on floor at Y=0)
    public static Group createSpikeTrap() {
        Group trap = new Group();
        trap.setId("SpikeTrap");

        PhongMaterial plateMat = FXTextureFactory.getMaterial("metal_rust");
        PhongMaterial spikeMat = FXTextureFactory.getMaterial("metal_steel");

        // Base Plate
        Box plate = new Box(1.8, 0.05, 1.8);
        plate.setMaterial(plateMat);
        plate.setTranslateY(0.02);
        trap.getChildren().add(plate);

        // Retractable Spikes Group (extends into -Y when triggered)
        Group spikes = new Group();
        spikes.setId("Spikes");

        for (int sx = -1; sx <= 1; sx++) {
            for (int sz = -1; sz <= 1; sz++) {
                Cylinder spike = new Cylinder(0.04, 0.6);
                spike.setMaterial(spikeMat);
                spike.setTranslateX(sx * 0.55);
                spike.setTranslateY(-0.3);
                spike.setTranslateZ(sz * 0.55);
                spikes.getChildren().add(spike);
            }
        }
        spikes.setTranslateY(0.55); // Initially concealed below floor
        trap.getChildren().add(spikes);
        return trap;
    }

    // 3D Exit Portal (stands on floor at Y=0, extends into -Y)
    public static Group createExitPortal() {
        Group portal = new Group();
        portal.setId("ExitPortal");

        PhongMaterial stoneMat = FXTextureFactory.getMaterial("fortress_wall");
        PhongMaterial magicMat = FXTextureFactory.getColorMaterial(Color.rgb(50, 180, 255));

        // Pillars
        Cylinder pL = new Cylinder(0.25, 3.8);
        pL.setMaterial(stoneMat);
        pL.setTranslateX(-1.5);
        pL.setTranslateY(-1.9);
        portal.getChildren().add(pL);

        Cylinder pR = new Cylinder(0.25, 3.8);
        pR.setMaterial(stoneMat);
        pR.setTranslateX(1.5);
        pR.setTranslateY(-1.9);
        portal.getChildren().add(pR);

        // Arch
        Box arch = new Box(3.6, 0.4, 0.6);
        arch.setMaterial(stoneMat);
        arch.setTranslateY(-3.8);
        portal.getChildren().add(arch);

        // Glowing Energy Barrier
        Box vortex = new Box(2.8, 3.4, 0.1);
        vortex.setMaterial(magicMat);
        vortex.setTranslateY(-1.9);
        portal.getChildren().add(vortex);

        return portal;
    }

    // 3D Wall Torch (bracket at -Y, flame above)
    public static Group createTorch() {
        Group torch = new Group();
        torch.setId("Torch");

        PhongMaterial rustMat = FXTextureFactory.getMaterial("metal_rust");
        PhongMaterial woodMat = FXTextureFactory.getMaterial("wood_planks");
        PhongMaterial flameMat = FXTextureFactory.getColorMaterial(Color.rgb(255, 150, 20));

        // Wall bracket
        Box bracket = new Box(0.06, 0.25, 0.04);
        bracket.setMaterial(rustMat);
        bracket.setTranslateY(0.0);
        torch.getChildren().add(bracket);

        // Angled torch shaft
        Cylinder shaft = new Cylinder(0.04, 0.45);
        shaft.setMaterial(woodMat);
        shaft.setTranslateZ(-0.25);
        shaft.setTranslateY(-0.15);
        torch.getChildren().add(shaft);

        // Flame core (above shaft in -Y)
        Sphere flame = new Sphere(0.12);
        flame.setMaterial(flameMat);
        flame.setTranslateZ(-0.25);
        flame.setTranslateY(-0.42);
        torch.getChildren().add(flame);

        return torch;
    }

    // 3D Pillar (stands on floor at Y=0, extends to -height)
    public static Group createPillar(String wallTex, double height) {
        Group pillar = new Group();
        PhongMaterial mat = FXTextureFactory.getMaterial(wallTex);

        Box base = new Box(0.9, 0.35, 0.9);
        base.setMaterial(mat);
        base.setTranslateY(-0.18);
        pillar.getChildren().add(base);

        Cylinder shaft = new Cylinder(0.42, height - 0.7);
        shaft.setMaterial(mat);
        shaft.setTranslateY(-height * 0.5);
        pillar.getChildren().add(shaft);

        Box cap = new Box(0.9, 0.35, 0.9);
        cap.setMaterial(mat);
        cap.setTranslateY(-(height - 0.18));
        pillar.getChildren().add(cap);

        return pillar;
    }

    // 3D Enemy: Skeleton Warrior (stands at Y=0, head at Y=-1.7)
    public static Group createSkeletonModel() {
        Group skel = new Group();
        skel.setId("SkeletonModel");

        PhongMaterial boneMat = FXTextureFactory.getColorMaterial(Color.rgb(220, 215, 200));
        PhongMaterial eyeMat = FXTextureFactory.getColorMaterial(Color.rgb(255, 20, 20));
        PhongMaterial rustMat = FXTextureFactory.getMaterial("metal_rust");

        // Legs (floor Y=0 to Y=-0.9)
        Cylinder legL = new Cylinder(0.05, 0.9);
        legL.setMaterial(boneMat);
        legL.setTranslateX(-0.15);
        legL.setTranslateY(-0.45);
        skel.getChildren().add(legL);

        Cylinder legR = new Cylinder(0.05, 0.9);
        legR.setMaterial(boneMat);
        legR.setTranslateX(0.15);
        legR.setTranslateY(-0.45);
        skel.getChildren().add(legR);

        // Ribs / Spine (centered at Y=-1.25)
        Box ribs = new Box(0.42, 0.45, 0.22);
        ribs.setMaterial(boneMat);
        ribs.setTranslateY(-1.25);
        skel.getChildren().add(ribs);

        // Skull (centered at Y=-1.7)
        Box skull = new Box(0.28, 0.3, 0.28);
        skull.setMaterial(boneMat);
        skull.setTranslateY(-1.7);
        skel.getChildren().add(skull);

        // Glowing red eyes
        Sphere eyeL = new Sphere(0.04);
        eyeL.setMaterial(eyeMat);
        eyeL.setTranslateX(-0.08);
        eyeL.setTranslateY(-1.72);
        eyeL.setTranslateZ(-0.15);
        skel.getChildren().add(eyeL);

        Sphere eyeR = new Sphere(0.04);
        eyeR.setMaterial(eyeMat);
        eyeR.setTranslateX(0.08);
        eyeR.setTranslateY(-1.72);
        eyeR.setTranslateZ(-0.15);
        skel.getChildren().add(eyeR);

        // Rusty Sword in Hand
        Box sword = new Box(0.04, 0.7, 0.015);
        sword.setMaterial(rustMat);
        sword.setTranslateX(0.35);
        sword.setTranslateY(-1.1);
        sword.setTranslateZ(-0.25);
        skel.getChildren().add(sword);

        return skel;
    }

    // 3D Enemy: Goblin Skulker (stands at Y=0, head at Y=-1.15)
    public static Group createGoblinModel() {
        Group goblin = new Group();
        goblin.setId("GoblinModel");

        PhongMaterial skinMat = FXTextureFactory.getColorMaterial(Color.rgb(85, 140, 60));
        PhongMaterial eyeMat = FXTextureFactory.getColorMaterial(Color.rgb(255, 230, 20));
        PhongMaterial clubMat = FXTextureFactory.getMaterial("wood_planks");

        // Legs
        Cylinder legL = new Cylinder(0.06, 0.5);
        legL.setMaterial(skinMat);
        legL.setTranslateX(-0.12);
        legL.setTranslateY(-0.25);
        goblin.getChildren().add(legL);

        Cylinder legR = new Cylinder(0.06, 0.5);
        legR.setMaterial(skinMat);
        legR.setTranslateX(0.12);
        legR.setTranslateY(-0.25);
        goblin.getChildren().add(legR);

        // Torso
        Box torso = new Box(0.45, 0.5, 0.35);
        torso.setMaterial(skinMat);
        torso.setTranslateY(-0.7);
        goblin.getChildren().add(torso);

        // Head
        Box head = new Box(0.32, 0.3, 0.3);
        head.setMaterial(skinMat);
        head.setTranslateY(-1.15);
        goblin.getChildren().add(head);

        // Glowing Eyes
        Sphere eyeL = new Sphere(0.04);
        eyeL.setMaterial(eyeMat);
        eyeL.setTranslateX(-0.09);
        eyeL.setTranslateY(-1.16);
        eyeL.setTranslateZ(-0.16);
        goblin.getChildren().add(eyeL);

        Sphere eyeR = new Sphere(0.04);
        eyeR.setMaterial(eyeMat);
        eyeR.setTranslateX(0.09);
        eyeR.setTranslateY(-1.16);
        eyeR.setTranslateZ(-0.16);
        goblin.getChildren().add(eyeR);

        // Wooden Club
        Cylinder club = new Cylinder(0.07, 0.6);
        club.setMaterial(clubMat);
        club.setTranslateX(0.35);
        club.setTranslateY(-0.65);
        club.setTranslateZ(-0.2);
        goblin.getChildren().add(club);

        return goblin;
    }

    // 3D Enemy: Giant Spider
    public static Group createSpiderModel() {
        Group spider = new Group();
        spider.setId("SpiderModel");

        PhongMaterial bodyMat = FXTextureFactory.getColorMaterial(Color.rgb(35, 25, 25));
        PhongMaterial redEye = FXTextureFactory.getColorMaterial(Color.rgb(255, 0, 0));

        // Cephalothorax (head/chest)
        Sphere thorax = new Sphere(0.28);
        thorax.setMaterial(bodyMat);
        thorax.setTranslateY(-0.35);
        spider.getChildren().add(thorax);

        // Abdomen (large bulbous rear)
        Sphere abdomen = new Sphere(0.45);
        abdomen.setMaterial(bodyMat);
        abdomen.setTranslateY(-0.45);
        abdomen.setTranslateZ(0.5);
        spider.getChildren().add(abdomen);

        // 8 Spider Legs
        for (int i = -2; i <= 1; i++) {
            double zOff = i * 0.18 + 0.05;
            // Left leg
            Cylinder legL = new Cylinder(0.025, 0.65);
            legL.setMaterial(bodyMat);
            legL.setTranslateX(-0.45);
            legL.setTranslateY(-0.25);
            legL.setTranslateZ(zOff);
            legL.setRotate(-35);
            spider.getChildren().add(legL);

            // Right leg
            Cylinder legR = new Cylinder(0.025, 0.65);
            legR.setMaterial(bodyMat);
            legR.setTranslateX(0.45);
            legR.setTranslateY(-0.25);
            legR.setTranslateZ(zOff);
            legR.setRotate(35);
            spider.getChildren().add(legR);
        }

        // Multiple red glowing eyes
        for (int e = -2; e <= 2; e++) {
            Sphere eye = new Sphere(0.025);
            eye.setMaterial(redEye);
            eye.setTranslateX(e * 0.05);
            eye.setTranslateY(-0.38);
            eye.setTranslateZ(-0.27);
            spider.getChildren().add(eye);
        }

        return spider;
    }

    // 3D Enemy: Dungeon Guard
    public static Group createGuardModel() {
        Group guard = new Group();
        guard.setId("GuardModel");

        PhongMaterial leatherMat = FXTextureFactory.getColorMaterial(Color.rgb(100, 65, 40));
        PhongMaterial ironMat = FXTextureFactory.getMaterial("metal_rust");
        PhongMaterial woodMat = FXTextureFactory.getMaterial("wood_planks");

        // Legs
        Cylinder legL = new Cylinder(0.08, 0.9);
        legL.setMaterial(leatherMat);
        legL.setTranslateX(-0.18);
        legL.setTranslateY(-0.45);
        guard.getChildren().add(legL);

        Cylinder legR = new Cylinder(0.08, 0.9);
        legR.setMaterial(leatherMat);
        legR.setTranslateX(0.18);
        legR.setTranslateY(-0.45);
        guard.getChildren().add(legR);

        // Torso & Brigandine
        Box torso = new Box(0.55, 0.65, 0.35);
        torso.setMaterial(leatherMat);
        torso.setTranslateY(-1.2);
        guard.getChildren().add(torso);

        // Iron Helmet
        Box helm = new Box(0.36, 0.38, 0.36);
        helm.setMaterial(ironMat);
        helm.setTranslateY(-1.75);
        guard.getChildren().add(helm);

        // Halberd Weapon
        Cylinder pole = new Cylinder(0.03, 2.2);
        pole.setMaterial(woodMat);
        pole.setTranslateX(0.45);
        pole.setTranslateY(-1.1);
        pole.setTranslateZ(-0.25);
        guard.getChildren().add(pole);

        Box axeHead = new Box(0.35, 0.25, 0.03);
        axeHead.setMaterial(ironMat);
        axeHead.setTranslateX(0.55);
        axeHead.setTranslateY(-2.0);
        axeHead.setTranslateZ(-0.25);
        guard.getChildren().add(axeHead);

        return guard;
    }

    // 3D Enemy: Armored Knight (stands at Y=0, helm at Y=-1.75)
    public static Group createArmoredKnightModel() {
        Group knight = new Group();
        knight.setId("KnightModel");

        PhongMaterial steelMat = FXTextureFactory.getMaterial("metal_steel");
        PhongMaterial goldMat = FXTextureFactory.getMaterial("metal_gold");

        // Greaves
        Cylinder legL = new Cylinder(0.09, 0.95);
        legL.setMaterial(steelMat);
        legL.setTranslateX(-0.2);
        legL.setTranslateY(-0.48);
        knight.getChildren().add(legL);

        Cylinder legR = new Cylinder(0.09, 0.95);
        legR.setMaterial(steelMat);
        legR.setTranslateX(0.2);
        legR.setTranslateY(-0.48);
        knight.getChildren().add(legR);

        // Breastplate
        Box chest = new Box(0.6, 0.65, 0.38);
        chest.setMaterial(steelMat);
        chest.setTranslateY(-1.2);
        knight.getChildren().add(chest);

        // Pauldrons
        Sphere pauldronL = new Sphere(0.18);
        pauldronL.setMaterial(goldMat);
        pauldronL.setTranslateX(-0.42);
        pauldronL.setTranslateY(-1.48);
        knight.getChildren().add(pauldronL);

        Sphere pauldronR = new Sphere(0.18);
        pauldronR.setMaterial(goldMat);
        pauldronR.setTranslateX(0.42);
        pauldronR.setTranslateY(-1.48);
        knight.getChildren().add(pauldronR);

        // Greathelm
        Box helm = new Box(0.36, 0.4, 0.36);
        helm.setMaterial(steelMat);
        helm.setTranslateY(-1.75);
        knight.getChildren().add(helm);

        // Knight Broadsword
        Box sword = new Box(0.05, 0.95, 0.02);
        sword.setMaterial(steelMat);
        sword.setTranslateX(0.48);
        sword.setTranslateY(-1.0);
        sword.setTranslateZ(-0.3);
        knight.getChildren().add(sword);

        return knight;
    }

    // 3D Final Boss: The Dark King (stands at Y=0, helm at Y=-1.95, 1.5x scale)
    public static Group createBossModel() {
        Group boss = new Group();
        boss.setId("DarkKingBoss");

        PhongMaterial darkMat = FXTextureFactory.getColorMaterial(Color.rgb(25, 20, 30));
        PhongMaterial goldMat = FXTextureFactory.getMaterial("metal_gold");
        PhongMaterial redGlow = FXTextureFactory.getColorMaterial(Color.rgb(255, 30, 30));

        // Scale up 1.5x
        boss.setScaleX(1.5);
        boss.setScaleY(1.5);
        boss.setScaleZ(1.5);

        // Greaves
        Cylinder legL = new Cylinder(0.12, 1.0);
        legL.setMaterial(darkMat);
        legL.setTranslateX(-0.25);
        legL.setTranslateY(-0.5);
        boss.getChildren().add(legL);

        Cylinder legR = new Cylinder(0.12, 1.0);
        legR.setMaterial(darkMat);
        legR.setTranslateX(0.25);
        legR.setTranslateY(-0.5);
        boss.getChildren().add(legR);

        // Heavy Breastplate
        Box chest = new Box(0.75, 0.85, 0.48);
        chest.setMaterial(darkMat);
        chest.setTranslateY(-1.35);
        boss.getChildren().add(chest);

        // Crown & Helmet
        Box helm = new Box(0.42, 0.45, 0.42);
        helm.setMaterial(darkMat);
        helm.setTranslateY(-1.95);
        boss.getChildren().add(helm);

        // Menacing Spiked Horns (extend into -Y)
        Cylinder hornL = new Cylinder(0.06, 0.5);
        hornL.setMaterial(goldMat);
        hornL.setTranslateX(-0.35);
        hornL.setTranslateY(-2.25);
        hornL.setRotate(25);
        boss.getChildren().add(hornL);

        Cylinder hornR = new Cylinder(0.06, 0.5);
        hornR.setMaterial(goldMat);
        hornR.setTranslateX(0.35);
        hornR.setTranslateY(-2.25);
        hornR.setRotate(-25);
        boss.getChildren().add(hornR);

        // Glowing red visor
        Box visor = new Box(0.25, 0.05, 0.05);
        visor.setMaterial(redGlow);
        visor.setTranslateY(-1.95);
        visor.setTranslateZ(-0.22);
        boss.getChildren().add(visor);

        // Massive Dark Greatsword
        Box sword = new Box(0.08, 1.6, 0.025);
        sword.setMaterial(redGlow);
        sword.setTranslateX(0.65);
        sword.setTranslateY(-1.2);
        sword.setTranslateZ(-0.4);
        boss.getChildren().add(sword);

        return boss;
    }
}
