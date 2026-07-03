package org.yuemi.mmomechanics.api.skill.executor;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Handles executing a sequence of mechanics on targets within a SkillContext.
 */
public interface SkillExecutor {
    /**
     * Executes the list of mechanics sequentially.
     *
     * @param context the context of the skill execution
     * @param initialTargets the initial targets for the execution
     * @param mechanics the collection of mechanics to run
     */
    void run(
            @NotNull SkillContext context,
            @NotNull Collection<Target> initialTargets,
            @NotNull Collection<MechanicWrapper> mechanics
    );
}
