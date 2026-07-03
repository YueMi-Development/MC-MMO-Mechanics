package org.yuemi.mmomechanics.api.skill.targeter;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Resolves targets (entities or locations) based on a skill execution context.
 */
public interface Targeter {
    /**
     * Resolves the targets for the given skill context.
     *
     * @param context the context of the skill execution
     * @return a collection of resolved targets
     */
    @NotNull Collection<Target> getTargets(@NotNull SkillContext context);
}
