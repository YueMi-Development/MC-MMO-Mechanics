package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.BreakBlockMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class BreakBlockParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        boolean drop = true;
        if (options.containsKey("drop")) drop = Boolean.parseBoolean(options.get("drop"));
        else if (options.containsKey("d")) drop = Boolean.parseBoolean(options.get("d"));

        Material give = Material.AIR;
        int amount = 1;

        if (lower.startsWith("breakblockandgiveitem")) {
            String itemStr = options.getOrDefault("give", options.getOrDefault("g", options.getOrDefault("item", options.getOrDefault("i", ""))));
            if (!itemStr.isEmpty()) {
                Material matched = Material.matchMaterial(itemStr.toUpperCase());
                if (matched != null) give = matched;
            }
            try {
                if (options.containsKey("amount")) amount = Integer.parseInt(options.get("amount"));
                else if (options.containsKey("a")) amount = Integer.parseInt(options.get("a"));
            } catch (NumberFormatException ignored) {}
        }

        return new BreakBlockMechanic(drop, give, amount);
    }
}
