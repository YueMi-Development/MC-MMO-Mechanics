package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Targets non-living entities (like items or armor stands) within a radius around the origin.
 */
public final class NotLivingNearOriginTargeter implements Targeter {

    private final String radiusExpression;

    public NotLivingNearOriginTargeter(@NotNull String radiusExpression) {
        this.radiusExpression = radiusExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        if (origin.getWorld() == null) {
            return Collections.emptyList();
        }

        double radius = 5.0;
        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                radius = Double.parseDouble(api.parsePlaceholders(context.getCaster(), radiusExpression));
            } catch (NumberFormatException ignored) {}
        }

        return origin.getWorld().getNearbyEntities(origin, radius, radius, radius).stream()
                .filter(entity -> !(entity instanceof LivingEntity))
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
