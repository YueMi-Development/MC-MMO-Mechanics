package org.yuemi.mmomechanics.plugin.skill.targeter.entity.multi;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
 * Targets monsters or vanilla mobs of specified types within a radius.
 */
public final class MobsInRadiusTargeter implements Targeter {

    private final String radiusExpression;
    private final String typesExpression;

    public MobsInRadiusTargeter(@NotNull String radiusExpression, @NotNull String typesExpression) {
        this.radiusExpression = radiusExpression;
        this.typesExpression = typesExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        if (origin.getWorld() == null) {
            return Collections.emptyList();
        }

        double radius = 5.0;
        String typesStr = "";

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                radius = Double.parseDouble(api.parsePlaceholders(context.getCaster(), radiusExpression));
            } catch (NumberFormatException ignored) {}
            typesStr = api.parsePlaceholders(context.getCaster(), typesExpression).trim().toLowerCase();
        }

        final double finalRadius = radius;
        final String finalTypes = typesStr;

        return origin.getWorld().getNearbyEntities(origin, finalRadius, finalRadius, finalRadius).stream()
                .filter(entity -> entity instanceof LivingEntity && !(entity instanceof Player))
                .filter(entity -> {
                    if (finalTypes.isEmpty() || finalTypes.equals("all") || finalTypes.equals("any")) {
                        return true;
                    }
                    String entityTypeName = entity.getType().name().toLowerCase();
                    for (String t : finalTypes.split(",")) {
                        if (entityTypeName.equals(t.trim())) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
