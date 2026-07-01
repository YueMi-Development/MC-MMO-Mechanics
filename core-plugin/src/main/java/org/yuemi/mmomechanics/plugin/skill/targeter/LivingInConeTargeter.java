package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
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
 * Targets living entities located inside a cone sector in front of the caster.
 */
public final class LivingInConeTargeter implements Targeter {

    private final String angleExpression;
    private final String lengthExpression;
    private final String rotationExpression;

    public LivingInConeTargeter(@NotNull String angleExpression, @NotNull String lengthExpression, @NotNull String rotationExpression) {
        this.angleExpression = angleExpression;
        this.lengthExpression = lengthExpression;
        this.rotationExpression = rotationExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        if (origin.getWorld() == null) {
            return Collections.emptyList();
        }

        double angle = 45.0;
        double length = 10.0;
        double rotation = 0.0;

        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                angle = Double.parseDouble(api.parsePlaceholders(context.getCaster(), angleExpression));
            } catch (NumberFormatException ignored) {}
            try {
                length = Double.parseDouble(api.parsePlaceholders(context.getCaster(), lengthExpression));
            } catch (NumberFormatException ignored) {}
            try {
                rotation = Double.parseDouble(api.parsePlaceholders(context.getCaster(), rotationExpression));
            } catch (NumberFormatException ignored) {}
        }

        Vector direction = origin.getDirection().clone().setY(0).normalize();
        if (rotation != 0.0) {
            direction.rotateAroundY(Math.toRadians(rotation));
        }

        double finalLength = length;
        double maxAngleDiff = angle / 2.0;

        Entity casterEntity = context.getCaster().getAsEntity();

        return origin.getWorld().getNearbyEntities(origin, length, length, length).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> entity != casterEntity)
                .filter(entity -> {
                    Vector toTarget = entity.getLocation().toVector().subtract(origin.toVector());
                    double dist = toTarget.length();
                    if (dist > finalLength) return false;
                    if (dist == 0.0) return true;
                    toTarget.setY(0).normalize();
                    double angleDiff = Math.toDegrees(direction.angle(toTarget));
                    return angleDiff <= maxAngleDiff;
                })
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
