package org.yuemi.mmomechanics.plugin.skill.targeter.location.multi;

import org.bukkit.Location;
import org.bukkit.util.Vector;
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
 * Generates a list of locations forming a cone shape on the ground in front of the caster.
 */
public final class ConeTargeter implements Targeter {

    private final String angleExpression;
    private final String lengthExpression;
    private final String pointsExpression;

    public ConeTargeter(@NotNull String angleExpression, @NotNull String lengthExpression, @NotNull String pointsExpression) {
        this.angleExpression = angleExpression;
        this.lengthExpression = lengthExpression;
        this.pointsExpression = pointsExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double angle = 45.0;
        double length = 10.0;
        int points = 10;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                angle = Double.parseDouble(api.parsePlaceholders(context.getCaster(), angleExpression));
            } catch (NumberFormatException ignored) {}
            try {
                length = Double.parseDouble(api.parsePlaceholders(context.getCaster(), lengthExpression));
            } catch (NumberFormatException ignored) {}
            try {
                points = Integer.parseInt(api.parsePlaceholders(context.getCaster(), pointsExpression));
            } catch (NumberFormatException ignored) {}
        }

        List<Target> targets = new ArrayList<>();
        Vector lookDir = origin.getDirection().clone().setY(0).normalize();
        double startAngle = -Math.toRadians(angle / 2.0);
        double endAngle = Math.toRadians(angle / 2.0);
        double angleStep = (endAngle - startAngle) / Math.max(1, points - 1);

        for (int i = 0; i < points; i++) {
            double currentAngle = startAngle + i * angleStep;
            Vector pointVector = lookDir.clone().rotateAroundY(currentAngle).multiply(length);
            Location point = origin.clone().add(pointVector);
            targets.add(new LocationTarget(point));
        }

        return targets;
    }
}
