package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

/**
 * Targets the highest solid block location directly above or below the caster.
 */
public final class HighestBlockTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location casterLoc = context.getCaster().getLocation();
        World world = casterLoc.getWorld();
        if (world != null) {
            Block block = world.getHighestBlockAt(casterLoc);
            return Collections.singletonList(new LocationTarget(block.getLocation()));
        }
        return Collections.singletonList(new LocationTarget(casterLoc));
    }
}
