package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the spawn location of the world.
 */
public final class SpawnLocationTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        World world = context.getCaster().getLocation().getWorld();
        if (world != null) {
            return Collections.singletonList(new LocationTarget(world.getSpawnLocation()));
        }
        return Collections.singletonList(new LocationTarget(context.getCaster().getLocation()));
    }
}
