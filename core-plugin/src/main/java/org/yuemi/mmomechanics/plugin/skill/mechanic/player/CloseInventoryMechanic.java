package org.yuemi.mmomechanics.plugin.skill.mechanic.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Closes open container screen GUIs.
 */
public final class CloseInventoryMechanic implements Mechanic {

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                player.closeInventory();
            }
        }
    }
}

