package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class TargetPredictedLocationTargeter implements Targeter {

    private final String ticksExpression;

    public TargetPredictedLocationTargeter(@NotNull String ticksExpression) {
        this.ticksExpression = ticksExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        Entity target = null;
        if (casterEntity instanceof Mob mob) {
            target = mob.getTarget();
        } else if (casterEntity instanceof LivingEntity living) {
            target = living.getTargetEntity(50);
        }

        if (target == null) {
            return Collections.emptyList();
        }

        double ticks = 5.0;
        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                ticks = Double.parseDouble(api.parsePlaceholders(context.getCaster(), ticksExpression));
            } catch (NumberFormatException ignored) {}
        }

        Location targetLoc = target.getLocation();
        org.bukkit.util.Vector velocity = target.getVelocity();
        Location predicted = targetLoc.clone().add(velocity.multiply(ticks));
        return Collections.singletonList(new LocationTarget(predicted));
    }
}
