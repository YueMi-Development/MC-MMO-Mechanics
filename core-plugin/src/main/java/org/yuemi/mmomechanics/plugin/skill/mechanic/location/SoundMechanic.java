package org.yuemi.mmomechanics.plugin.skill.mechanic.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Plays sound effects at locations or stops sounds for targeted players.
 */
public final class SoundMechanic implements Mechanic {
    private final String soundName;
    private final float volume;
    private final float pitch;
    private final boolean stop;

    public SoundMechanic(@NotNull String soundName, float volume, float pitch, boolean stop) {
        this.soundName = soundName.toLowerCase().trim();
        this.volume = volume;
        this.pitch = pitch;
        this.stop = stop;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Location loc = target.getLocation();
            if (stop) {
                if (target.getAsEntity() instanceof Player player) {
                    if (soundName.isEmpty()) {
                        player.stopSound("");
                    } else {
                        player.stopSound(soundName);
                    }
                }
            } else {
                if (!soundName.isEmpty()) {
                    loc.getWorld().playSound(loc, soundName, volume, pitch);
                }
            }
        }
    }
}

