package org.yuemi.mmomechanics.api.skill.executor;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

public interface SkillExecutor {
    void run(
            @NotNull SkillContext context,
            @NotNull Collection<Target> initialTargets,
            @NotNull Collection<MechanicWrapper> mechanics
    );
}
