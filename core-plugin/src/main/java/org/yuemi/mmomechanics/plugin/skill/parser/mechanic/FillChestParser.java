package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.FillChestMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class FillChestParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        Material material = Material.AIR;
        String itemStr = options.getOrDefault("item", options.getOrDefault("i", options.getOrDefault("material", options.getOrDefault("m", ""))));
        if (!itemStr.isEmpty()) {
            Material matched = Material.matchMaterial(itemStr.toUpperCase());
            if (matched != null) material = matched;
        }

        int amount = 1;
        try {
            if (options.containsKey("amount")) amount = Integer.parseInt(options.get("amount"));
            else if (options.containsKey("a")) amount = Integer.parseInt(options.get("a"));
        } catch (NumberFormatException ignored) {}

        boolean clear = true;
        if (options.containsKey("clear")) clear = Boolean.parseBoolean(options.get("clear"));

        return new FillChestMechanic(material, amount, clear);
    }
}
