package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the entity that triggered the skill.
 */
public final class TriggerTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        return context.getTrigger().getTriggerTarget()
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }
}
