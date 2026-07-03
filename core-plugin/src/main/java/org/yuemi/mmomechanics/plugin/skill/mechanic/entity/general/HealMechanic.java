package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Restores health to target entities by raw values or max health percentages.
 */
public final class HealMechanic implements Mechanic {
    private final double value;
    private final boolean isPercent;

    public HealMechanic(double value, boolean isPercent) {
        this.value = value;
        this.isPercent = isPercent;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            if (target.getAsEntity() instanceof LivingEntity living) {
                if (living.isDead()) continue;

                var maxHealthAttr = living.getAttribute(Attribute.MAX_HEALTH);
                double maxHealth = maxHealthAttr != null ? maxHealthAttr.getValue() : living.getHealth();

                double healAmount = value;
                if (isPercent) {
                    healAmount = maxHealth * value;
                }

                double newHealth = Math.min(maxHealth, living.getHealth() + healAmount);
                living.setHealth(newHealth);
            }
        }
    }
}

