package com.dungeoncrawler.javafx;

import java.util.ArrayList;
import java.util.List;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.PlayerStats;
import javafx.geometry.Point3D;
import javafx.scene.Group;

public class FXDungeonLevel {
    public static final double TILE_SIZE = 4.0;

    private final int levelNumber;
    private final String name;
    private final String objective;
    private final Group levelGroup = new Group();

    private final int gridWidth;
    private final int gridHeight;
    private final boolean[][] wallGrid;

    private Point3D playerSpawn = new Point3D(TILE_SIZE * 2, 0, TILE_SIZE * 2);
    private Group exitPortal = null;
    private Point3D exitPortalPos = null;

    private final List<FXDoor> doors = new ArrayList<>();
    private final List<FXChest> chests = new ArrayList<>();
    private final List<FXTrap> traps = new ArrayList<>();
    private final List<FXEnemy> enemies = new ArrayList<>();

    public FXDungeonLevel(int levelNumber, String name, String objective, int gridWidth, int gridHeight) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.objective = objective;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.wallGrid = new boolean[gridWidth][gridHeight];
    }

    public void setWall(int x, int z, boolean isWall) {
        if (x >= 0 && x < gridWidth && z >= 0 && z < gridHeight) {
            wallGrid[x][z] = isWall;
        }
    }

    public boolean isWall(int x, int z) {
        if (x < 0 || x >= gridWidth || z < 0 || z >= gridHeight) return true;
        return wallGrid[x][z];
    }

    public boolean isColliding(double x, double z, double radius) {
        int minX = (int) Math.floor((x - radius) / TILE_SIZE);
        int maxX = (int) Math.floor((x + radius) / TILE_SIZE);
        int minZ = (int) Math.floor((z - radius) / TILE_SIZE);
        int maxZ = (int) Math.floor((z + radius) / TILE_SIZE);

        for (int gx = minX; gx <= maxX; gx++) {
            for (int gz = minZ; gz <= maxZ; gz++) {
                if (isWall(gx, gz)) {
                    double tileMinX = gx * TILE_SIZE;
                    double tileMaxX = tileMinX + TILE_SIZE;
                    double tileMinZ = gz * TILE_SIZE;
                    double tileMaxZ = tileMinZ + TILE_SIZE;

                    if (x + radius > tileMinX && x - radius < tileMaxX &&
                        z + radius > tileMinZ && z - radius < tileMaxZ) {
                        return true;
                    }
                }
            }
        }

        // Closed doors collision
        for (FXDoor door : doors) {
            if (!door.isOpen()) {
                double dx = x - door.getPosition().getX();
                double dz = z - door.getPosition().getZ();
                if (dx * dx + dz * dz < 1.5 * 1.5) {
                    return true;
                }
            }
        }

        return false;
    }

    public void update(double dt, Point3D playerPos, PlayerStats stats, Inventory inv, FXPlayer player) {
        for (FXDoor door : doors) door.update(dt);
        for (FXChest chest : chests) chest.update(dt);
        for (FXTrap trap : traps) trap.update(dt, playerPos, stats);
        for (FXEnemy enemy : enemies) enemy.update(dt, playerPos, stats, inv, player);
    }

    public void addDoor(FXDoor d) {
        doors.add(d);
        levelGroup.getChildren().add(d.getSpatial());
    }

    public void addChest(FXChest c) {
        chests.add(c);
        levelGroup.getChildren().add(c.getSpatial());
    }

    public void addTrap(FXTrap t) {
        traps.add(t);
        levelGroup.getChildren().add(t.getSpatial());
    }

    public void addEnemy(FXEnemy e) {
        enemies.add(e);
        levelGroup.getChildren().add(e.getSpatial());
    }

    public void setExitPortal(Group portal, Point3D pos) {
        this.exitPortal = portal;
        this.exitPortalPos = pos;
        portal.setTranslateX(pos.getX());
        portal.setTranslateY(pos.getY());
        portal.setTranslateZ(pos.getZ());
        levelGroup.getChildren().add(portal);
    }

    public void cleanup() {
        levelGroup.getChildren().clear();
        doors.clear();
        chests.clear();
        traps.clear();
        enemies.clear();
    }

    public int getLevelNumber() { return levelNumber; }
    public String getName() { return name; }
    public String getObjective() { return objective; }
    public Group getLevelGroup() { return levelGroup; }
    public Point3D getPlayerSpawn() { return playerSpawn; }
    public void setPlayerSpawn(Point3D p) { this.playerSpawn = p; }
    public Group getExitPortal() { return exitPortal; }
    public Point3D getExitPortalPos() { return exitPortalPos; }
    public Point3D getExitPosition() { return exitPortalPos; }
    public List<FXDoor> getDoors() { return doors; }
    public List<FXChest> getChests() { return chests; }
    public List<FXTrap> getTraps() { return traps; }
    public List<FXEnemy> getEnemies() { return enemies; }
    public int getGridWidth() { return gridWidth; }
    public int getGridHeight() { return gridHeight; }
    public int getWidth() { return gridWidth; }
    public int getHeight() { return gridHeight; }
}
