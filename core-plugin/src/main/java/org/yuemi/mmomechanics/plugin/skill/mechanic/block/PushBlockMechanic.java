package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Physically slides blocks in a chosen direction (cardinal or facing direction).
 */
public final class PushBlockMechanic implements Mechanic {
    private final String directionStr;
    private final int distance;

    public PushBlockMechanic(@NotNull String directionStr, int distance) {
        this.directionStr = directionStr.toLowerCase().trim();
        this.distance = distance;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Entity caster = context.getCaster().getAsEntity();

        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            if (block.getType().isAir()) continue;

            Vector dir = new Vector(0, 0, 0);
            switch (directionStr) {
                case "north" -> dir.setZ(-1);
                case "south" -> dir.setZ(1);
                case "east" -> dir.setX(1);
                case "west" -> dir.setX(-1);
                case "up" -> dir.setY(1);
                case "down" -> dir.setY(-1);
                default -> {
                    if (caster != null) {
                        dir = caster.getLocation().getDirection().clone().setY(0);
                        if (dir.lengthSquared() > 0) {
                            dir.normalize();
                        }
                    } else {
                        dir.setY(1); // Default to up if no caster
                    }
                }
            }

            BlockData blockData = block.getBlockData().clone();
            Material material = block.getType();

            // Set source to AIR
            block.setType(Material.AIR);

            // Set destination
            Block destination = block.getLocation().add(dir.multiply(distance)).getBlock();
            destination.setType(material, true);
            destination.setBlockData(blockData, true);
        }
    }
}

