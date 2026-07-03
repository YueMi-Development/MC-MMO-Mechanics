package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general;

import org.bukkit.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Applies potion effects with custom amplifier/duration, or clears active potion effects.
 */
public final class PotionMechanic implements Mechanic {
    private final PotionEffectType effectType;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean particles;
    private final boolean icon;
    private final boolean clear;
    private final boolean clearAll;

    public PotionMechanic(
            @Nullable PotionEffectType effectType,
            int duration,
            int amplifier,
            boolean ambient,
            boolean particles,
            boolean icon,
            boolean clear,
            boolean clearAll
    ) {
        this.effectType = effectType;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
        this.icon = icon;
        this.clear = clear;
        this.clearAll = clearAll;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            if (target.getAsEntity() instanceof LivingEntity living) {
                if (clear) {
                    if (clearAll) {
                        for (PotionEffect effect : living.getActivePotionEffects()) {
                            living.removePotionEffect(effect.getType());
                        }
                    } else if (effectType != null) {
                        living.removePotionEffect(effectType);
                    }
                } else if (effectType != null) {
                    living.addPotionEffect(new PotionEffect(effectType, duration, amplifier, ambient, particles, icon));
                }
            }
        }
    }
}

