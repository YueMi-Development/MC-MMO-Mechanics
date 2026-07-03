package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Fills targeted containers (chests, barrels, shulker boxes) with items, optionally clearing them first.
 */
public final class FillChestMechanic implements Mechanic {
    private final Material material;
    private final int amount;
    private final boolean clear;

    public FillChestMechanic(@NotNull Material material, int amount, boolean clear) {
        this.material = material;
        this.amount = amount;
        this.clear = clear;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            if (block.getState() instanceof Container container) {
                Inventory inv = container.getInventory();
                if (clear) {
                    inv.clear();
                }
                if (material != Material.AIR && amount > 0) {
                    inv.addItem(new ItemStack(material, amount));
                }
            }
        }
    }
}

