package org.yuemi.mmomechanics.plugin.skill.targeter.location.single;

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
import java.util.Random;

/**
 * Targets a nearby safe location that the caster can teleport to (similar to an Enderman's escape).
 */
public final class EscapeLocationTargeter implements Targeter {

    private static final Random RANDOM = new Random();

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Location origin = context.getCaster().getLocation();
        World world = origin.getWorld();
        if (world == null) {
            return Collections.singletonList(new LocationTarget(origin));
        }

        // Try up to 15 times to find a safe location within a radius of 8-16 blocks
        for (int i = 0; i < 15; i++) {
            double angle = RANDOM.nextDouble() * 2 * Math.PI;
            double distance = 8 + RANDOM.nextDouble() * 8;
            int x = (int) (origin.getX() + Math.cos(angle) * distance);
            int z = (int) (origin.getZ() + Math.sin(angle) * distance);
            int startY = (int) origin.getY();

            // Check Y heights offset by up to 4 blocks up or down
            for (int yOffset : new int[]{0, 1, -1, 2, -2, 3, -3, 4, -4}) {
                int y = startY + yOffset;
                Block block = world.getBlockAt(x, y, z);
                Block blockAbove = world.getBlockAt(x, y + 1, z);
                Block blockBelow = world.getBlockAt(x, y - 1, z);

                if (block.isEmpty() && blockAbove.isEmpty() && blockBelow.getType().isSolid()) {
                    Location escapeLoc = new Location(world, x + 0.5, y, z + 0.5, origin.getYaw(), origin.getPitch());
                    return Collections.singletonList(new LocationTarget(escapeLoc));
                }
            }
        }

        return Collections.singletonList(new LocationTarget(origin));
    }
}
