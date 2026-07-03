package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Changes the type/material of blocks at target locations with optional physics updates.
 */
public final class SetBlockTypeMechanic implements Mechanic {
    private final Material material;
    private final boolean physics;

    public SetBlockTypeMechanic(@NotNull Material material, boolean physics) {
        this.material = material;
        this.physics = physics;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            block.setType(material, physics);
        }
    }
}

