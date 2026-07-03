package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Breaks the block at the target location, with options to drop items naturally or give them to the player.
 */
public final class BreakBlockMechanic implements Mechanic {
    private final boolean dropItems;
    private final Material giveMaterial;
    private final int giveAmount;

    public BreakBlockMechanic(boolean dropItems, @NotNull Material giveMaterial, int giveAmount) {
        this.dropItems = dropItems;
        this.giveMaterial = giveMaterial;
        this.giveAmount = giveAmount;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Player casterPlayer = context.getCaster().getAsEntity() instanceof Player p ? p : null;

        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            if (block.getType().isAir()) continue;

            if (giveMaterial != Material.AIR && casterPlayer != null) {
                block.setType(Material.AIR);
                casterPlayer.getInventory().addItem(new ItemStack(giveMaterial, giveAmount));
            } else {
                if (dropItems) {
                    block.breakNaturally();
                } else {
                    block.setType(Material.AIR);
                }
            }
        }
    }
}

