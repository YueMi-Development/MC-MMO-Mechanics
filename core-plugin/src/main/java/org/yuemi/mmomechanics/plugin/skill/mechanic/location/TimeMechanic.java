package org.yuemi.mmomechanics.plugin.skill.mechanic.location;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Sets absolute or relative world times.
 */
public final class TimeMechanic implements Mechanic {
    private final long timeValue;
    private final boolean relative;

    public TimeMechanic(long timeValue, boolean relative) {
        this.timeValue = timeValue;
        this.relative = relative;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            World world = target.getLocation().getWorld();
            if (world == null) continue;

            if (relative) {
                world.setFullTime(world.getFullTime() + timeValue);
            } else {
                world.setTime(timeValue);
            }
        }
    }
}

