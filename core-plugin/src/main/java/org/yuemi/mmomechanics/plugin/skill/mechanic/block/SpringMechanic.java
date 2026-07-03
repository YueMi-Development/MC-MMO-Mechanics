package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Spawns temporary liquid sources (water or lava) that vanish after a duration.
 */
public final class SpringMechanic implements Mechanic {
    private final Material liquid;
    private final int durationTicks;

    public SpringMechanic(@NotNull Material liquid, int durationTicks) {
        this.liquid = liquid;
        this.durationTicks = durationTicks;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());
        if (plugin == null) return;

        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            if (!block.getType().isAir() && block.getType() != Material.WATER && block.getType() != Material.LAVA) {
                continue; // only place in air or replace existing liquids
            }

            Material original = block.getType();
            block.setType(liquid);

            if (durationTicks > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Block currentBlock = block.getLocation().getBlock();
                        if (currentBlock.getType() == liquid) {
                            currentBlock.setType(original);
                        }
                    }
                }.runTaskLater(plugin, durationTicks);
            }
        }
    }
}

