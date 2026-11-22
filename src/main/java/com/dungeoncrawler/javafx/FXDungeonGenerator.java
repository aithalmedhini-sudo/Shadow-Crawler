package com.dungeoncrawler.javafx;

import com.dungeoncrawler.entity.EnemyType;
import com.dungeoncrawler.model.Item;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class FXDungeonGenerator {

    public static final int TOTAL_STAGES = 5;

    public static FXDungeonLevel buildLevel(int levelNum) {
        switch (levelNum) {
            case 1: return buildStage1();
            case 2: return buildStage2();
            case 3: return buildStage3();
            case 4: return buildStage4();
            case 5: return buildStage5();
            default: return buildStage1();
        }
    }

    // ==========================================
    // STAGE 1: THE FORGOTTEN CRYPT
    // ==========================================
    private static FXDungeonLevel buildStage1() {
        int w = 18, h = 18;
        FXDungeonLevel lvl = new FXDungeonLevel(1, "Stage 1: The Forgotten Crypt",
                "Explore the ancient crypt, slay the skeleton warriors, loot the chests for the Crypt Key, and breach the Vault Gate.", w, h);

        String[] map = {
            "##################",
            "#P...#.......#...#",
            "#.L..#..S....#...#",
            "#....#.......#...#",
            "#....D.......#...#",
            "###.##########...#",
            "#....#......T#...#",
            "#..L.#..S....#...#",
            "#....#.......#...#",
            "#.C..#.......#...#",
            "######...D########",
            "#........#.......#",
            "#..T.....#...L...#",
            "#..S.....#...S...#",
            "#........#...X...#",
            "#........#.......#",
            "##################",
            "##################"
        };

        buildFromMap(map, lvl, "crypt_wall", "stone_floor", "ceiling");

        // Chest 1: Crypt Entrance Stash at (2, 4)
        Point3D chest1Pos = getTileCenter(2, 4);
        Group c1Spatial = FXMeshFactory.createChest();
        c1Spatial.setTranslateX(chest1Pos.getX());
        c1Spatial.setTranslateZ(chest1Pos.getZ());
        FXChest cryptStash = new FXChest(c1Spatial, chest1Pos, 65,
                Item.getById("steel_longsword"),
                Item.getById("minor_health_potion"));
        lvl.addChest(cryptStash);

        // Chest 2: Vault Chest with Crypt Key at (2, 9)
        Point3D chest2Pos = getTileCenter(2, 9);
        Group c2Spatial = FXMeshFactory.createChest();
        c2Spatial.setTranslateX(chest2Pos.getX());
        c2Spatial.setTranslateZ(chest2Pos.getZ());
        FXChest vaultChest = new FXChest(c2Spatial, chest2Pos, 110,
                Item.getById("crypt_key"),
                Item.getById("greater_health_potion"),
                Item.getById("ruby_gem"));
        lvl.addChest(vaultChest);

        // Vault Gate requiring crypt_key
        Point3D doorPos = getTileCenter(9, 10);
        Group dSpatial = FXMeshFactory.createDoor("crypt_wall");
        dSpatial.setTranslateX(doorPos.getX());
        dSpatial.setTranslateZ(doorPos.getZ());
        FXDoor vaultDoor = new FXDoor(dSpatial, doorPos, true, "crypt_key", "Crypt Vault Gate");
        lvl.addDoor(vaultDoor);

        // Normal passage door
        Point3D door1Pos = getTileCenter(5, 4);
        Group d1Spatial = FXMeshFactory.createDoor("crypt_wall");
        d1Spatial.setTranslateX(door1Pos.getX());
        d1Spatial.setTranslateZ(door1Pos.getZ());
        FXDoor normalDoor = new FXDoor(d1Spatial, door1Pos, false, null, "Crypt Passage Door");
        lvl.addDoor(normalDoor);

        return lvl;
    }

    // ==========================================
    // STAGE 2: THE SPIDER & GOBLIN CAVERNS
    // ==========================================
    private static FXDungeonLevel buildStage2() {
        int w = 20, h = 20;
        FXDungeonLevel lvl = new FXDungeonLevel(2, "Stage 2: The Spider & Goblin Caverns",
                "Cross the dark lake bridges, clear out goblin ambushers and giant spiders, and recover the Cavern Key.", w, h);

        String[] map = {
            "####################",
            "#P...#.............#",
            "#....#..G....C.....#",
            "#..L.#.............#",
            "###.########.#######",
            "#........#.........#",
            "#...W....#....L....#",
            "#...W....D.........#",
            "#...W....#....G....#",
            "#...B....#.........#",
            "#...W....#####.#####",
            "#...W....#...T.....#",
            "#........#...K.....#",
            "#..L.....#.........#",
            "#...G....D...S.....#",
            "#........#.........#",
            "##########..X......#",
            "#..................#",
            "#..................#",
            "####################"
        };

        buildFromMap(map, lvl, "cavern_wall", "flagstone", "ceiling");

        // Water and Bridge visuals
        for (int z = 6; z <= 12; z++) {
            for (int x = 4; x <= 6; x++) {
                Point3D center = getTileCenter(x, z);
                Box tile = new Box(FXDungeonLevel.TILE_SIZE, 0.2, FXDungeonLevel.TILE_SIZE);
                if (z == 9) {
                    tile.setMaterial(FXTextureFactory.getMaterial("wood_planks"));
                    tile.setTranslateY(0.05); // Wood bridge sits on water
                } else {
                    tile.setMaterial(FXTextureFactory.getMaterial("water"));
                    tile.setTranslateY(0.15); // Water surface
                }
                tile.setTranslateX(center.getX());
                tile.setTranslateZ(center.getZ());
                lvl.getLevelGroup().getChildren().add(tile);
            }
        }

        // Chest 1: Lakeside Stash at (13, 2)
        Point3D chest1Pos = getTileCenter(13, 2);
        Group c1Spatial = FXMeshFactory.createChest();
        c1Spatial.setTranslateX(chest1Pos.getX());
        c1Spatial.setTranslateZ(chest1Pos.getZ());
        FXChest c1 = new FXChest(c1Spatial, chest1Pos, 90,
                Item.getById("shadow_dagger"),
                Item.getById("stamina_potion"));
        lvl.addChest(c1);

        // Chest 2: Cavern Armory Cache with Battleaxe & Cavern Key at (17, 12)
        Point3D chest2Pos = getTileCenter(17, 12);
        Group c2Spatial = FXMeshFactory.createChest();
        c2Spatial.setTranslateX(chest2Pos.getX());
        c2Spatial.setTranslateZ(chest2Pos.getZ());
        FXChest c2 = new FXChest(c2Spatial, chest2Pos, 150,
                Item.getById("flaming_axe"),
                Item.getById("knight_shield"),
                Item.getById("cavern_key"),
                Item.getById("greater_health_potion"));
        lvl.addChest(c2);

        // Cavern Gateway Door
        Point3D gatePos = getTileCenter(9, 7);
        Group gSpatial = FXMeshFactory.createDoor("cavern_wall");
        gSpatial.setTranslateX(gatePos.getX());
        gSpatial.setTranslateZ(gatePos.getZ());
        FXDoor gate = new FXDoor(gSpatial, gatePos, true, "cavern_key", "Cavern Gateway");
        lvl.addDoor(gate);

        // Extra Spider Ambushers
        Point3D sp1 = getTileCenter(8, 12);
        Group spider1Spatial = FXMeshFactory.createSpiderModel();
        FXEnemy spider1 = new FXEnemy(EnemyType.GIANT_SPIDER, spider1Spatial, sp1,
                new Point3D(sp1.getX() + 2, 0, sp1.getZ() + 2));
        lvl.addEnemy(spider1);

        Point3D sp2 = getTileCenter(16, 8);
        Group spider2Spatial = FXMeshFactory.createSpiderModel();
        FXEnemy spider2 = new FXEnemy(EnemyType.GIANT_SPIDER, spider2Spatial, sp2,
                new Point3D(sp2.getX() - 2, 0, sp2.getZ() + 3));
        lvl.addEnemy(spider2);

        return lvl;
    }

    // ==========================================
    // STAGE 3: THE SUNKEN CATACOMBS
    // ==========================================
    private static FXDungeonLevel buildStage3() {
        int w = 20, h = 20;
        FXDungeonLevel lvl = new FXDungeonLevel(3, "Stage 3: The Sunken Catacombs",
                "Navigate the flooded corridors, defeat the dungeon guards and skeleton elites, and recover the Catacomb Seal Key.", w, h);

        String[] map = {
            "####################",
            "#P...#.......#.....#",
            "#..L.#...S...#..C..#",
            "#....#.......#.....#",
            "###.###.###.####.###",
            "#........#.........#",
            "#..G.....#....L....#",
            "#........D...G.....#",
            "######.#####.#######",
            "#....#.......#.....#",
            "#....#...T...#..S..#",
            "#..L.#.......#.....#",
            "###.###.###.####.###",
            "#........#.........#",
            "#..G.....#...T.K...#",
            "#........#.........#",
            "######...D...#######",
            "#........#.....X...#",
            "#...C....#...L.....#",
            "####################"
        };

        buildFromMap(map, lvl, "crypt_wall", "stone_floor", "ceiling");

        // Chest 1: Barracks Footlocker at (16, 2)
        Point3D c1Pos = getTileCenter(16, 2);
        Group c1Spatial = FXMeshFactory.createChest();
        c1Spatial.setTranslateX(c1Pos.getX());
        c1Spatial.setTranslateZ(c1Pos.getZ());
        FXChest c1 = new FXChest(c1Spatial, c1Pos, 140,
                Item.getById("greater_health_potion"),
                Item.getById("stamina_potion"),
                Item.getById("gold_chalice"));
        lvl.addChest(c1);

        // Chest 2: Catacomb Reliquary with Catacomb Key at (4, 18)
        Point3D c2Pos = getTileCenter(4, 18);
        Group c2Spatial = FXMeshFactory.createChest();
        c2Spatial.setTranslateX(c2Pos.getX());
        c2Spatial.setTranslateZ(c2Pos.getZ());
        FXChest c2 = new FXChest(c2Spatial, c2Pos, 180,
                Item.getById("catacomb_key"),
                Item.getById("greater_health_potion"),
                Item.getById("ruby_gem"));
        lvl.addChest(c2);

        // Catacomb Gateway Door
        Point3D dPos = getTileCenter(9, 16);
        Group dSpatial = FXMeshFactory.createDoor("crypt_wall");
        dSpatial.setTranslateX(dPos.getX());
        dSpatial.setTranslateZ(dPos.getZ());
        FXDoor gate = new FXDoor(dSpatial, dPos, true, "catacomb_key", "Catacomb Heavy Portcullis");
        lvl.addDoor(gate);

        // Extra Guard
        Point3D gPos = getTileCenter(14, 10);
        Group gSpatial = FXMeshFactory.createGuardModel();
        FXEnemy guard = new FXEnemy(EnemyType.DUNGEON_GUARD, gSpatial, gPos,
                new Point3D(gPos.getX() + 3, 0, gPos.getZ() + 2));
        lvl.addEnemy(guard);

        return lvl;
    }

    // ==========================================
    // STAGE 4: THE ROYAL FORTRESS ARMORY
    // ==========================================
    private static FXDungeonLevel buildStage4() {
        int w = 22, h = 22;
        FXDungeonLevel lvl = new FXDungeonLevel(4, "Stage 4: The Royal Fortress Armory",
                "Infiltrate the high fortress armory, defeat the armored knight patrols, and claim the legendary Dungeon Slayer and Fortress Key.", w, h);

        String[] map = {
            "######################",
            "#P...#.......#.......#",
            "#..L.#...K...#...C...#",
            "#....#.......#.......#",
            "###.###.###.###.######",
            "#........#...........#",
            "#...L....#.....L.....#",
            "#........D...K.......#",
            "######.#####.#########",
            "#....#.......#.......#",
            "#..T.#...T...#...T...#",
            "#....#.......#.......#",
            "###.###.###.###.######",
            "#........#...........#",
            "#...K....#.....K.....#",
            "#........D...........#",
            "######.#####.#########",
            "#........#.......#...#",
            "#...C....#...K...#.X.#",
            "#........#.......#...#",
            "#..L.....#...L...#...#",
            "######################"
        };

        buildFromMap(map, lvl, "fortress_wall", "flagstone", "ceiling");

        // Chest 1: Commander's Armory Chest at (17, 2)
        Point3D c1Pos = getTileCenter(17, 2);
        Group c1Spatial = FXMeshFactory.createChest();
        c1Spatial.setTranslateX(c1Pos.getX());
        c1Spatial.setTranslateZ(c1Pos.getZ());
        FXChest c1 = new FXChest(c1Spatial, c1Pos, 220,
                Item.getById("dungeon_slayer"),
                Item.getById("dragon_shield"),
                Item.getById("greater_health_potion"));
        lvl.addChest(c1);

        // Chest 2: Royal Vault Chest with Fortress Key at (4, 18)
        Point3D c2Pos = getTileCenter(4, 18);
        Group c2Spatial = FXMeshFactory.createChest();
        c2Spatial.setTranslateX(c2Pos.getX());
        c2Spatial.setTranslateZ(c2Pos.getZ());
        FXChest c2 = new FXChest(c2Spatial, c2Pos, 260,
                Item.getById("fortress_key"),
                Item.getById("ancient_crown"),
                Item.getById("stamina_potion"));
        lvl.addChest(c2);

        // Fortress Portcullis
        Point3D gatePos = getTileCenter(9, 15);
        Group gSpatial = FXMeshFactory.createDoor("fortress_wall");
        gSpatial.setTranslateX(gatePos.getX());
        gSpatial.setTranslateZ(gatePos.getZ());
        FXDoor gate = new FXDoor(gSpatial, gatePos, true, "fortress_key", "Royal Fortress Portcullis");
        lvl.addDoor(gate);

        // 4 Grand Stone Pillars
        Point3D[] pillars = {
            getTileCenter(4, 6), getTileCenter(17, 6),
            getTileCenter(4, 13), getTileCenter(17, 13)
        };
        for (Point3D p : pillars) {
            Group pillar = FXMeshFactory.createPillar("fortress_wall", 4.0);
            pillar.setTranslateX(p.getX());
            pillar.setTranslateZ(p.getZ());
            lvl.getLevelGroup().getChildren().add(pillar);
        }

        return lvl;
    }

    // ==========================================
    // STAGE 5: THE DARK KING'S THRONE & SANCTUM (FINAL BOSS)
    // ==========================================
    private static FXDungeonLevel buildStage5() {
        int w = 24, h = 24;
        FXDungeonLevel lvl = new FXDungeonLevel(5, "Stage 5: The Dark King's Throne & Sanctum",
                "Vanquish the Royal Elite Guards and slay THE DARK KING to escape the dungeon and achieve ultimate victory!", w, h);

        String[] map = {
            "########################",
            "#P...#.................#",
            "#....#.................#",
            "#..L.#.................#",
            "###.####################",
            "#......................#",
            "#..M...M.........M...M.#",
            "#..M...M.........M...M.#",
            "#......................#",
            "#..........K...........#",
            "#......................#",
            "#......L.........L.....#",
            "#......................#",
            "#......................#",
            "#..........K...........#",
            "#......................#",
            "#..M...M.........M...M.#",
            "#..M...M.........M...M.#",
            "#......................#",
            "#############.##########",
            "#......................#",
            "#..C...........X...L...#",
            "#......................#",
            "########################"
        };

        buildFromMap(map, lvl, "fortress_wall", "flagstone", "ceiling");

        // Glowing Molten Lava Channels ('M')
        for (int z = 6; z <= 17; z++) {
            if ((z >= 6 && z <= 7) || (z >= 16 && z <= 17)) {
                for (int x : new int[]{3, 7, 17, 21}) {
                    Point3D center = getTileCenter(x, z);
                    Box lava = new Box(FXDungeonLevel.TILE_SIZE, 0.2, FXDungeonLevel.TILE_SIZE);
                    lava.setMaterial(FXTextureFactory.getMaterial("lava"));
                    lava.setTranslateX(center.getX());
                    lava.setTranslateY(0.12);
                    lava.setTranslateZ(center.getZ());
                    lvl.getLevelGroup().getChildren().add(lava);

                    PointLight lavaGlow = new PointLight(Color.rgb(255, 60, 20));
                    lavaGlow.setTranslateX(center.getX());
                    lavaGlow.setTranslateY(-1.2);
                    lavaGlow.setTranslateZ(center.getZ());
                    lvl.getLevelGroup().getChildren().add(lavaGlow);
                }
            }
        }

        // Royal Vault Chest with Emperor's Treasures at (3, 21)
        Point3D armoryPos = getTileCenter(3, 21);
        Group armorySpatial = FXMeshFactory.createChest();
        armorySpatial.setTranslateX(armoryPos.getX());
        armorySpatial.setTranslateZ(armoryPos.getZ());
        FXChest armory = new FXChest(armorySpatial, armoryPos, 350,
                Item.getById("ancient_crown"),
                Item.getById("greater_health_potion"),
                Item.getById("stamina_potion"));
        lvl.addChest(armory);

        // Throne Gate requiring boss_key
        Point3D throneDoorPos = getTileCenter(13, 19);
        Group tSpatial = FXMeshFactory.createDoor("fortress_wall");
        tSpatial.setTranslateX(throneDoorPos.getX());
        tSpatial.setTranslateZ(throneDoorPos.getZ());
        FXDoor throneDoor = new FXDoor(tSpatial, throneDoorPos, true, "boss_key", "Sanctum Escape Gate");
        lvl.addDoor(throneDoor);

        // 4 Grand Pillars in Throne Hall
        Point3D[] pillars = {
            getTileCenter(10, 8), getTileCenter(16, 8),
            getTileCenter(10, 14), getTileCenter(16, 14)
        };
        for (Point3D p : pillars) {
            Group pillar = FXMeshFactory.createPillar("fortress_wall", 4.0);
            pillar.setTranslateX(p.getX());
            pillar.setTranslateZ(p.getZ());
            lvl.getLevelGroup().getChildren().add(pillar);
        }

        // Spawn The Dark King Boss
        Point3D bossPos = getTileCenter(13, 12);
        Group bossSpatial = FXMeshFactory.createBossModel();
        FXBossEnemy darkKing = new FXBossEnemy(bossSpatial, bossPos);
        darkKing.setDropItem(Item.getById("boss_key"));
        lvl.addEnemy(darkKing);

        return lvl;
    }

    // ==========================================
    // MAP PARSER & 3D GEOMETRY BUILDER
    // ==========================================
    private static void buildFromMap(String[] map, FXDungeonLevel lvl, String wallTex, String floorTex, String ceilTex) {
        PhongMaterial wallMat = FXTextureFactory.getMaterial(wallTex);
        PhongMaterial floorMat = FXTextureFactory.getMaterial(floorTex);
        PhongMaterial ceilMat = FXTextureFactory.getMaterial(ceilTex);

        int rows = map.length;
        int cols = map[0].length();

        for (int z = 0; z < rows; z++) {
            for (int x = 0; x < cols; x++) {
                char ch = (x < map[z].length()) ? map[z].charAt(x) : '#';
                Point3D center = getTileCenter(x, z);

                if (ch == '#') {
                    lvl.setWall(x, z, true);
                    // Wall from floor Y=0 up to ceiling Y=-4.0 (height 4.0, centered at Y=-2.0)
                    Box wall = new Box(FXDungeonLevel.TILE_SIZE, 4.0, FXDungeonLevel.TILE_SIZE);
                    wall.setMaterial(wallMat);
                    wall.setTranslateX(center.getX());
                    wall.setTranslateY(-2.0);
                    wall.setTranslateZ(center.getZ());
                    lvl.getLevelGroup().getChildren().add(wall);
                } else {
                    lvl.setWall(x, z, false);

                    // Floor surface at Y=0 (top edge of box centered at Y=0.1)
                    Box floor = new Box(FXDungeonLevel.TILE_SIZE, 0.2, FXDungeonLevel.TILE_SIZE);
                    floor.setMaterial(floorMat);
                    floor.setTranslateX(center.getX());
                    floor.setTranslateY(0.1);
                    floor.setTranslateZ(center.getZ());
                    lvl.getLevelGroup().getChildren().add(floor);

                    // Ceiling surface at Y=-4.0 (bottom edge of box centered at Y=-4.1)
                    Box ceil = new Box(FXDungeonLevel.TILE_SIZE, 0.2, FXDungeonLevel.TILE_SIZE);
                    ceil.setMaterial(ceilMat);
                    ceil.setTranslateX(center.getX());
                    ceil.setTranslateY(-4.1);
                    ceil.setTranslateZ(center.getZ());
                    lvl.getLevelGroup().getChildren().add(ceil);

                    if (ch == 'P') {
                        lvl.setPlayerSpawn(center);
                    } else if (ch == 'L') {
                        // Torch on wall at Y=-2.2
                        Group torch = FXMeshFactory.createTorch();
                        torch.setTranslateX(center.getX());
                        torch.setTranslateY(-2.2);
                        torch.setTranslateZ(center.getZ());
                        lvl.getLevelGroup().getChildren().add(torch);

                        PointLight pLight = new PointLight(Color.rgb(255, 175, 60));
                        pLight.setTranslateX(center.getX());
                        pLight.setTranslateY(-2.4);
                        pLight.setTranslateZ(center.getZ());
                        lvl.getLevelGroup().getChildren().add(pLight);
                    } else if (ch == 'T') {
                        Group tSpatial = FXMeshFactory.createSpikeTrap();
                        tSpatial.setTranslateX(center.getX());
                        tSpatial.setTranslateZ(center.getZ());
                        FXTrap trap = new FXTrap(tSpatial, center);
                        lvl.addTrap(trap);
                    } else if (ch == 'S') {
                        Group skelSpatial = FXMeshFactory.createSkeletonModel();
                        FXEnemy skel = new FXEnemy(EnemyType.SKELETON_WARRIOR, skelSpatial, center,
                                new Point3D(center.getX() + 3, 0, center.getZ() + 3));
                        lvl.addEnemy(skel);
                    } else if (ch == 'G') {
                        Group gSpatial = FXMeshFactory.createGoblinModel();
                        FXEnemy goblin = new FXEnemy(EnemyType.GOBLIN_SKULKER, gSpatial, center,
                                new Point3D(center.getX() + 3, 0, center.getZ() + 2));
                        lvl.addEnemy(goblin);
                    } else if (ch == 'K') {
                        Group kSpatial = FXMeshFactory.createArmoredKnightModel();
                        FXEnemy knight = new FXEnemy(EnemyType.ARMORED_KNIGHT, kSpatial, center,
                                new Point3D(center.getX() + 4, 0, center.getZ() + 3));
                        lvl.addEnemy(knight);
                    } else if (ch == 'X') {
                        Group portal = FXMeshFactory.createExitPortal();
                        lvl.setExitPortal(portal, center);
                    }
                }
            }
        }
    }

    public static Point3D getTileCenter(int x, int z) {
        return new Point3D(x * FXDungeonLevel.TILE_SIZE + FXDungeonLevel.TILE_SIZE * 0.5,
                0,
                z * FXDungeonLevel.TILE_SIZE + FXDungeonLevel.TILE_SIZE * 0.5);
    }
}
