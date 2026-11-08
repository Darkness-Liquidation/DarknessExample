# DarknessExample

Official example Paper/Purpur/Folia plugin demonstrating full integration with [DarknessAPI](https://github.com/Darkness-Liquidation/DarknessAPI).

## Features Demonstrated

### 1. Custom Punishment Animation (`InfernoMode`)
- Implements `AnimationMode` from `ac.darkness.darknessac.api.punishment`.
- Sends levitation packets and displays converging flame/smoke rings around the target using PacketEvents.
- Automatically registered via `DarknessAPI.registerAnimation("INFERNO", animation)` and unregistered on plugin disable.

### 2. Bukkit Event Handling (`DarknessListener`)
- **`DarknessViolationEvent`**: Intercepts check flags, prints detailed telemetry (check, subtype, VL, verbose), and demonstrates canceling false positives in designated arena worlds.
- **`DarknessPunishEvent`**: Intercepts punishment commands before execution, allowing inspection or modification of punishment reasons.
- **`DarknessPunishWaveEvent`**: Listens for batch ban-waves and processes the collection of queued player UUIDs.

### 3. API & Player Management (`DarknessExampleCommand`)
Provides the `/darknessexample` (or `/dexample`) administrative command:
- `/dexample bypass <player> [seconds]` — Grants temporary check immunity via `DarknessUser#setTempBypass(seconds)`.
- `/dexample info <player>` — Displays aggregate violations, category violations, trust level, and active check data.
- `/dexample flag <player> <check> [type]` — Triggers a manual check flag via `DarknessUser#flag(...)`.
- `/dexample punish <player> [reason]` — Executes the anticheat punishment workflow via `DarknessUser#punish(...)` or `DarknessAPI.INSTANCE.punish(...)`.
- `/dexample alert <message>` — Broadcasts a staff alert via `DarknessAPI.INSTANCE.getAlertManager().sendAlert(message)`.

---

## Requirements

- Java 21 toolchain (runs on Java 17+)
- Paper / Purpur / Folia server (1.20.6+)
- [Darkness Anticheat](https://github.com/Darkness-Liquidation)
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
