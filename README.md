# DarknessExample

Official example Paper/Purpur/Folia plugin demonstrating full integration with [DarknessAPI](https://github.com/Darkness-Liquidation/DarknessAPI).

## Features Demonstrated

### 1. Custom Punishment Animation (`InfernoMode`)
- Implements `AnimationMode` from `ac.darkness.darknessac.api.punishment`.
- Sends levitation packets and displays converging flame/smoke rings around the target using PacketEvents.
- Automatically registered via `DarknessAPI.registerAnimation("INFERNO", animation)` and unregistered on plugin disable.

### 2. EventBus Event Handling (`DarknessListener`)
- Registered via `DarknessAPI.INSTANCE.getEventBus().registerAnnotatedListeners(this, new DarknessListener(this))` with `@DarknessEventHandler`.
- **`DarknessViolationEvent`**: Intercepts check flags, prints detailed telemetry (check, subtype, VL, verbose), and demonstrates canceling false positives in designated arena worlds.
- **`DarknessSetbackEvent`**: Intercepts setback rollback actions, demonstrating how to cancel rollback teleports in minigame/parkour worlds while keeping violation logs intact.
- **`DarknessPunishEvent`**: Intercepts punishment commands before execution, allowing inspection or cancellation for immune staff.
- **`DarknessPunishWaveEvent`**: Listens for batch ban-waves, providing both queued player UUIDs (`getPlayerUUIDs()`) and currently online players (`getOnlinePlayers()`).
- **`DarknessTrustChangeEvent`**: Listens for real-time changes in player trust factor (e.g. degrading to RED) to immediately notify online staff.
- **`DarknessCommandExecuteEvent`**: Intercepts anticheat command executions, allowing command modification or cancellation.
- **`DarknessReloadEvent`**: Listens for configuration reload completions with success status and duration (`getDurationMillis()`).

### 3. API & Player Management (`DarknessExampleCommand`)
Provides the `/darknessexample` (or `/dexample`) administrative command:
- `/dexample bypass <player> [seconds] [category/check]` — Grants temporary check immunity globally, per-category (`movement`), per-check-type (`speed`), or for a specific individual check (`SpeedA`) via `BypassManager` or `DarknessUser`.
- `/dexample info <player>` — Displays aggregate violations, category violations, trust level, and active check data.
- `/dexample flag <player> <check> [type]` — Triggers a manual check flag via `DarknessUser#flag(...)`.
- `/dexample punish <player> [reason]` — Executes the anticheat punishment workflow via `DarknessAPI.getPunishmentManager().punish(...)` or `DarknessUser#punish(...)`.
- `/dexample alert <message>` — Broadcasts a staff alert via `DarknessAPI.INSTANCE.getAlertManager().sendAlert(message)`.

---

## Requirements

- Java 21 toolchain (runs on Java 17+)
- Paper / Purpur / Folia server (1.20.6+)
- [Darkness Liquidation](https://github.com/Darkness-Liquidation)
- [PacketEvents](https://github.com/retrooper/packetevents)

---

## Building

```bash
./gradlew build
```

The compiled plugin jar will be located in `build/libs/DarknessExample-1.0.jar`.

---

## Configuration & Testing

1. Put `DarknessExample-1.0.jar` into your server's `plugins/` directory.
2. Ensure `Darkness` and `PacketEvents` are installed and running.
3. To use the custom `INFERNO` animation, configure `config.yml` of Darkness:
   ```yaml
   punish_command:
     animation: "INFERNO"
   ```
4. Test the animation:
   ```bash
   /darkness punish <player> [reason]
   ```
5. Test API commands:
   ```bash
   /dexample bypass <player> 15
   /dexample info <player>
   /dexample alert Test broadcast from DarknessExample
   ```
