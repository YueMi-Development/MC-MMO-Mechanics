package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Targets all living entities in the same world as the caster.
 */
public final class LivingInWorldTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        World world = context.getCaster().getLocation().getWorld();
        if (world == null) {
            return Collections.emptyList();
        }
        return world.getLivingEntities().stream()
                .filter(entity -> entity != context.getCaster().getAsEntity())
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
