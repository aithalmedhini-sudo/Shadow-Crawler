package com.dungeoncrawler.javafx;

import com.dungeoncrawler.GameState;
import com.dungeoncrawler.audio.AudioManager;
import com.dungeoncrawler.audio.SoundType;
import com.dungeoncrawler.model.Inventory;
import com.dungeoncrawler.model.Item;
import com.dungeoncrawler.model.PlayerStats;
import com.dungeoncrawler.save.GameSettings;
import com.dungeoncrawler.save.SaveManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class FXUIManager {
    private final StackPane rootOverlay = new StackPane();
    private final Pane hudPane = new Pane();
    private final VBox menuBox = new VBox(14);
    private final VBox pauseBox = new VBox(14);
    private final VBox invBox = new VBox(12);
    private final VBox settingsBox = new VBox(12);
    private final VBox controlsBox = new VBox(12);
    private final VBox stageClearBox = new VBox(14);
    private final VBox gameOverBox = new VBox(14);
    private final VBox victoryBox = new VBox(14);

    // HUD Elements
    private final Label levelLabel = new Label("Lv. 1 - Stage 1: The Forgotten Crypt");
    private final Canvas hpBarCanvas = new Canvas(240, 22);
    private final Canvas staminaBarCanvas = new Canvas(240, 18);
    private final Label goldLabel = new Label("Gold: 0");
    private final Label gearLabel = new Label("Weapon: Steel Longsword | Shield: Buckler");
    private final Label potionLabel = new Label("[Q] Drink Health Potion (x2) | [I] Inventory");
    private final Label interactionLabel = new Label("");
    private final Label toastLabel = new Label("");
    private final Canvas minimapCanvas = new Canvas(140, 140);

    // Boss Bar
    private final VBox bossBarWrap = new VBox(4);
    private final Canvas bossBarCanvas = new Canvas(380, 20);

    // Stage Clear Elements
    private final Label stageClearTitle = new Label("STAGE CLEARED!");
    private final Label stageClearSubtitle = new Label("");
    private final Label stageClearStats = new Label("");

    private final GameSettings settings;
    private final Runnable onStartNewGame;
    private final Runnable onContinueGame;
    private final Runnable onResumeGame;
    private final Runnable onRestartLevel;
    private final Runnable onProceedNextStage;
    private final Runnable onReturnToMenu;

    private double toastTimer = 0;

    public FXUIManager(GameSettings settings, Runnable onStart, Runnable onCont,
                       Runnable onResume, Runnable onRestart, Runnable onProceed, Runnable onMenu) {
        this.settings = settings;
        this.onStartNewGame = onStart;
        this.onContinueGame = onCont;
        this.onResumeGame = onResume;
        this.onRestartLevel = onRestart;
        this.onProceedNextStage = onProceed;
        this.onReturnToMenu = onMenu;

        initHUD();
        initMainMenu();
        initPauseMenu();
        initInventoryMenu();
        initSettingsMenu();
        initControlsMenu();
        initStageClearMenu();
        initGameOverMenu();
        initVictoryMenu();

        rootOverlay.getChildren().addAll(hudPane, menuBox, pauseBox, invBox, settingsBox, controlsBox, stageClearBox, gameOverBox, victoryBox);
        hideAllMenus();
        menuBox.setVisible(true);
    }

    private void initHUD() {
        hudPane.setPickOnBounds(false);

        // Top Left: Player Status
        VBox statusBox = new VBox(6);
        statusBox.setLayoutX(20);
        statusBox.setLayoutY(20);

        levelLabel.setStyle("-fx-text-fill: #f4d06f; -fx-font-size: 15px; -fx-font-weight: bold;");
        goldLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 13px; -fx-font-weight: bold;");
        gearLabel.setStyle("-fx-text-fill: #b0bec5; -fx-font-size: 12px;");
        potionLabel.setStyle("-fx-text-fill: #81c784; -fx-font-size: 12px;");

        statusBox.getChildren().addAll(levelLabel, hpBarCanvas, staminaBarCanvas, goldLabel, gearLabel, potionLabel);
        hudPane.getChildren().add(statusBox);

        // Top Right: Minimap Radar
        VBox mapBox = new VBox(4);
        mapBox.setLayoutX(1280 - 165);
        mapBox.setLayoutY(20);
        Label mapTitle = new Label("DUNGEON MAP");
        mapTitle.setStyle("-fx-text-fill: #90a4ae; -fx-font-size: 11px; -fx-font-weight: bold;");
        mapBox.getChildren().addAll(mapTitle, minimapCanvas);
        hudPane.getChildren().add(mapBox);

        // Top Center: Boss Health Bar
        bossBarWrap.setLayoutX(1280 / 2.0 - 190);
        bossBarWrap.setLayoutY(25);
        bossBarWrap.setAlignment(Pos.CENTER);
        Label bossName = new Label("THE DARK KING - CORRUPTED RULER");
        bossName.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 13px; -fx-font-weight: bold;");
        bossBarWrap.getChildren().addAll(bossName, bossBarCanvas);
        bossBarWrap.setVisible(false);
        hudPane.getChildren().add(bossBarWrap);

        // Center: Targeting Reticle Crosshair
        Circle crosshair = new Circle(3, Color.rgb(255, 255, 255, 0.65));
        crosshair.setLayoutX(1280 / 2.0);
        crosshair.setLayoutY(720 / 2.0);
        hudPane.getChildren().add(crosshair);

        // Interaction Prompt
        interactionLabel.setLayoutX(1280 / 2.0 - 150);
        interactionLabel.setLayoutY(720 / 2.0 + 35);
        interactionLabel.setPrefWidth(300);
        interactionLabel.setAlignment(Pos.CENTER);
        interactionLabel.setStyle("-fx-text-fill: #ffeb3b; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.65); -fx-padding: 6 12; -fx-background-radius: 6;");
        interactionLabel.setVisible(false);
        hudPane.getChildren().add(interactionLabel);

        // Toast Notifications
        toastLabel.setLayoutX(1280 / 2.0 - 250);
        toastLabel.setLayoutY(110);
        toastLabel.setPrefWidth(500);
        toastLabel.setAlignment(Pos.CENTER);
        toastLabel.setStyle("-fx-text-fill: #ffd54f; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: rgba(15,12,20,0.85); -fx-padding: 8 16; -fx-border-color: #ffd54f; -fx-border-width: 1; -fx-background-radius: 8; -fx-border-radius: 8;");
        toastLabel.setVisible(false);
        hudPane.getChildren().add(toastLabel);
    }

    private void initMainMenu() {
        styleMenuBox(menuBox, 460, 480);
        Label title = new Label("THE DARK DESCENT");
        title.setStyle("-fx-text-fill: #e6c229; -fx-font-size: 30px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, #b8860b, 10, 0, 0, 0);");
        Label subtitle = new Label("3D First-Person Dungeon Crawler (JavaFX Edition)");
        subtitle.setStyle("-fx-text-fill: #a69ea1; -fx-font-size: 13px;");

        Button btnNew = createFantasyButton("START NEW GAME", onStartNewGame);
        Button btnContinue = createFantasyButton("CONTINUE GAME", onContinueGame);
        btnContinue.setDisable(!SaveManager.hasSaveGame());

        Button btnSettings = createFantasyButton("SETTINGS", () -> showMenu(settingsBox));
        Button btnControls = createFantasyButton("CONTROLS & HOW TO PLAY", () -> showMenu(controlsBox));
        Button btnExit = createFantasyButton("EXIT TO DESKTOP", () -> System.exit(0));

        menuBox.getChildren().addAll(title, subtitle, btnNew, btnContinue, btnSettings, btnControls, btnExit);
    }

    private void initPauseMenu() {
        styleMenuBox(pauseBox, 380, 360);
        Label title = new Label("GAME PAUSED");
        title.setStyle("-fx-text-fill: #e6c229; -fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnResume = createFantasyButton("RESUME", onResumeGame);
        Button btnRestart = createFantasyButton("RESTART LEVEL", onRestartLevel);
        Button btnSettings = createFantasyButton("SETTINGS", () -> showMenu(settingsBox));
        Button btnMenu = createFantasyButton("MAIN MENU", onReturnToMenu);

        pauseBox.getChildren().addAll(title, btnResume, btnRestart, btnSettings, btnMenu);
    }

    private void initStageClearMenu() {
        styleMenuBox(stageClearBox, 500, 400);

        stageClearTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 32px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, #ff8c00, 15, 0, 0, 0);");
        stageClearSubtitle.setStyle("-fx-text-fill: #81c784; -fx-font-size: 16px; -fx-font-weight: bold;");
        stageClearStats.setStyle("-fx-text-fill: #cfd8dc; -fx-font-size: 14px; -fx-line-spacing: 5px;");
        stageClearStats.setAlignment(Pos.CENTER);

        Button btnProceed = createFantasyButton("PROCEED TO NEXT STAGE [E]", onProceedNextStage);
        btnProceed.setStyle("-fx-background-color: linear-gradient(#43a047, #2e7d32); -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnMenu = createFantasyButton("SAVE & RETURN TO MENU", onReturnToMenu);

        stageClearBox.getChildren().addAll(stageClearTitle, stageClearSubtitle, stageClearStats, btnProceed, btnMenu);
    }

    public void showStageCleared(String stageName, int kills, int chests, int gold) {
        stageClearSubtitle.setText(stageName + " Cleared!");
        stageClearStats.setText(
                "STAGE SUMMARY:\n\n" +
                "Monsters Slayed: " + kills + "\n" +
                "Chests Looted: " + chests + "\n" +
                "Gold Acquired: +" + gold + " Gold\n\n" +
                "Health & Stamina have been fully restored for the descent!"
        );
        AudioManager.getInstance().playSFX(SoundType.LEVEL_UP);
    }

    private void initInventoryMenu() {
        styleMenuBox(invBox, 560, 460);
        Label title = new Label("INVENTORY & EQUIPMENT");
        title.setStyle("-fx-text-fill: #e6c229; -fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 16; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(70, 70);
            slot.setStyle("-fx-background-color: #1e1b24; -fx-border-color: #3e384d; -fx-border-width: 1.5; -fx-border-radius: 4;");
            Label slotLabel = new Label("Slot " + (i + 1));
            slotLabel.setStyle("-fx-text-fill: #5c556b; -fx-font-size: 10px;");
            slot.getChildren().add(slotLabel);
            grid.add(slot, i % 4, i / 4);
        }

        Button btnClose = createFantasyButton("CLOSE INVENTORY [I]", onResumeGame);
        invBox.getChildren().addAll(title, grid, btnClose);
    }

    private void initSettingsMenu() {
        styleMenuBox(settingsBox, 440, 420);
        Label title = new Label("SETTINGS");
        title.setStyle("-fx-text-fill: #e6c229; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label musicLbl = new Label("Music Volume: " + (int)(settings.getMusicVolume() * 100) + "%");
        musicLbl.setStyle("-fx-text-fill: #cfd8dc;");
        Slider musicSlider = new Slider(0, 1, settings.getMusicVolume());
        musicSlider.valueProperty().addListener((obs, oldV, newV) -> {
            settings.setMusicVolume(newV.floatValue());
            musicLbl.setText("Music Volume: " + (int)(newV.floatValue() * 100) + "%");
            AudioManager.getInstance().setMusicVolume(newV.floatValue());
        });

        Label sfxLbl = new Label("SFX Volume: " + (int)(settings.getSfxVolume() * 100) + "%");
        sfxLbl.setStyle("-fx-text-fill: #cfd8dc;");
        Slider sfxSlider = new Slider(0, 1, settings.getSfxVolume());
        sfxSlider.valueProperty().addListener((obs, oldV, newV) -> {
            settings.setSfxVolume(newV.floatValue());
            sfxLbl.setText("SFX Volume: " + (int)(newV.floatValue() * 100) + "%");
            AudioManager.getInstance().setSfxVolume(newV.floatValue());
        });

        Label sensLbl = new Label("Mouse Sensitivity: " + (int)settings.getMouseSensitivity());
        sensLbl.setStyle("-fx-text-fill: #cfd8dc;");
        Slider sensSlider = new Slider(1, 10, settings.getMouseSensitivity());
        sensSlider.valueProperty().addListener((obs, oldV, newV) -> {
            settings.setMouseSensitivity(newV.floatValue());
            sensLbl.setText("Mouse Sensitivity: " + newV.intValue());
        });

        Button btnBack = createFantasyButton("BACK", () -> {
            settings.save();
            showMenu(menuBox);
        });

        settingsBox.getChildren().addAll(title, musicLbl, musicSlider, sfxLbl, sfxSlider, sensLbl, sensSlider, btnBack);
    }

    private void initControlsMenu() {
        styleMenuBox(controlsBox, 480, 440);
        Label title = new Label("CONTROLS & INSTRUCTIONS");
        title.setStyle("-fx-text-fill: #e6c229; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label body = new Label(
                "• W / A / S / D - Move / Strafe\n" +
                "• Mouse Look - Aim and look around (First-Person)\n" +
                "• Left Click - Attack / Weapon Slash\n" +
                "• Right Click - Block with Shield\n" +
                "• Left Shift - Sprint (Uses stamina)\n" +
                "• Spacebar - Jump\n" +
                "• E - Interact (Open Doors, Loot Chests, Enter Portals)\n" +
                "• I - Open Inventory\n" +
                "• Q / 1 - Quick Drink Health Potion\n" +
                "• ESC - Pause / Back\n\n" +
                "Clear each stage by looting chests for keys and descending down the glowing exit portal!"
        );
        body.setStyle("-fx-text-fill: #cfd8dc; -fx-font-size: 13px; -fx-line-spacing: 3px;");

        Button btnBack = createFantasyButton("BACK", () -> showMenu(menuBox));
        controlsBox.getChildren().addAll(title, body, btnBack);
    }

    private void initGameOverMenu() {
        styleMenuBox(gameOverBox, 440, 320);
        Label title = new Label("YOU HAVE FALLEN");
        title.setStyle("-fx-text-fill: #ff3333; -fx-font-size: 26px; -fx-font-weight: bold;");
        Label sub = new Label("The dungeon depths have claimed your life.");
        sub.setStyle("-fx-text-fill: #a69ea1; -fx-font-size: 13px;");

        Button btnRestart = createFantasyButton("RESTART LEVEL", onRestartLevel);
        Button btnMenu = createFantasyButton("MAIN MENU", onReturnToMenu);

        gameOverBox.getChildren().addAll(title, sub, btnRestart, btnMenu);
    }

    private void initVictoryMenu() {
        styleMenuBox(victoryBox, 500, 360);
        Label title = new Label("DUNGEON CONQUERED!");
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 26px; -fx-font-weight: bold;");
        Label sub = new Label("The Dark King has fallen and you have conquered all 5 stages of the dungeon!");
        sub.setStyle("-fx-text-fill: #81c784; -fx-font-size: 14px;");

        Button btnAgain = createFantasyButton("PLAY AGAIN", onStartNewGame);
        Button btnMenu = createFantasyButton("MAIN MENU", onReturnToMenu);

        victoryBox.getChildren().addAll(title, sub, btnAgain, btnMenu);
    }

    public void update(double dt, FXPlayer player, PlayerStats stats, Inventory inv, FXDungeonLevel level) {
        // Draw HP Bar
        GraphicsContext hpGc = hpBarCanvas.getGraphicsContext2D();
        hpGc.setFill(Color.rgb(20, 20, 24));
        hpGc.fillRect(0, 0, 240, 22);
        double hpRatio = Math.max(0, stats.getCurrentHP() / stats.getMaxHP());
        hpGc.setFill(Color.rgb(210, 35, 35));
        hpGc.fillRect(2, 2, 236 * hpRatio, 18);
        hpGc.setFill(Color.WHITE);
        hpGc.fillText("HP: " + (int)stats.getCurrentHP() + " / " + (int)stats.getMaxHP(), 80, 16);

        // Draw Stamina Bar
        GraphicsContext sGc = staminaBarCanvas.getGraphicsContext2D();
        sGc.setFill(Color.rgb(20, 20, 24));
        sGc.fillRect(0, 0, 240, 18);
        double sRatio = Math.max(0, stats.getCurrentStamina() / stats.getMaxStamina());
        sGc.setFill(Color.rgb(35, 180, 55));
        sGc.fillRect(2, 2, 236 * sRatio, 14);
        sGc.setFill(Color.WHITE);
        sGc.fillText("Stamina: " + (int)stats.getCurrentStamina() + " / " + (int)stats.getMaxStamina(), 75, 14);

        // Text labels
        levelLabel.setText("Lv. " + stats.getLevel() + " - " + level.getName());
        goldLabel.setText("Gold: " + stats.getGold() + " | Foes Vanquished: " + stats.getEnemiesDefeated());
        Item weapon = inv.getEquippedWeapon();
        Item shield = inv.getEquippedShield();
        gearLabel.setText("Weapon: " + (weapon != null ? weapon.getName() : "Fists") + " | Shield: " + (shield != null ? shield.getName() : "None"));
        potionLabel.setText("[Q] Drink Health Potion (" + inv.getPotionCount() + " remaining) | [I] Inventory");

        // Interaction prompt
        String prompt = player.getInteractionPrompt();
        if (prompt != null && !prompt.isEmpty()) {
            interactionLabel.setText(prompt);
            interactionLabel.setVisible(true);
        } else {
            interactionLabel.setVisible(false);
        }

        // Notification / Loot Toast
        if (stats.getNotification() != null && !stats.getNotification().isEmpty()) {
            toastLabel.setText(stats.getNotification());
            toastLabel.setVisible(true);
            toastTimer = 3.5;
            stats.clearNotification();
        } else if (toastTimer > 0) {
            toastTimer -= dt;
            if (toastTimer <= 0) toastLabel.setVisible(false);
        }

        // Boss Bar (Stage 5)
        boolean hasBoss = false;
        for (FXEnemy e : level.getEnemies()) {
            if (e instanceof FXBossEnemy && e.isAlive()) {
                hasBoss = true;
                bossBarWrap.setVisible(true);
                GraphicsContext bGc = bossBarCanvas.getGraphicsContext2D();
                bGc.setFill(Color.rgb(25, 20, 25));
                bGc.fillRect(0, 0, 380, 20);
                double bRatio = Math.max(0, e.getHealth() / e.getMaxHealth());
                bGc.setFill(Color.rgb(180, 20, 30));
                bGc.fillRect(2, 2, 376 * bRatio, 16);
                bGc.setFill(Color.WHITE);
                bGc.fillText((int)e.getHealth() + " / " + (int)e.getMaxHealth(), 160, 15);
                break;
            }
        }
        if (!hasBoss) bossBarWrap.setVisible(false);

        // Render Minimap Radar
        renderMinimap(player, level);
    }

    private void renderMinimap(FXPlayer player, FXDungeonLevel level) {
        GraphicsContext gc = minimapCanvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(15, 12, 18, 0.85));
        gc.fillRect(0, 0, 140, 140);
        gc.setStroke(Color.rgb(60, 50, 70));
        gc.strokeRect(0, 0, 140, 140);

        int cols = level.getWidth();
        int rows = level.getHeight();
        double cw = 140.0 / cols;
        double ch = 140.0 / rows;

        // Walls
        gc.setFill(Color.rgb(65, 55, 75));
        for (int z = 0; z < rows; z++) {
            for (int x = 0; x < cols; x++) {
                if (level.isWall(x, z)) {
                    gc.fillRect(x * cw, z * ch, cw + 0.5, ch + 0.5);
                }
            }
        }

        // Chests
        gc.setFill(Color.GOLD);
        for (FXChest c : level.getChests()) {
            if (!c.isOpened()) {
                double cx = (c.getPosition().getX() / FXDungeonLevel.TILE_SIZE) * cw;
                double cz = (c.getPosition().getZ() / FXDungeonLevel.TILE_SIZE) * ch;
                gc.fillRect(cx - 1.5, cz - 1.5, 3, 3);
            }
        }

        // Enemies
        gc.setFill(Color.RED);
        for (FXEnemy e : level.getEnemies()) {
            if (e.isAlive()) {
                double ex = (e.getPosition().getX() / FXDungeonLevel.TILE_SIZE) * cw;
                double ez = (e.getPosition().getZ() / FXDungeonLevel.TILE_SIZE) * ch;
                gc.fillRect(ex - 1.5, ez - 1.5, 3, 3);
            }
        }

        // Player Dot
        gc.setFill(Color.CYAN);
        double px = (player.getPosition().getX() / FXDungeonLevel.TILE_SIZE) * cw;
        double pz = (player.getPosition().getZ() / FXDungeonLevel.TILE_SIZE) * ch;
        gc.fillOval(px - 3, pz - 3, 6, 6);
    }

    public void setState(GameState state) {
        hideAllMenus();
        switch (state) {
            case MAIN_MENU:
                hudPane.setVisible(false);
                menuBox.setVisible(true);
                break;
            case PLAYING:
                hudPane.setVisible(true);
                break;
            case PAUSED:
                hudPane.setVisible(true);
                pauseBox.setVisible(true);
                break;
            case INVENTORY:
                hudPane.setVisible(true);
                invBox.setVisible(true);
                break;
            case SETTINGS:
                settingsBox.setVisible(true);
                break;
            case CONTROLS:
                controlsBox.setVisible(true);
                break;
            case STAGE_CLEARED:
                hudPane.setVisible(true);
                stageClearBox.setVisible(true);
                break;
            case GAME_OVER:
                hudPane.setVisible(false);
                gameOverBox.setVisible(true);
                break;
            case VICTORY:
                hudPane.setVisible(false);
                victoryBox.setVisible(true);
                break;
        }
    }

    private void showMenu(VBox target) {
        hideAllMenus();
        target.setVisible(true);
    }

    private void hideAllMenus() {
        menuBox.setVisible(false);
        pauseBox.setVisible(false);
        invBox.setVisible(false);
        settingsBox.setVisible(false);
        controlsBox.setVisible(false);
        stageClearBox.setVisible(false);
        gameOverBox.setVisible(false);
        victoryBox.setVisible(false);
    }

    private void styleMenuBox(VBox box, double width, double height) {
        box.setMaxSize(width, height);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: rgba(16, 14, 22, 0.94); -fx-border-color: #5d4037; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 20, 0, 0, 0);");
    }

    private Button createFantasyButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefWidth(280);
        btn.setStyle("-fx-background-color: linear-gradient(#3a3245, #221e29); -fx-text-fill: #e0d8c3; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #6d5b45; -fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6; -fx-cursor: hand; -fx-padding: 8 16;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: linear-gradient(#5a4c6e, #3a3245); -fx-text-fill: #ffeb3b; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #ffd700; -fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6; -fx-cursor: hand; -fx-padding: 8 16;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: linear-gradient(#3a3245, #221e29); -fx-text-fill: #e0d8c3; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #6d5b45; -fx-border-width: 1; -fx-background-radius: 6; -fx-border-radius: 6; -fx-cursor: hand; -fx-padding: 8 16;"));
        btn.setOnAction(e -> {
            AudioManager.getInstance().playSFX(SoundType.BUTTON_CLICK);
            if (action != null) action.run();
        });
        return btn;
    }

    public StackPane getRootOverlay() { return rootOverlay; }
}
