package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class CasterTargetTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null) {
                return Collections.singletonList(new EntityTarget(target));
            }
        } else if (casterEntity instanceof LivingEntity living) {
            Entity target = living.getTargetEntity(50);
            if (target != null) {
                return Collections.singletonList(new EntityTarget(target));
            }
        }
        return Collections.emptyList();
    }
}
