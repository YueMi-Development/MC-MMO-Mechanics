package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general.DamageMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class DamageParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        DamageMechanic.Type type = DamageMechanic.Type.RAW;
        if (lower.startsWith("basedamage")) {
            type = DamageMechanic.Type.BASE;
        } else if (lower.startsWith("percentdamage")) {
            type = DamageMechanic.Type.PERCENT;
        }

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

        boolean ignoreArmor = false;
        if (options.containsKey("ignorearmor")) ignoreArmor = Boolean.parseBoolean(options.get("ignorearmor"));
        else if (options.containsKey("ia")) ignoreArmor = Boolean.parseBoolean(options.get("ia"));

        return new DamageMechanic(type, value, ignoreArmor);
    }
}
