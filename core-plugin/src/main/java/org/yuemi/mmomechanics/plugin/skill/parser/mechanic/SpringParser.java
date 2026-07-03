package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.SpringMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SpringParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        Material liquid = Material.WATER;
        String liquidStr = options.getOrDefault("liquid", options.getOrDefault("type", options.getOrDefault("l", "water"))).toUpperCase();
        if (liquidStr.contains("LAVA")) {
            liquid = Material.LAVA;
        }

        int duration = 100;
        try {
            if (options.containsKey("duration")) duration = Integer.parseInt(options.get("duration"));
            else if (options.containsKey("ticks")) duration = Integer.parseInt(options.get("ticks"));
            else if (options.containsKey("t")) duration = Integer.parseInt(options.get("t"));
        } catch (NumberFormatException ignored) {}

        return new SpringMechanic(liquid, duration);
    }
}
