package org.yuemi.mmomechanics.plugin.skill.targeter.location.single;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets a specific set of coordinates (X, Y, Z) in the world.
 */
public final class CoordinateLocationTargeter implements Targeter {

    private final String xExpr;
    private final String yExpr;
    private final String zExpr;

    public CoordinateLocationTargeter(@NotNull String xExpr, @NotNull String yExpr, @NotNull String zExpr) {
        this.xExpr = xExpr;
        this.yExpr = yExpr;
        this.zExpr = zExpr;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location casterLoc = context.getCaster().getLocation();
        double x = casterLoc.getX();
        double y = casterLoc.getY();
        double z = casterLoc.getZ();

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                x = Double.parseDouble(api.parsePlaceholders(context.getCaster(), xExpr));
            } catch (NumberFormatException ignored) {}
            try {
                y = Double.parseDouble(api.parsePlaceholders(context.getCaster(), yExpr));
            } catch (NumberFormatException ignored) {}
            try {
                z = Double.parseDouble(api.parsePlaceholders(context.getCaster(), zExpr));
            } catch (NumberFormatException ignored) {}
        }

        Location targetLoc = new Location(casterLoc.getWorld(), x, y, z, casterLoc.getYaw(), casterLoc.getPitch());
        return Collections.singletonList(new LocationTarget(targetLoc));
    }
}
