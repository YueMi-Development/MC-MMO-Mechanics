package org.yuemi.mmomechanics.plugin.skill.mechanic.location;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Modifies world weather (Clear, Rain, Storm) and duration.
 */
public final class WeatherMechanic implements Mechanic {
    public enum Type {
        CLEAR,
        RAIN,
        STORM
    }

    private final Type type;
    private final int durationTicks;

    public WeatherMechanic(@NotNull Type type, int durationTicks) {
        this.type = type;
        this.durationTicks = durationTicks;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            World world = target.getLocation().getWorld();
            if (world == null) continue;

            switch (type) {
                case CLEAR -> {
                    world.setStorm(false);
                    world.setThundering(false);
                    world.setClearWeatherDuration(durationTicks);
                }
                case RAIN -> {
                    world.setStorm(true);
                    world.setThundering(false);
                    world.setWeatherDuration(durationTicks);
                }
                case STORM -> {
                    world.setStorm(true);
                    world.setThundering(true);
                    world.setWeatherDuration(durationTicks);
                }
            }
        }
    }
}

