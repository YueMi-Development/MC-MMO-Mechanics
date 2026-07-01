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
import java.util.Random;

public final class RandomRingPointTargeter implements Targeter {

    private static final Random RANDOM = new Random();
    private final String radiusExpression;
    private final String amountExpression;

    public RandomRingPointTargeter(@NotNull String radiusExpression, @NotNull String amountExpression) {
        this.radiusExpression = radiusExpression;
        this.amountExpression = amountExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double radius = 5.0;
        int amount = 1;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                radius = Double.parseDouble(api.parsePlaceholders(context.getCaster(), radiusExpression));
            } catch (NumberFormatException ignored) {}
            try {
                amount = Integer.parseInt(api.parsePlaceholders(context.getCaster(), amountExpression));
            } catch (NumberFormatException ignored) {}
        }

        List<Target> targets = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = RANDOM.nextDouble() * 2 * Math.PI;
            double dx = Math.cos(angle) * radius;
            double dz = Math.sin(angle) * radius;
            Location point = origin.clone().add(dx, 0, dz);
            targets.add(new LocationTarget(point));
        }

        return targets;
    }
}
