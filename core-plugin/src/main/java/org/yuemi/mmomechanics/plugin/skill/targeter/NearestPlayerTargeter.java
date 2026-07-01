package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Targets the single nearest player in a radius around the caster.
 */
public final class NearestPlayerTargeter implements Targeter {

    private final String radiusExpression;

    public NearestPlayerTargeter(@NotNull String radiusExpression) {
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
            String parsed = api.parsePlaceholders(context.getCaster(), radiusExpression);
            try {
                radius = Double.parseDouble(parsed);
            } catch (NumberFormatException ignored) {}
        }

        double finalRadius = radius;
        return origin.getWorld().getNearbyEntities(origin, finalRadius, finalRadius, finalRadius).stream()
                .filter(entity -> entity instanceof Player)
                .filter(entity -> entity != context.getCaster().getAsEntity())
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(origin)))
                .map(entity -> (Target) new EntityTarget(entity))
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }
}
