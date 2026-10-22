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
import org.yuemi.mmomechanics.plugin.skill.executor.TargetDispatcher;

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
                        ItemStack[] copy = {
                                targetEq.getHelmet() != null ? targetEq.getHelmet().clone() : null,
                                targetEq.getChestplate() != null ? targetEq.getChestplate().clone() : null,
                                targetEq.getLeggings() != null ? targetEq.getLeggings().clone() : null,
                                targetEq.getBoots() != null ? targetEq.getBoots().clone() : null,
                                targetEq.getItemInMainHand() != null ? targetEq.getItemInMainHand().clone() : null,
                                targetEq.getItemInOffHand() != null ? targetEq.getItemInOffHand().clone() : null
                        };
                        // The caster may live in another region than the target, so the
                        // equipment write has to run on the caster's own thread.
                        TargetDispatcher dispatcher = TargetDispatcher.getInstance();
                        dispatcher.dispatchToCaster(caster, () -> {
                            casterEq.setHelmet(copy[0]);
                            casterEq.setChestplate(copy[1]);
                            casterEq.setLeggings(copy[2]);
                            casterEq.setBoots(copy[3]);
                            casterEq.setItemInMainHand(copy[4]);
                            casterEq.setItemInOffHand(copy[5]);
                        });
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

