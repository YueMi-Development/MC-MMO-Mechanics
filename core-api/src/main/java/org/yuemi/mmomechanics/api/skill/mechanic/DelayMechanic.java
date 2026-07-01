package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

public final class DelayMechanic implements Mechanic {
    private final String expression;
    private final boolean isSeconds;

    public DelayMechanic(@NotNull String expression, boolean isSeconds) {
        this.expression = expression;
        this.isSeconds = isSeconds;
    }

    public @NotNull String getExpression() {
        return expression;
    }

    public boolean isSeconds() {
        return isSeconds;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        // Handled by the SkillExecutor implementation
    }
}
