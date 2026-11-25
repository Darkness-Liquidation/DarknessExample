package ac.darkness.darknessac;

import ac.darkness.darknessac.animation.InfernoMode;
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.command.DarknessExampleCommand;
import ac.darkness.darknessac.listener.DarknessListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DarknessExample extends JavaPlugin {

    public static final String ANIMATION_NAME = "INFERNO";

    @Override
    public void onEnable() {
        if (!DarknessAPI.isInitialized()) {
            getLogger().severe("Darkness API is not initialized! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        DarknessAPI.registerAnimation(ANIMATION_NAME, new InfernoMode());
        DarknessAPI.INSTANCE.getEventBus().registerAnnotatedListeners(this, new DarknessListener(this));

        PluginCommand cmd = getCommand("darknessexample");
        if (cmd != null) {
            DarknessExampleCommand executor = new DarknessExampleCommand();
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }

        getLogger().info("DarknessExample enabled successfully (Darkness v" + DarknessAPI.INSTANCE.getDarknessVersion() + ")");
    }

    @Override
    public void onDisable() {
        DarknessAPI.unregisterAnimation(ANIMATION_NAME);
    }
}
