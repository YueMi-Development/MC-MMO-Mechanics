package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * A Mechanic representing a execution pause in ticks or seconds.
 */
public final class DelayMechanic implements Mechanic {
    private final String expression;
    private final boolean isSeconds;

    /**
     * Constructs a DelayMechanic.
     *
     * @param expression the mathematical/string expression of the delay amount
     * @param isSeconds true if the delay is in seconds, false if in ticks
     */
    public DelayMechanic(@NotNull String expression, boolean isSeconds) {
        this.expression = expression;
        this.isSeconds = isSeconds;
    }

    /**
     * Gets the parsed delay expression.
     *
     * @return the delay expression
     */
    public @NotNull String getExpression() {
        return expression;
    }

    /**
     * Checks if the delay is represented in seconds.
     *
     * @return true if seconds, false if ticks
     */
    public boolean isSeconds() {
        return isSeconds;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        // Handled by the SkillExecutor implementation
    }
}
