package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Changes dye/collar colors for sheep, wolves, cats, llamas, and shulkers.
 */
public final class SetMobColorMechanic implements Mechanic {
    private final String colorName;

    public SetMobColorMechanic(@NotNull String colorName) {
        this.colorName = colorName;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        DyeColor dyeColor = null;
        try {
            dyeColor = DyeColor.valueOf(colorName.toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        Llama.Color llamaColor = null;
        try {
            llamaColor = Llama.Color.valueOf(colorName.toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity == null) continue;

            if (entity instanceof Sheep sheep && dyeColor != null) {
                sheep.setColor(dyeColor);
            } else if (entity instanceof Wolf wolf && dyeColor != null) {
                wolf.setCollarColor(dyeColor);
            } else if (entity instanceof Cat cat && dyeColor != null) {
                cat.setCollarColor(dyeColor);
            } else if (entity instanceof Shulker shulker && dyeColor != null) {
                shulker.setColor(dyeColor);
            } else if (entity instanceof Llama llama && llamaColor != null) {
                llama.setColor(llamaColor);
            }
        }
    }
}

