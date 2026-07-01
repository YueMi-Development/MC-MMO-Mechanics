package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the parent or summoner entity of the caster.
 */
public final class ParentTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity instanceof Tameable tameable) {
            AnimalTamer owner = tameable.getOwner();
            if (owner instanceof Player player) {
                return Collections.singletonList(new EntityTarget(player));
            }
        }
        return Collections.emptyList();
    }
}
