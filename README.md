# 3D Dungeon Crawler - The Dark Descent (JavaFX 3D Edition)

An immersive, fully functional 3D First-Person Dungeon Crawler built **100% in JavaFX 3D**, **Maven**, and standard **Java SE 17+**.

Experience classic dark fantasy dungeon exploration featuring real-time melee combat, first-person weapon and shield viewmodels, interactive chests and hinged doors, dynamic torch lighting with point lights, procedural textures, intelligent enemy AI, an inventory and leveling system, live minimap radar, procedural audio synthesis, and persistent save/load functionality.

---

## 🎮 Key Features

- **100% JavaFX 3D First-Person Perspective**:
  - Full native JavaFX 3D rendering (`SubScene`, `PerspectiveCamera`, `PhongMaterial`, `PointLight`, `AmbientLight`).
  - Smooth mouse pitch and yaw controls with FOV adjustments.
  - Realistic head bobbing and stone footstep sound synchronization.
  - **In-Hand 3D Viewmodel**: Procedural 3D modeled weapons (swords, battleaxes) in right hand and shields in left hand attached directly to the first-person camera.
  - Real-time weapon swing slash animations and shield blocking defense animations.
  - Dynamic camera shake on heavy impacts and boss shockwaves.
  - Raycast targeting reticle / crosshair with context-sensitive interaction prompts.

- **5 Handcrafted Dungeon Stages Cleared Stage-by-Stage**:
  - **Stage 1: The Forgotten Crypt**:
    - Stone crypt corridors, wall torches with dancing point lights.
    - Floor spike traps that trigger when stepped on.
    - Skeleton Warriors patrolling ancient halls.
    - 2 Loot Chests: Entrance Stash (*Steel Longsword*, *Minor Health Potion*) and Vault Chest (*Crypt Gate Key*, *Blood Ruby*, *Greater Health Potion*).
    - Locked Crypt Vault requiring the **Crypt Gate Key** to reach the exit portal.
  - **Stage 2: The Spider & Goblin Caverns**:
    - Subterranean rock cavern with a dark underground lake and stone bridges.
    - Goblin Skulkers and Giant Spider ambushers.
    - 2 Loot Chests: Lakeside Stash (*Shadowfang Dagger*, *Stamina Elixir*) and Cavern Armory (*Battleaxe of Embers*, *Knight's Crest Shield*, *Cavern Key*).
    - Locked Cavern Gateway leading to the lower descent.
  - **Stage 3: The Sunken Catacombs**:
    - Flooded corridors, heavy portcullis gates, and spike traps.
    - Dungeon Guards and Skeleton Warriors.
    - 2 Loot Chests: Barracks Footlocker (*Royal Golden Chalice*, *Greater Health Potion*) and Catacomb Reliquary (*Catacomb Seal Key*, *Ruby Gem*).
  - **Stage 4: The Royal Fortress Armory**:
    - High-vaulted fortress lined with grand stone pillars.
    - Armored Knight patrols and elite guards.
    - 2 High-tier Chests: Commander's Armory (*Dungeon Slayer Greatsword*, *Dragonplate Aegis*) and Royal Vault (*Fortress Key*, *Crown of the Forgotten King*).
  - **Stage 5: The Dark King's Throne & Sanctum**:
    - High imperial throne room lined with glowing molten lava channels and towering pillars.
    - Heavily armored knight protectors.
    - Emperor's Secret Stash (*Ancient Crown*, 350 Gold, *Greater Health Potions*).
    - **Epic Multi-Phase Boss Battle**: **The Dark King**, featuring heavy cleaves, ground slam shockwaves, and enraged combat phase.
    - Unlocks the exit gateway to conquer the dungeon and claim ultimate victory!

- **Stage Cleared Transition System**:
  - Step into the glowing Exit Portal to trigger the **STAGE CLEARED** victory screen!
  - Displays full stage summary: Monsters Slain, Chests Looted, and Gold Acquired.
  - Full HP and Stamina replenishment upon advancing to the next stage!
  - Auto-equips superior weapons and shields looted from chests!

- **Combat System & Enemy AI**:
  - Left Click: Melee weapon attack combo with swing timing, hit detection cone, critical hit chances, and damage reactions.
  - Right Click: Active shield block absorbing incoming damage at the cost of stamina.
  - Living 3D Billboard overhead health bars for all enemies.
  - State-machine AI: Patrol, Alert/Chase, Attack Windup (telegraphed), Melee Strike, Hurt Stagger, and Death Crumble.

- **Player Progression & Inventory**:
  - Experience Points (XP) earned by slaying dungeon monsters.
  - Level Up progression boosting Max HP, Max Stamina, and Base Attack.
  - 16-slot interactive inventory grid (press `[I]`).
  - Weapons: *Rusty Shortsword*, *Steel Longsword*, *Battleaxe of Embers*, *Shadowfang Dagger*, *Ancient Greatsword*.
  - Shields: *Wooden Buckler*, *Knight's Crest Shield*, *Dragonplate Aegis*.
  - Consumables: *Minor Health Potions*, *Greater Health Potions*, *Stamina Elixirs*.
  - Quick-drink hotkey (`[Q]` or `[1]`) for rapid healing in combat.

- **Audio Engineering & Soundtrack**:
  - 44.1kHz 16-bit procedural waveform synthesis: zero external binary bloat, crystal clear latency-free sound.
  - Sound effects: sword swings, metallic clangs, shield impacts, player hurts, footstep thuds, chest open chimes, heavy stone door grinds, potion gulps, level-up fanfares, spike trap triggers, boss roars, and seismic ground slams.
  - Background music themes: Ambient gothic crypt drone, tense cavern rhythm, heart-pounding boss battle drums, and majestic victory fanfare.
  - Master, Music, and SFX volume sliders in the Settings menu.

- **Save & Load System**:
  - Automatic progression saving on level transition.
  - Manual save serialization to `savegame.json` with player stats, current level, gold, and inventory items.
  - "Continue Game" button on Main Menu.

---

## ⌨️ Controls & Keybindings

| Key / Input | Action |
|---|---|
| **W / S** | Move Forward / Backward |
| **A / D** | Strafe Left / Right |
| **Mouse Look** | Rotate Camera (First-Person Look) |
| **Left Click** | Melee Attack / Weapon Slash |
| **Right Click** | Raise Shield to Block (Absorbs Damage) |
| **Spacebar** | Jump |
| **Left Shift** | Sprint (Consumes Stamina) |
| **E** | Interact (Open Doors, Chests, Portals) |
| **I** | Open / Close Inventory & Stats Screen |
| **Q** or **1** | Quick Drink Health Potion |
| **Escape** | Pause Game / Return / Close Menu |

---

## 🚀 How to Build and Run (JavaFX 3D Desktop)

### Option 1: Quick Launch (Windows)
Double-click `run-game.bat` in the project root:
```cmd
run-game.bat
```

### Option 2: Run via Maven
```cmd
mvn javafx:run
```
*(Or use the included helper script `run-maven.bat javafx:run`)*

### Option 3: Direct JAR Execution
```cmd
java --module-path target\fx-libs --add-modules javafx.controls,javafx.graphics,javafx.swing -jar target\dungeon-crawler-javafx-1.0.0.jar
```

### Option 4: Compile & Package from Source
```cmd
mvn package -DskipTests
```
This builds `target/dungeon-crawler-javafx-1.0.0.jar` and copies all JavaFX modular runtime dependencies to `target/fx-libs/`.

---

## 🌐 Instant Web Edition (Play in Any Browser)

A complete **3D Web Edition** is also provided in the [`web/`](file:///c:/Users/aitha/OneDrive/Desktop/AIML1/web/index.html) folder:

### 1. Play Locally in Your Browser
- **Direct Link**: Click and open [`web/index.html`](file:///c:/Users/aitha/OneDrive/Desktop/AIML1/web/index.html) in Chrome, Edge, Firefox, or Safari.
- **Local Server**: Double-click [`start-web-game.bat`](file:///c:/Users/aitha/OneDrive/Desktop/AIML1/start-web-game.bat) to host the game on:
  ```
  http://localhost:8080/index.html
  ```

### 2. Share Online (For Anyone in the World to Click and Play)
- **Netlify Drop**:
  1. Open [app.netlify.com/drop](https://app.netlify.com/drop).
  2. Drag and drop the `web/` folder into the browser window.
  3. Netlify immediately generates a public, shareable URL.

---

## 🛠️ JavaFX 3D Architecture

```
dungeon-crawler-javafx/
├── pom.xml                                     # Maven project configuration (JavaFX 21 + Gson)
├── run-game.bat                                # Windows one-click game launcher
├── run-maven.bat                               # Bundled Maven helper
├── settings.json                               # User settings persistence
├── savegame.json                               # Saved progression file
└── src/
    ├── main/
    │   ├── java/com/dungeoncrawler/
    │   │   ├── MainGameFX.java                 # JavaFX Application, AnimationTimer loop (60 FPS), scene coordination
    │   │   ├── LauncherFX.java                 # Non-Application bootstrap launcher
    │   │   ├── GameState.java                  # App state enum (MENU, PLAYING, PAUSED, etc.)
    │   │   ├── audio/
    │   │   │   ├── AudioManager.java           # Audio playback, pooling & volume control
    │   │   │   ├── SoundType.java              # Enum of all 23 SFX and BGM tracks
    │   │   │   └── SoundSynthesizer.java       # Procedural 44.1kHz PCM waveform generator
    │   │   ├── entity/
    │   │   │   └── EnemyType.java              # Skeleton, Goblin, Guard, Knight, Boss stats
    │   │   ├── javafx/
    │   │   │   ├── FXPlayer.java               # First-person player, PerspectiveCamera, viewmodel, head bob
    │   │   │   ├── FXMeshFactory.java          # Procedural 3D composite meshes (swords, shields, fixtures, enemies)
    │   │   │   ├── FXTextureFactory.java       # Procedural diffuse & normal-style textures for PhongMaterial
    │   │   │   ├── FXEnemy.java                # State-machine AI enemy with 3D overhead health bar
    │   │   │   ├── FXBossEnemy.java            # Multi-phase Dark King boss
    │   │   │   ├── FXCombatSystem.java         # Raycast/cone hit detection, damage math, crits, camera shake
    │   │   │   ├── FXDungeonLevel.java         # 3D level node graph & grid collision detection
    │   │   │   ├── FXDungeonGenerator.java     # Procedural generator for Levels 1, 2, and 3
    │   │   │   ├── FXLevelManager.java         # Level loading, transition, and clearing
    │   │   │   ├── FXDoor.java                 # Interactive 3D hinged door with rotation animation
    │   │   │   ├── FXChest.java                # Interactive 3D treasure chest with lid opening animation
    │   │   │   ├── FXTrap.java                 # Floor spike trap with proximity trigger
    │   │   │   └── FXUIManager.java            # 2D HUD (Health, Stamina, Minimap, Menus, Inventory, Settings)
    │   │   ├── model/
    │   │   │   ├── Item.java                   # Weapons, shields, consumables, keys, treasures
    │   │   │   ├── Inventory.java              # 16-slot inventory, equipment slots
    │   │   │   └── PlayerStats.java            # Level, XP, HP, Stamina, Gold, damage calculations
    │   │   └── save/
    │   │       ├── GameSettings.java           # Settings model (volumes, sensitivity, FOV)
    │   │       ├── SaveData.java               # Serialized player & level state
    │   │       └── SaveManager.java            # JSON save/load manager
    └── test/
        └── java/com/dungeoncrawler/
            └── GameSystemTest.java             # Automated verification test suite
```

---

## 🧪 Automated Verification Test Suite

Run the built-in system verification tests anytime to validate all game subsystems:
```cmd
java -cp "target/test-classes;target/classes;target/fx-libs/*" com.dungeoncrawler.GameSystemTest
```
Tested Systems:
1. Item Registry & catalog properties.
2. Player stats, stamina regeneration, and level-up curves.
3. Inventory slots, weapon/shield equipping, and potion usage.
4. Procedural audio waveform synthesis for all 23 sound effects and musical tracks.
5. JSON persistence and savegame serialization.
6. User settings saving and reloading.
