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
 * Generates a wall-like plane grid of locations in front of the caster.
 */
public final class ForwardWallTargeter implements Targeter {

    private final String widthExpression;
    private final String heightExpression;
    private final String distanceExpression;

    public ForwardWallTargeter(@NotNull String widthExpression, @NotNull String heightExpression, @NotNull String distanceExpression) {
        this.widthExpression = widthExpression;
        this.heightExpression = heightExpression;
        this.distanceExpression = distanceExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double width = 3.0;
        double height = 3.0;
        double distance = 5.0;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                width = Double.parseDouble(api.parsePlaceholders(context.getCaster(), widthExpression));
            } catch (NumberFormatException ignored) {}
            try {
                height = Double.parseDouble(api.parsePlaceholders(context.getCaster(), heightExpression));
            } catch (NumberFormatException ignored) {}
            try {
                distance = Double.parseDouble(api.parsePlaceholders(context.getCaster(), distanceExpression));
            } catch (NumberFormatException ignored) {}
        }

        Vector dir = origin.getDirection().clone().normalize();
        Vector right = new Vector(0, 1, 0).crossProduct(dir);
        if (right.lengthSquared() < 0.001) {
            right = new Vector(1, 0, 0); // fallback if looking straight up/down
        } else {
            right.normalize();
        }
        Vector up = dir.clone().crossProduct(right).normalize();

        Location center = origin.clone().add(dir.multiply(distance));
        List<Target> targets = new ArrayList<>();

        int wSteps = (int) width;
        int hSteps = (int) height;

        for (int w = -wSteps / 2; w <= wSteps / 2; w++) {
            for (int h = -hSteps / 2; h <= hSteps / 2; h++) {
                Location loc = center.clone().add(right.clone().multiply(w)).add(up.clone().multiply(h));
                targets.add(new LocationTarget(loc));
            }
        }

        return targets;
    }
}
