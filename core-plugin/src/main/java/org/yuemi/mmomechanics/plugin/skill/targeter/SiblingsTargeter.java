package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.World;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class SiblingsTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (!(casterEntity instanceof Tameable tameableCaster)) {
            return Collections.emptyList();
        }
        AnimalTamer owner = tameableCaster.getOwner();
        if (owner == null) {
            return Collections.emptyList();
        }
        World world = casterEntity.getWorld();
        return world.getLivingEntities().stream()
                .filter(entity -> entity != casterEntity)
                .filter(entity -> entity instanceof Tameable)
                .filter(entity -> {
                    AnimalTamer otherOwner = ((Tameable) entity).getOwner();
                    return otherOwner != null && otherOwner.getUniqueId().equals(owner.getUniqueId());
                })
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
