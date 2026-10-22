package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.CasterSnapshot;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.plugin.hook.EssenceHook;
import org.yuemi.mmoessence.api.ElementType;

import java.util.Collection;

/**
 * Deals damage to targeted entities as raw values, multiplier of caster base damage, or percentage of target health.
 * Supports elemental damage types with essence-based scaling.
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
    private final ElementType element;

    public DamageMechanic(@NotNull Type type, double value, boolean ignoreArmor) {
        this(type, value, ignoreArmor, null);
    }

    public DamageMechanic(@NotNull Type type, double value, boolean ignoreArmor, ElementType element) {
        this.type = type;
        this.value = value;
        this.ignoreArmor = ignoreArmor;
        this.element = element;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Entity caster = context.getCaster().getAsEntity();
        CasterSnapshot snapshot = context.getCasterSnapshot();

        double baseDamage = 1.0;
        if (type == Type.BASE && caster instanceof LivingEntity livingCaster) {
            if (snapshot != null && snapshot.baseAttackDamage() != null) {
                baseDamage = snapshot.baseAttackDamage();
            } else {
                var attr = livingCaster.getAttribute(Attribute.ATTACK_DAMAGE);
                if (attr != null) {
                    baseDamage = attr.getValue();
                }
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

                // Apply essence-based damage calculation
                if (caster instanceof LivingEntity livingCaster) {
                    amount = calculateDamage(amount, livingCaster);
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

    /**
     * Calculate damage with essence scaling.
     *
     * @param baseDamage The base damage value
     * @param caster    The living entity dealing damage
     * @return Calculated damage
     */
    private double calculateDamage(double baseDamage, LivingEntity caster) {
        double finalDamage = baseDamage;

        if (!ignoreArmor) {
            if (element != null && EssenceHook.isEnabled(EssenceHook.getPlugin())) {
                double essenceMultiplier = EssenceHook.getDamageMultiplier(element, caster);
                finalDamage *= essenceMultiplier;
            }
        }

        return Math.max(Math.abs(finalDamage), 0.001);
    }
}
