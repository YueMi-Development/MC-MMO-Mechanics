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
 * Generates a 3D grid of target locations forming a rectangle/cuboid outline.
 */
public final class RectangleTargeter implements Targeter {

    private final String widthExpression;
    private final String heightExpression;
    private final String lengthExpression;

    public RectangleTargeter(@NotNull String widthExpression, @NotNull String heightExpression, @NotNull String lengthExpression) {
        this.widthExpression = widthExpression;
        this.heightExpression = heightExpression;
        this.lengthExpression = lengthExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double w = 3.0;
        double h = 3.0;
        double l = 3.0;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                w = Double.parseDouble(api.parsePlaceholders(context.getCaster(), widthExpression));
            } catch (NumberFormatException ignored) {}
            try {
                h = Double.parseDouble(api.parsePlaceholders(context.getCaster(), heightExpression));
            } catch (NumberFormatException ignored) {}
            try {
                l = Double.parseDouble(api.parsePlaceholders(context.getCaster(), lengthExpression));
            } catch (NumberFormatException ignored) {}
        }

        List<Target> targets = new ArrayList<>();
        int xSteps = (int) w;
        int ySteps = (int) h;
        int zSteps = (int) l;

        for (int x = -xSteps / 2; x <= xSteps / 2; x++) {
            for (int y = -ySteps / 2; y <= ySteps / 2; y++) {
                for (int z = -zSteps / 2; z <= zSteps / 2; z++) {
                    Location loc = origin.clone().add(x, y, z);
                    targets.add(new LocationTarget(loc));
                }
            }
        }

        return targets;
    }
}
