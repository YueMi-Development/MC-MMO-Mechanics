package org.yuemi.mmomechanics.plugin.skill.mechanic.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Feeds targeted players, increasing food and saturation levels.
 */
public final class FeedMechanic implements Mechanic {
    private final int amount;
    private final float saturation;

    public FeedMechanic(int amount, float saturation) {
        this.amount = amount;
        this.saturation = saturation;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                int newFood = Math.max(0, Math.min(20, player.getFoodLevel() + amount));
                player.setFoodLevel(newFood);
                if (saturation != 0.0f) {
                    float newSat = Math.max(0.0f, Math.min(20.0f, player.getSaturation() + saturation));
                    player.setSaturation(newSat);
                }
            }
        }
    }
}

