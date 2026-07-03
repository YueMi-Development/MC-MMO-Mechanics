package org.yuemi.mmomechanics.api.skill.condition;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

/**
 * Gating check determining if a mechanic should run on a specific target.
 */
public interface Condition {
    /**
     * Tests if the target passes this condition.
     *
     * @param context the context of the skill execution
     * @param target the target to evaluate
     * @return true if the condition passes, false otherwise
     */
    boolean test(@NotNull SkillContext context, @NotNull Target target);
}
