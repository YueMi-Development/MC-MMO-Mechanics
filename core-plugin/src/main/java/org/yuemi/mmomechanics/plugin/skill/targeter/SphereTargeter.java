package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generates point locations forming a sphere shell around the caster.
 */
public final class SphereTargeter implements Targeter {

    private final String radiusExpression;
    private final String pointsExpression;

    public SphereTargeter(@NotNull String radiusExpression, @NotNull String pointsExpression) {
        this.radiusExpression = radiusExpression;
        this.pointsExpression = pointsExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double radius = 3.0;
        int points = 20;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                radius = Double.parseDouble(api.parsePlaceholders(context.getCaster(), radiusExpression));
            } catch (NumberFormatException ignored) {}
            try {
                points = Integer.parseInt(api.parsePlaceholders(context.getCaster(), pointsExpression));
            } catch (NumberFormatException ignored) {}
        }

        List<Target> targets = new ArrayList<>();

        double phi = Math.PI * (Math.sqrt(5.0) - 1.0);

        for (int i = 0; i < points; i++) {
            double y = 1 - (i / (double) (points - 1)) * 2;
            double radiusAtY = Math.sqrt(1 - y * y);

            double theta = phi * i;

            double x = Math.cos(theta) * radiusAtY;
            double z = Math.sin(theta) * radiusAtY;

            Location point = origin.clone().add(x * radius, y * radius, z * radius);
            targets.add(new LocationTarget(point));
        }

        return targets;
    }
}
