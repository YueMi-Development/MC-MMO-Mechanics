package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Appends custom recipes/trades to villagers.
 */
public final class AddTradeMechanic implements Mechanic {
    private final Material resultMaterial;
    private final int resultAmount;
    private final Material ingredient1;
    private final int ingredient1Amount;
    private final Material ingredient2;
    private final int ingredient2Amount;

    public AddTradeMechanic(
            @NotNull Material resultMaterial,
            int resultAmount,
            @NotNull Material ingredient1,
            int ingredient1Amount,
            @NotNull Material ingredient2,
            int ingredient2Amount
    ) {
        this.resultMaterial = resultMaterial;
        this.resultAmount = resultAmount;
        this.ingredient1 = ingredient1;
        this.ingredient1Amount = ingredient1Amount;
        this.ingredient2 = ingredient2;
        this.ingredient2Amount = ingredient2Amount;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity instanceof Villager villager) {
                List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());

                MerchantRecipe recipe = new MerchantRecipe(new ItemStack(resultMaterial, resultAmount), 999);
                recipe.addIngredient(new ItemStack(ingredient1, ingredient1Amount));
                if (ingredient2 != Material.AIR && ingredient2Amount > 0) {
                    recipe.addIngredient(new ItemStack(ingredient2, ingredient2Amount));
                }

                recipes.add(recipe);
                villager.setRecipes(recipes);
            }
        }
    }
}

