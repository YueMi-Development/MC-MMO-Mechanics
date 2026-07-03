package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Deals damage to targeted entities as raw values, multiplier of caster base damage, or percentage of target health.
 */
public final class DamageMechanic implements Mechanic {
    public enum Type {
        RAW,
        BASE,
        PERCENT
    }

    private final Type type;
    private final double value;
    private final boolean ignoreArmor;

    public DamageMechanic(@NotNull Type type, double value, boolean ignoreArmor) {
        this.type = type;
        this.value = value;
        this.ignoreArmor = ignoreArmor;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Entity caster = context.getCaster().getAsEntity();

        double baseDamage = 1.0;
        if (type == Type.BASE && caster instanceof LivingEntity livingCaster) {
            var attr = livingCaster.getAttribute(Attribute.ATTACK_DAMAGE);
            if (attr != null) {
                baseDamage = attr.getValue();
            }
        }

        for (Target target : targets) {
            if (target.getAsEntity() instanceof Damageable damageable) {
                double amount = value;
                if (type == Type.BASE) {
                    amount = baseDamage * value;
                } else if (type == Type.PERCENT && damageable instanceof LivingEntity livingTarget) {
                    var maxHealthAttr = livingTarget.getAttribute(Attribute.MAX_HEALTH);
                    double maxHealth = maxHealthAttr != null ? maxHealthAttr.getValue() : livingTarget.getHealth();
                    amount = maxHealth * value;
                }

                if (ignoreArmor && damageable instanceof LivingEntity livingTarget) {
                    // Bypass armor/damage reduction by directly adjusting health
                    double newHealth = Math.max(0, livingTarget.getHealth() - amount);
                    livingTarget.setHealth(newHealth);
                    // trigger red hurt animation and damage event if possible
                    if (newHealth <= 0) {
                        livingTarget.damage(1000000.0, caster);
                    } else {
                        livingTarget.damage(0.001, caster); // play hurt animation/sound
                    }
                } else {
                    damageable.damage(amount, caster);
                }
            }
        }
    }
}

