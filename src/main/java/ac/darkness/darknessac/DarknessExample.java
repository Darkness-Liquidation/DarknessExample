package ac.darkness.darknessac;

import ac.darkness.darknessac.animation.InfernoMode;
import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import org.bukkit.plugin.java.JavaPlugin;

public final class DarknessExample extends JavaPlugin {

    public static final String ANIMATION_NAME = "INFERNO";

    private AnimationMode infernoAnimation;

    @Override
    public void onEnable() {
        this.infernoAnimation = new InfernoMode();
        DarknessAPI.registerAnimation(ANIMATION_NAME, infernoAnimation);
        getLogger().info("Successfully registered " + ANIMATION_NAME + " animation in DarknessAPI!");
    }

    @Override
    public void onDisable() {
        if (infernoAnimation != null) {
            DarknessAPI.unregisterAnimation(ANIMATION_NAME);
        }
    }
}
