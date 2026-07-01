package org.yuemi.mmomechanics.api.skill.condition;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

public interface Condition {
    boolean test(@NotNull SkillContext context, @NotNull Target target);
}
