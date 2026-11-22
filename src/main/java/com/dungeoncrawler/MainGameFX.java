package com.dungeoncrawler;

import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.javafx.*;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import com.dungeoncrawler.save.GameSettings;
import com.dungeoncrawler.save.SaveData;
import com.dungeoncrawler.save.SaveManager;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main application for the 100% JavaFX 3D Dungeon Crawler.
 */
public class MainGameFX extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private GameSettings settings;
    private GameState currentState = GameState.MAIN_MENU;

    private Group root3D;
    private SubScene subScene3D;
    private PerspectiveCamera camera;

    private FXPlayer player;
    private FXCombatSystem combatSystem;
    private FXLevelManager levelManager;
    private FXUIManager uiManager;

    private PlayerStats stats;
    private Inventory inventory;

    private boolean keyW = false;
    private boolean keyS = false;
    private boolean keyA = false;
    private boolean keyD = false;

    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        settings = GameSettings.load();
        AudioManager.getInstance().init(settings);

        stats = new PlayerStats();
        inventory = new Inventory();

        // Setup 3D Scene
        root3D = new Group();
        camera = new PerspectiveCamera(true);
        player = new FXPlayer(camera, stats, inventory);
        root3D.getChildren().add(player.getPlayerGroup());

        // Global 3D Lighting (warm fantasy ambient + directional overhead)
        AmbientLight ambLight = new AmbientLight(Color.rgb(65, 55, 75));
        DirectionalLight dirLight = new DirectionalLight(Color.rgb(110, 100, 125));
        dirLight.setTranslateX(10);
        dirLight.setTranslateY(-20); // overhead in -Y
        dirLight.setTranslateZ(15);
        root3D.getChildren().addAll(ambLight, dirLight);

        subScene3D = new SubScene(root3D, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        subScene3D.setCamera(camera);

        combatSystem = new FXCombatSystem(player, stats, inventory);
        levelManager = new FXLevelManager(root3D, player, stats, inventory);

        // UI Manager
        uiManager = new FXUIManager(settings,
                this::startNewGame,
                this::continueGame,
                this::resumeGame,
                this::restartCurrentLevel,
                this::proceedToNextStage,
                this::returnToMainMenu);

        StackPane masterRoot = new StackPane(subScene3D, uiManager.getRootOverlay());
        Scene mainScene = new Scene(masterRoot, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        mainScene.setFill(Color.rgb(8, 7, 10));

        initInputHandling(mainScene);

        primaryStage.setTitle("3D Dungeon Crawler: The Dark Descent (JavaFX Edition)");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        changeState(GameState.MAIN_MENU);
        AudioManager.getInstance().playMusic(SoundType.BGM_MENU);

        // Start Game Loop (60 FPS)
        AnimationTimer loop = new AnimationTimer() {
            private long lastNano = 0;

            @Override
            public void handle(long now) {
                if (lastNano == 0) {
                    lastNano = now;
                    return;
                }
                double dt = Math.min((now - lastNano) / 1e9, 0.1);
                lastNano = now;

                update(dt);
            }
        };
        loop.start();
    }

    private void initInputHandling(Scene scene) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (currentState == GameState.PLAYING) {
                if (code == KeyCode.W || code == KeyCode.UP) keyW = true;
                if (code == KeyCode.S || code == KeyCode.DOWN) keyS = true;
                if (code == KeyCode.A || code == KeyCode.LEFT) keyA = true;
                if (code == KeyCode.D || code == KeyCode.RIGHT) keyD = true;
                if (code == KeyCode.SHIFT) player.setSprinting(true);
                if (code == KeyCode.E) {
                    if (levelManager.checkLevelExit()) {
                        triggerStageCleared();
                    } else {
                        player.interact();
                    }
                }
                if (code == KeyCode.Q || code == KeyCode.DIGIT1) inventory.useQuickPotion(stats);
                if (code == KeyCode.I) changeState(GameState.INVENTORY);
                if (code == KeyCode.ESCAPE) changeState(GameState.PAUSED);
            } else if (currentState == GameState.STAGE_CLEARED) {
                if (code == KeyCode.E || code == KeyCode.ENTER || code == KeyCode.SPACE) {
                    proceedToNextStage();
                }
            } else if (code == KeyCode.ESCAPE) {
                if (currentState == GameState.PAUSED || currentState == GameState.INVENTORY) {
                    changeState(GameState.PLAYING);
                }
            } else if (code == KeyCode.I && currentState == GameState.INVENTORY) {
                changeState(GameState.PLAYING);
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.W || code == KeyCode.UP) keyW = false;
            if (code == KeyCode.S || code == KeyCode.DOWN) keyS = false;
            if (code == KeyCode.A || code == KeyCode.LEFT) keyA = false;
            if (code == KeyCode.D || code == KeyCode.RIGHT) keyD = false;
            if (code == KeyCode.SHIFT) player.setSprinting(false);
        });

        // Mouse look & attack
        scene.setOnMousePressed(e -> {
            if (currentState == GameState.PLAYING) {
                if (e.getButton() == MouseButton.PRIMARY) {
                    combatSystem.executePlayerAttack(levelManager.getCurrentLevel());
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    player.setBlocking(true);
                }
            }
            lastMouseX = e.getSceneX();
            lastMouseY = e.getSceneY();
        });

        scene.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                player.setBlocking(false);
            }
        });

        scene.setOnMouseMoved(e -> handleMouseLook(e.getSceneX(), e.getSceneY()));
        scene.setOnMouseDragged(e -> handleMouseLook(e.getSceneX(), e.getSceneY()));
    }

    private void handleMouseLook(double mouseX, double mouseY) {
        if (currentState != GameState.PLAYING) {
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return;
        }

        if (lastMouseX >= 0 && lastMouseY >= 0) {
            double dx = mouseX - lastMouseX;
            double dy = mouseY - lastMouseY;
            double sens = settings.getMouseSensitivity() * 0.12;
            player.addLook(dx * sens, -dy * sens);
        }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    private void update(double dt) {
        if (currentState == GameState.PLAYING) {
            if (!stats.isAlive()) {
                AudioManager.getInstance().playMusic(SoundType.BGM_DEFEAT);
                changeState(GameState.GAME_OVER);
                return;
            }

            FXDungeonLevel level = levelManager.getCurrentLevel();
            if (level != null) {
                player.update(dt, keyW, keyS, keyA, keyD, level);
                level.update(dt, player.getPosition(), stats, inventory, player);
                uiManager.update(dt, player, stats, inventory, level);
            }
        }
    }

    public void triggerStageCleared() {
        uiManager.showStageCleared(levelManager.getCurrentLevel().getName(),
                levelManager.getStageKills(),
                levelManager.getStageChests(),
                levelManager.getStageGold());
        changeState(GameState.STAGE_CLEARED);
    }

    public void proceedToNextStage() {
        GameState nextState = levelManager.proceedToNextLevel();
        changeState(nextState);
    }

    public void changeState(GameState newState) {
        this.currentState = newState;
        uiManager.setState(newState);

        if (newState == GameState.PLAYING) {
            lastMouseX = -1;
            lastMouseY = -1;
        }
    }

    public void startNewGame() {
        stats.reset();
        inventory.clear();
        inventory.addItem(Item.getById("rusty_sword"));
        inventory.addItem(Item.getById("wood_buckler"));
        inventory.addItem(Item.getById("minor_health_potion"));
        inventory.addItem(Item.getById("minor_health_potion"));
        inventory.equipWeapon(Item.getById("rusty_sword"));
        inventory.equipShield(Item.getById("wood_buckler"));
        player.refreshEquippedMeshes();

        levelManager.loadLevel(1);
        changeState(GameState.PLAYING);
    }

    public void continueGame() {
        if (!SaveManager.hasSave()) {
            startNewGame();
            return;
        }

        SaveData data = SaveManager.loadGame();
        if (data == null) {
            startNewGame();
            return;
        }

        stats = new PlayerStats();
        stats.setLevel(data.playerLevel);
        stats.setCurrentXP(data.currentXP);
        stats.setCurrentHP(data.currentHP);
        stats.setMaxHP(data.maxHP);
        stats.setCurrentStamina(data.currentStamina);
        stats.setMaxStamina(data.maxStamina);
        stats.setGold(data.gold);
        stats.setEnemiesDefeated(data.enemiesDefeated);
        stats.setChestsOpened(data.chestsOpened);

        inventory = new Inventory();
        inventory.clear();
        for (String itemId : data.inventoryItemIds) {
            inventory.addItem(Item.getById(itemId));
        }

        player.refreshEquippedMeshes();
        levelManager.loadLevel(data.currentLevelIndex);
        changeState(GameState.PLAYING);
    }

    public void resumeGame() {
        changeState(GameState.PLAYING);
    }

    public void restartCurrentLevel() {
        levelManager.restartCurrentLevel();
        changeState(GameState.PLAYING);
    }

    public void returnToMainMenu() {
        if (levelManager.getCurrentLevel() != null) {
            levelManager.getCurrentLevel().cleanup();
        }
        changeState(GameState.MAIN_MENU);
        AudioManager.getInstance().playMusic(SoundType.BGM_MENU);
    }
}
