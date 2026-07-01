package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the location marked by a pin in the current skill context.
 */
public final class PinTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        return context.getVariable("pin")
                .filter(val -> val instanceof Location)
                .map(val -> (Location) val)
                .map(loc -> Collections.singletonList((Target) new LocationTarget(loc)))
                .orElseGet(() -> Collections.singletonList(new LocationTarget(context.getCaster().getLocation())));
    }
}
