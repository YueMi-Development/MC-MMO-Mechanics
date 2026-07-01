package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
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

public final class EntitiesInRingTargeter implements Targeter {

    private final String minRadiusExpression;
    private final String maxRadiusExpression;

    public EntitiesInRingTargeter(@NotNull String minRadiusExpression, @NotNull String maxRadiusExpression) {
        this.minRadiusExpression = minRadiusExpression;
        this.maxRadiusExpression = maxRadiusExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        if (origin.getWorld() == null) {
            return Collections.emptyList();
        }

        double minR = 0.0;
        double maxR = 5.0;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                minR = Double.parseDouble(api.parsePlaceholders(context.getCaster(), minRadiusExpression));
            } catch (NumberFormatException ignored) {}
            try {
                maxR = Double.parseDouble(api.parsePlaceholders(context.getCaster(), maxRadiusExpression));
            } catch (NumberFormatException ignored) {}
        }

        double finalMinR = minR;
        double finalMaxR = maxR;
        double finalMinRSq = minR * minR;
        double finalMaxRSq = maxR * maxR;

        return origin.getWorld().getNearbyEntities(origin, finalMaxR, finalMaxR, finalMaxR).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> {
                    double distSq = entity.getLocation().distanceSquared(origin);
                    return distSq >= finalMinRSq && distSq <= finalMaxRSq;
                })
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
