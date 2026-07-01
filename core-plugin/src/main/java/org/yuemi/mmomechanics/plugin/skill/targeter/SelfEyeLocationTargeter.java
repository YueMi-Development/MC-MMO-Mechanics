package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class SelfEyeLocationTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity instanceof LivingEntity living) {
            return Collections.singletonList(new LocationTarget(living.getEyeLocation()));
        }
        return Collections.singletonList(new LocationTarget(context.getCaster().getLocation()));
    }
}
