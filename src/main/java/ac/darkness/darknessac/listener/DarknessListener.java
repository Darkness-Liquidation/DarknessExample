package ac.darkness.darknessac.listener;

import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessEventHandler;
import ac.darkness.darknessac.api.events.DarknessCommandExecuteEvent;
import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import ac.darkness.darknessac.api.events.DarknessPunishWaveEvent;
import ac.darkness.darknessac.api.events.DarknessReloadEvent;
import ac.darkness.darknessac.api.events.DarknessSetbackEvent;
import ac.darkness.darknessac.api.events.DarknessTrustChangeEvent;
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import ac.darkness.darknessac.command.DarknessExampleCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class DarknessListener {

    private static final String EXEMPT_WORLD = "exempt_world";

    private final Plugin plugin;

    public DarknessListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        Player player = event.getPlayer();
        if (player != null && EXEMPT_WORLD.equalsIgnoreCase(player.getWorld().getName())) {
            event.setCancelled(true);
            return;
        }

        if (event.getViolations() >= 5.0) {
            String name = player != null ? player.getName() : event.getUser().getName();
            String alert = DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &c" + name
                    + " &7failed &c" + event.getFullCheckName()
                    + " &7(x&c" + (int) event.getViolations() + "&7) &c" + event.getVerbose());

            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("darknessexample.staff")) {
                    staff.sendMessage(alert);
                }
            }
        }
    }

    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onSetback(DarknessSetbackEvent event) {
        Player player = event.getPlayer();
        if (player != null && EXEMPT_WORLD.equalsIgnoreCase(player.getWorld().getName())) {
            event.setCancelled(true);
            return;
        }

        // Example: cancel setback in parkour / minigame worlds so player isn't rolled back
        if (player != null && "parkour".equalsIgnoreCase(player.getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @DarknessEventHandler(priority = 100, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true);
            plugin.getLogger().warning("Cancelled punishment for staff member: " + player.getName());
            return;
        }

        String name = player != null ? player.getName() : event.getUser().getName();
        Bukkit.broadcastMessage(DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &cPlayer &6&l"
                + name + "&r &cwas removed by Darkness Liquidation for cheating!"));
    }

    @DarknessEventHandler(priority = 0, ignoreCancelled = true)
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queued = event.getPlayerUUIDs().size();
        int online = event.getOnlinePlayers().size();
        Bukkit.broadcastMessage(DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &c" + queued
                + " &fplayers queued (" + online + " online) in ban wave!"));
    }

    @DarknessEventHandler
    public void onTrustChange(DarknessTrustChangeEvent event) {
        if (event.getNewLevel() == TrustLevel.RED) {
            String alert = DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &cStaff Alert: &f"
                    + event.getUsername() + " &7trust factor dropped to &cRED &8(&7" + event.getTrustScore() + "%&8)");
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("darknessexample.staff")) {
                    staff.sendMessage(alert);
                }
            }
        }
    }

    @DarknessEventHandler
    public void onCommandExecute(DarknessCommandExecuteEvent event) {
        if (event.getCommand().startsWith("say ")) {
            plugin.getLogger().info("Intercepted Darkness command: " + event.getCommand());
        }
    }

    @DarknessEventHandler
    public void onReload(DarknessReloadEvent event) {
        plugin.getLogger().info("Darkness reloaded (success=" + event.isSuccess()
                + ", took=" + event.getDurationMillis() + "ms)");
    }
}
