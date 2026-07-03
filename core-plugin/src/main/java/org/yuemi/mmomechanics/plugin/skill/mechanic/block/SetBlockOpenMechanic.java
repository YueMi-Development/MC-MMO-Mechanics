package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Opens or closes doors, trapdoors, fence gates, chests, or shulker boxes at target locations.
 */
public final class SetBlockOpenMechanic implements Mechanic {
    private final boolean openState;

    public SetBlockOpenMechanic(boolean openState) {
        this.openState = openState;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            BlockData data = block.getBlockData();
            if (data instanceof Openable openable) {
                openable.setOpen(openState);
                block.setBlockData(openable);
            } else if (block.getState() instanceof Lidded lidded) {
                if (openState) {
                    lidded.open();
                } else {
                    lidded.close();
                }
            }
        }
    }
}

