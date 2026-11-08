package ac.darkness.darknessac.listener;

import ac.darkness.darknessac.api.events.DarknessPunishEvent;
import ac.darkness.darknessac.api.events.DarknessPunishWaveEvent;
import ac.darkness.darknessac.api.events.DarknessViolationEvent;
import ac.darkness.darknessac.command.DarknessExampleCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("deprecation")
public final class DarknessListener implements Listener {

    private static final String EXEMPT_WORLD = "exempt_world";

    private final Plugin plugin;

    public DarknessListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onViolation(DarknessViolationEvent event) {
        Player player = event.getPlayer();
        if (EXEMPT_WORLD.equalsIgnoreCase(player.getWorld().getName())) {
            event.setCancelled(true);
            return;
        }

        if (event.getViolations() >= 5.0) {
            String alert = DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &c" + player.getName()
                    + " &7failed &c" + event.getFullCheckName()
                    + " &7(x&c" + (int) event.getViolations() + "&7) &c" + event.getVerbose());

            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("darknessexample.staff")) {
                    staff.sendMessage(alert);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPunish(DarknessPunishEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("darkness.bypass.punish")) {
            event.setCancelled(true);
            plugin.getLogger().warning("Cancelled punishment for staff member: " + player.getName());
            return;
        }

        Bukkit.broadcastMessage(DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &cPlayer &6&l"
                + player.getName() + "&r &cwas removed by anticheat for cheating!"));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPunishWave(DarknessPunishWaveEvent event) {
        int queued = event.getPlayerUUIDs().size();
        Bukkit.broadcastMessage(DarknessExampleCommand.color(DarknessExampleCommand.PREFIX + " &c" + queued
                + " &fplayers were punished in ban wave!"));
    }
}
