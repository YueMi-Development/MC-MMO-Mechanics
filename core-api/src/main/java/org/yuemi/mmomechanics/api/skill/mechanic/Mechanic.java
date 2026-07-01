package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

public interface Mechanic {
    void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets);
}
