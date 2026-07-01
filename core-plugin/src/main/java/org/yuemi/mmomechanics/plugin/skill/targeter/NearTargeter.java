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

/**
 * Targets entities within a close proximity to the caster.
 */
public final class NearTargeter implements Targeter {
    private final String radiusExpression;

    public NearTargeter(@NotNull String radiusExpression) {
        this.radiusExpression = radiusExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        if (origin.getWorld() == null) {
            return Collections.emptyList();
        }

        double evaluatedRadius = 5.0;
        org.yuemi.mmomechanics.api.MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(org.yuemi.mmomechanics.api.MmoMechanicsApi.class);
        if (api != null) {
            String parsed = api.parsePlaceholders(context.getCaster(), radiusExpression);
            try {
                evaluatedRadius = Double.parseDouble(parsed);
            } catch (NumberFormatException ignored) {}
        }

        Collection<Entity> entities = origin.getWorld().getNearbyEntities(origin, evaluatedRadius, evaluatedRadius, evaluatedRadius);
        
        Entity casterEntity = context.getCaster().getAsEntity();

        return entities.stream()
                .filter(entity -> casterEntity == null || !entity.getUniqueId().equals(casterEntity.getUniqueId()))
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
