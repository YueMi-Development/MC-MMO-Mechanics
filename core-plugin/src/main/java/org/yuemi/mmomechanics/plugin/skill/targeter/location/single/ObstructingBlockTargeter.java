package org.yuemi.mmomechanics.plugin.skill.targeter.location.single;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the solid block that is directly blocking the caster's path in front of them.
 */
public final class ObstructingBlockTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity instanceof LivingEntity living) {
            Block block = living.getTargetBlockExact(5);
            if (block != null && !block.isEmpty() && block.getType().isSolid()) {
                return Collections.singletonList(new LocationTarget(block.getLocation()));
            }
        }
        return Collections.emptyList();
    }
}
