package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Triggers targeted entities to swing their main hand or off hand.
 */
public final class ArmAnimationMechanic implements Mechanic {
    private final boolean offhand;

    public ArmAnimationMechanic(boolean offhand) {
        this.offhand = offhand;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            if (target.getAsEntity() instanceof LivingEntity living) {
                if (offhand) {
                    living.swingOffHand();
                } else {
                    living.swingMainHand();
                }
            }
        }
    }
}

