package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Temporarily masks or unmasks blocks client-side, showing fake block materials to players.
 */
public final class BlockMaskMechanic implements Mechanic {
    private final Material maskMaterial;
    private final boolean unmask;

    public BlockMaskMechanic(@NotNull Material maskMaterial, boolean unmask) {
        this.maskMaterial = maskMaterial;
        this.unmask = unmask;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Player casterPlayer = context.getCaster().getAsEntity() instanceof Player p ? p : null;

        for (Target target : targets) {
            Location loc = target.getLocation();
            Block block = loc.getBlock();

            var blockData = unmask ? block.getBlockData() : Bukkit.createBlockData(maskMaterial);

            if (casterPlayer != null) {
                casterPlayer.sendBlockChange(loc, blockData);
            } else {
                for (Player player : loc.getWorld().getPlayers()) {
                    if (player.getLocation().distanceSquared(loc) < 2500) { // 50 blocks radius
                        player.sendBlockChange(loc, blockData);
                    }
                }
            }
        }
    }
}

