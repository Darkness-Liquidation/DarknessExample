# DarknessExample

Example Bukkit/Paper plugin demonstrating custom punishment animations using the [DarknessAPI](https://github.com/Darkness-Liquidation/DarknessAPI).

## Overview

This example registers a custom `INFERNO` punishment animation into Darkness Anticheat:
- **`InfernoMode`**: Replicates the converging flame and smoke particle animation from core Darkness using PacketEvents.
- **`DarknessExample`**: Registers the animation on enable and unregisters on disable via `DarknessAPI`.

## How to Test

1. Build the plugin jar:
   ```bash
   ./gradlew build
   ```
2. Put `DarknessExample-1.0.jar` into your server's `plugins/` directory alongside `Darkness` and `packetevents`.
3. In `Darkness`'s `config.yml`, set the punish animation:
   ```yaml
   punish_command:
     animation: "INFERNO"
   ```
4. Run in-game or via console:
   ```bash
   /darkness punish <player> [reason]
   ```
