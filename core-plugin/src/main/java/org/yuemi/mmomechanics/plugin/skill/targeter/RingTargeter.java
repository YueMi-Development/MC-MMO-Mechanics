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

public final class RingTargeter implements Targeter {

    private final String radiusExpression;
    private final String pointsExpression;

    public RingTargeter(@NotNull String radiusExpression, @NotNull String pointsExpression) {
        this.radiusExpression = radiusExpression;
        this.pointsExpression = pointsExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double radius = 5.0;
        int points = 12;

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
        double angleStep = 2 * Math.PI / points;

        for (int i = 0; i < points; i++) {
            double angle = i * angleStep;
            double dx = Math.cos(angle) * radius;
            double dz = Math.sin(angle) * radius;
            Location point = origin.clone().add(dx, 0, dz);
            targets.add(new LocationTarget(point));
        }

        return targets;
    }
}
