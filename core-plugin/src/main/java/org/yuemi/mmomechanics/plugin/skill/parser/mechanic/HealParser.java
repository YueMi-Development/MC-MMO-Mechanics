package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general.HealMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class HealParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean isPercent = lower.startsWith("healpercent");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        double value = 1.0;
        try {
            if (options.containsKey("amount")) value = Double.parseDouble(options.get("amount"));
            else if (options.containsKey("value")) value = Double.parseDouble(options.get("value"));
            else if (options.containsKey("a")) value = Double.parseDouble(options.get("a"));
            else if (options.containsKey("v")) value = Double.parseDouble(options.get("v"));
            else if (options.containsKey("percent")) value = Double.parseDouble(options.get("percent"));
            else if (options.containsKey("p")) value = Double.parseDouble(options.get("p"));
        } catch (NumberFormatException ignored) {}

        return new HealMechanic(value, isPercent);
    }
}
