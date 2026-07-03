package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Forces targeted entities to equip items or copies equipment configurations.
 */
public final class EquipMechanic implements Mechanic {
    public enum Slot {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        MAINHAND,
        OFFHAND
    }

    private final boolean isCopy;
    private final Slot slot;
    private final Material material;
    private final int amount;

    public EquipMechanic(boolean isCopy, @NotNull Slot slot, @NotNull Material material, int amount) {
        this.isCopy = isCopy;
        this.slot = slot;
        this.material = material;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Entity caster = context.getCaster().getAsEntity();

        if (isCopy && caster instanceof LivingEntity livingCaster) {
            EntityEquipment casterEq = livingCaster.getEquipment();
            if (casterEq == null) return;
            for (Target target : targets) {
                if (target.getAsEntity() instanceof LivingEntity livingTarget) {
                    EntityEquipment targetEq = livingTarget.getEquipment();
                    if (targetEq != null) {
                        casterEq.setHelmet(targetEq.getHelmet() != null ? targetEq.getHelmet().clone() : null);
                        casterEq.setChestplate(targetEq.getChestplate() != null ? targetEq.getChestplate().clone() : null);
                        casterEq.setLeggings(targetEq.getLeggings() != null ? targetEq.getLeggings().clone() : null);
                        casterEq.setBoots(targetEq.getBoots() != null ? targetEq.getBoots().clone() : null);
                        casterEq.setItemInMainHand(targetEq.getItemInMainHand() != null ? targetEq.getItemInMainHand().clone() : null);
                        casterEq.setItemInOffHand(targetEq.getItemInOffHand() != null ? targetEq.getItemInOffHand().clone() : null);
                        break; // Copy first target
                    }
                }
            }
        } else {
            // Equip specific item on targeted entities (or caster if targeting self)
            for (Target target : targets) {
                if (target.getAsEntity() instanceof LivingEntity livingTarget) {
                    EntityEquipment eq = livingTarget.getEquipment();
                    if (eq != null) {
                        ItemStack item = material == Material.AIR ? null : new ItemStack(material, amount);
                        switch (slot) {
                            case HELMET -> eq.setHelmet(item);
                            case CHESTPLATE -> eq.setChestplate(item);
                            case LEGGINGS -> eq.setLeggings(item);
                            case BOOTS -> eq.setBoots(item);
                            case MAINHAND -> eq.setItemInMainHand(item);
                            case OFFHAND -> eq.setItemInOffHand(item);
                        }
                    }
                }
            }
        }
    }
}

