package org.yuemi.mmomechanics.plugin.skill.mechanic.block;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Toggles levers, or triggers buttons to power on and automatically shut off after a delay.
 */
public final class ToggleLeverMechanic implements Mechanic {
    private final Boolean powerState;
    private final int pulseTicks; // if > 0, power off after this duration

    public ToggleLeverMechanic(@Nullable Boolean powerState, int pulseTicks) {
        this.powerState = powerState;
        this.pulseTicks = pulseTicks;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());
        if (plugin == null) return;

        for (Target target : targets) {
            Block block = target.getLocation().getBlock();
            BlockData data = block.getBlockData();
            if (data instanceof Powerable powerable) {
                boolean nextState = powerState != null ? powerState : !powerable.isPowered();
                powerable.setPowered(nextState);
                block.setBlockData(powerable);

                // trigger physics updates
                block.getState().update(true, true);

                if (nextState && pulseTicks > 0) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block currentBlock = block.getLocation().getBlock();
                            BlockData currentData = currentBlock.getBlockData();
                            if (currentData instanceof Powerable currentPowerable && currentPowerable.isPowered()) {
                                currentPowerable.setPowered(false);
                                currentBlock.setBlockData(currentPowerable);
                                currentBlock.getState().update(true, true);
                            }
                        }
                    }.runTaskLater(plugin, pulseTicks);
                }
            }
        }
    }
}

