package org.yuemi.mmomechanics.api.skill.targeter;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

public interface Targeter {
    @NotNull Collection<Target> getTargets(@NotNull SkillContext context);
}
