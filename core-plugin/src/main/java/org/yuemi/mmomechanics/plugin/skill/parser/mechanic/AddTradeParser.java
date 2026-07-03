package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.AddTradeMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class AddTradeParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        Material result = Material.EMERALD;
        String resultStr = options.getOrDefault("result", options.getOrDefault("r", ""));
        if (!resultStr.isEmpty()) {
            Material matched = Material.matchMaterial(resultStr.toUpperCase());
            if (matched != null) result = matched;
        }

        int amount = 1;
        try {
            if (options.containsKey("amount")) amount = Integer.parseInt(options.get("amount"));
            else if (options.containsKey("a")) amount = Integer.parseInt(options.get("a"));
        } catch (NumberFormatException ignored) {}

        Material ing1 = Material.DIRT;
        String ing1Str = options.getOrDefault("ingredient1", options.getOrDefault("ing1", options.getOrDefault("i1", "")));
        if (!ing1Str.isEmpty()) {
            Material matched = Material.matchMaterial(ing1Str.toUpperCase());
            if (matched != null) ing1 = matched;
        }

        int ing1Amount = 1;
        try {
            if (options.containsKey("ingredient1amount")) ing1Amount = Integer.parseInt(options.get("ingredient1amount"));
            else if (options.containsKey("ing1a")) ing1Amount = Integer.parseInt(options.get("ing1a"));
            else if (options.containsKey("i1a")) ing1Amount = Integer.parseInt(options.get("i1a"));
        } catch (NumberFormatException ignored) {}

        Material ing2 = Material.AIR;
        String ing2Str = options.getOrDefault("ingredient2", options.getOrDefault("ing2", options.getOrDefault("i2", "")));
        if (!ing2Str.isEmpty()) {
            Material matched = Material.matchMaterial(ing2Str.toUpperCase());
            if (matched != null) ing2 = matched;
        }

        int ing2Amount = 0;
        try {
            if (options.containsKey("ingredient2amount")) ing2Amount = Integer.parseInt(options.get("ingredient2amount"));
            else if (options.containsKey("ing2a")) ing2Amount = Integer.parseInt(options.get("ing2a"));
            else if (options.containsKey("i2a")) ing2Amount = Integer.parseInt(options.get("i2a"));
        } catch (NumberFormatException ignored) {}

        return new AddTradeMechanic(result, amount, ing1, ing1Amount, ing2, ing2Amount);
    }
}
