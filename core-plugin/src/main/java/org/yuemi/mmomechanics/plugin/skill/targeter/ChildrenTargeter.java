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

public final class ChildrenTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity == null) {
            return Collections.emptyList();
        }
        World world = casterEntity.getWorld();
        return world.getLivingEntities().stream()
                .filter(entity -> entity instanceof Tameable)
                .filter(entity -> {
                    AnimalTamer owner = ((Tameable) entity).getOwner();
                    return owner != null && owner.getUniqueId().equals(casterEntity.getUniqueId());
                })
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
