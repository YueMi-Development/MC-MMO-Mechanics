package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Sets targeted entities on fire for a specified duration, or extinguishes active fire.
 */
public final class IgniteMechanic implements Mechanic {
    private final int ticks;
    private final boolean extinguish;

    public IgniteMechanic(int ticks, boolean extinguish) {
        this.ticks = ticks;
        this.extinguish = extinguish;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity != null) {
                if (extinguish) {
                    entity.setFireTicks(0);
                } else {
                    entity.setFireTicks(ticks);
                }
            }
        }
    }
}

