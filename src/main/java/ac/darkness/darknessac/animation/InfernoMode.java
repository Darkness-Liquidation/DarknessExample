package ac.darkness.darknessac.animation;

import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.punishment.ActiveAnimation;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InfernoMode extends AnimationMode {

    private static final double[][] OFFSETS = {
            {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1},
            {1, 0, -1}, {-1, 0, 1},
            {1, 0, 1}, {-1, 0, -1},
            {1, 1, 0}, {-1, -1, 0}, {0, 1, 1}, {0, -1, -1},
            {1, 1, -1}, {-1, -1, 1}, {-1, 1, 1}, {1, -1, -1},
            {1, 1, 1}, {-1, -1, -1},
            {1, -1, 0}, {-1, 1, 0}, {0, -1, 1}, {0, 1, -1},
            {1, -1, 1}, {-1, 1, -1}
    };

    private static final Particle<ParticleData> FLAME_PART = new Particle<>(ParticleTypes.FLAME);
    private static final Particle<ParticleData> SMOKE_PART = new Particle<>(ParticleTypes.SMOKE);
    private static final Vector3f OFFSET = new Vector3f(0.2f, 0.2f, 0.2f);

    public InfernoMode() {
        // 32 ticks duration, onTick every 2 ticks, with levitation enabled (amplifier -1)
        super(32, 2, true, -1);
    }

    @Override
    public void onTick(@NotNull ActiveAnimation anim, int step) {
        List<? extends DarknessUser> rawViewers = anim.getViewers();
        if (rawViewers.isEmpty()) return;

        PlayerManager playerManager = PacketEvents.getAPI().getPlayerManager();
        List<User> viewers = new ArrayList<>(rawViewers.size());
        for (DarknessUser viewer : rawViewers) {
            Player vp = Bukkit.getPlayer(viewer.getUniqueId());
            if (vp != null && vp.isOnline()) {
                User u = playerManager.getUser(vp);
                if (u != null) {
                    viewers.add(u);
                }
            }
        }

        if (viewers.isEmpty()) return;

        double d = 16.0 - step;

        Vector3i soundPos = new Vector3i((int) anim.getX(), (int) anim.getY(), (int) anim.getZ());
        WrapperPlayServerEffect effect = new WrapperPlayServerEffect(2004, soundPos, 0, false);
        for (User u : viewers) {
            u.writePacket(effect);
        }

        for (double[] off : OFFSETS) {
            Vector3d pos = new Vector3d(anim.getX() + off[0] * d, anim.getY() + off[1] * d, anim.getZ() + off[2] * d);
            WrapperPlayServerParticle flame = new WrapperPlayServerParticle(FLAME_PART, true, pos, OFFSET, 0.02f, 8);
            WrapperPlayServerParticle smoke = new WrapperPlayServerParticle(SMOKE_PART, true, pos, OFFSET, 0.02f, 4);

            for (User u : viewers) {
                u.writePacket(flame);
                u.writePacket(smoke);
            }
        }

        for (User u : viewers) {
            u.flushPackets();
        }
    }
}
