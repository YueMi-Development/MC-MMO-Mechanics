package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

public final class DelayMechanic implements Mechanic {
    private final long ticks;

    public DelayMechanic(long ticks) {
        this.ticks = ticks;
    }

    public long getTicks() {
        return ticks;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        // Handled by the SkillExecutor implementation
    }
}
