package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class ProjectileForwardTargeter implements Targeter {

    private final String distanceExpression;

    public ProjectileForwardTargeter(@NotNull String distanceExpression) {
        this.distanceExpression = distanceExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        double distance = 5.0;
        MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            try {
                distance = Double.parseDouble(api.parsePlaceholders(context.getCaster(), distanceExpression));
            } catch (NumberFormatException ignored) {}
        }

        Location origin = null;
        if (context.getTrigger().getTriggerTarget().isPresent()) {
            Entity triggerEntity = context.getTrigger().getTriggerTarget().get().getAsEntity();
            if (triggerEntity != null) {
                origin = triggerEntity.getLocation();
            }
        }

        if (origin == null) {
            origin = context.getCaster().getLocation();
        }

        Location targetLoc = origin.clone().add(origin.getDirection().multiply(distance));
        return Collections.singletonList(new LocationTarget(targetLoc));
    }
}
