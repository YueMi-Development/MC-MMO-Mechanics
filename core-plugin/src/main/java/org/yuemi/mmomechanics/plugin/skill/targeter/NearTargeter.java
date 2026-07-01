package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class NearTargeter implements Targeter {
    private final double radius;

    public NearTargeter(double radius) {
        this.radius = radius;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        if (origin.getWorld() == null) {
            return Collections.emptyList();
        }

        Collection<Entity> entities = origin.getWorld().getNearbyEntities(origin, radius, radius, radius);
        
        Entity casterEntity = context.getCaster().getAsEntity();

        return entities.stream()
                .filter(entity -> casterEntity == null || !entity.getUniqueId().equals(casterEntity.getUniqueId()))
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
