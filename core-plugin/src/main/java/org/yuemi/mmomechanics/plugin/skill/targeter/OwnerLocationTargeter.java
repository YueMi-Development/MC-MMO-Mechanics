package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the location of the owner of the casting entity.
 */
public final class OwnerLocationTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity instanceof Tameable tameable) {
            AnimalTamer owner = tameable.getOwner();
            if (owner instanceof Player player) {
                return Collections.singletonList(new LocationTarget(player.getLocation()));
            }
        }
        return Collections.emptyList();
    }
}
