package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the location of the caster's current target.
 */
public final class TargetLocationTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        Entity target = null;
        if (casterEntity instanceof Mob mob) {
            target = mob.getTarget();
        } else if (casterEntity instanceof LivingEntity living) {
            target = living.getTargetEntity(50);
        }
        if (target != null) {
            return Collections.singletonList(new LocationTarget(target.getLocation()));
        }
        return Collections.emptyList();
    }
}
