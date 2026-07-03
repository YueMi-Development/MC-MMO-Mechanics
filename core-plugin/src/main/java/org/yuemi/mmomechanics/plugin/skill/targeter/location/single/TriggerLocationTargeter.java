package org.yuemi.mmomechanics.plugin.skill.targeter.location.single;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the location of the entity or event that triggered the skill.
 */
public final class TriggerLocationTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        return context.getTrigger().getTriggerTarget()
                .map(target -> Collections.singletonList((Target) new LocationTarget(target.getLocation())))
                .orElseGet(() -> Collections.singletonList(new LocationTarget(context.getCaster().getLocation())));
    }
}
