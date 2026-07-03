package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Destabilizes blocks, causing them to fall downwards under gravity as active falling block entities.
 */
public final class BlockDestabilizeMechanic implements Mechanic {

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            if (block.getType().isAir()) continue;

            World world = block.getWorld();
            BlockData data = block.getBlockData().clone();
            
            // Spawn falling block slightly offset to avoid instant block landing
            block.setType(Material.AIR);
            FallingBlock falling = world.spawnFallingBlock(block.getLocation().add(0.5, 0.0, 0.5), data);
            falling.setDropItem(true);
            falling.setCancelDrop(false);
        }
    }
}

