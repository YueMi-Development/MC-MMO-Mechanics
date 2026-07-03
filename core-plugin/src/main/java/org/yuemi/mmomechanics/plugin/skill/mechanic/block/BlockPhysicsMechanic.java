package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Triggers physical block updates at targeted locations.
 */
public final class BlockPhysicsMechanic implements Mechanic {

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            block.setBlockData(block.getBlockData(), true);
        }
    }
}

