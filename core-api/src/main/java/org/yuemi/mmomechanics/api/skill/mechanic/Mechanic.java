package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Represents a single gameplay action or effect performed on targets.
 */
public interface Mechanic {
    /**
     * Executes this mechanic on the provided collection of targets.
     *
     * @param context the context of the skill execution
     * @param targets the targets to execute this action on
     */
    void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets);
}
