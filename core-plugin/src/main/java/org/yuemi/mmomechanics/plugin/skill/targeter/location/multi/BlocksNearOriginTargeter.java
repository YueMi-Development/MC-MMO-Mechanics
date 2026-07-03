package org.yuemi.mmomechanics.plugin.skill.targeter.location.multi;

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
 * Targets all block locations in a radius around the origin of the skill.
 */
public final class BlocksNearOriginTargeter implements Targeter {

    private final String radiusExpression;

    public BlocksNearOriginTargeter(@NotNull String radiusExpression) {
        this.radiusExpression = radiusExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        double radius = 3.0;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                radius = Double.parseDouble(api.parsePlaceholders(context.getCaster(), radiusExpression));
            } catch (NumberFormatException ignored) {}
        }

        List<Target> targets = new ArrayList<>();
        int r = (int) radius;

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    Location loc = origin.clone().add(x, y, z);
                    targets.add(new LocationTarget(loc));
                }
            }
        }

        return targets;
    }
}
