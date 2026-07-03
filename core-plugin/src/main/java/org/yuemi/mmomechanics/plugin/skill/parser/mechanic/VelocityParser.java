package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general.VelocityMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class VelocityParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean propel = lower.startsWith("propel") || lower.startsWith("throw");
        boolean directional = lower.startsWith("directionalvelocity");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        double x = 0;
        double y = 0;
        double z = 0;
        
        try {
            if (options.containsKey("x")) x = Double.parseDouble(options.get("x"));
            if (options.containsKey("y")) y = Double.parseDouble(options.get("y"));
            if (options.containsKey("z")) z = Double.parseDouble(options.get("z"));
        } catch (NumberFormatException ignored) {}

        double force = 1.0;
        try {
            if (options.containsKey("force")) force = Double.parseDouble(options.get("force"));
            else if (options.containsKey("f")) force = Double.parseDouble(options.get("f"));
            else if (options.containsKey("velocity")) force = Double.parseDouble(options.get("velocity"));
            else if (options.containsKey("v")) force = Double.parseDouble(options.get("v"));
        } catch (NumberFormatException ignored) {}

        VelocityMechanic.Mode mode = VelocityMechanic.Mode.ADD;
        if (options.containsKey("mode") || options.containsKey("m")) {
            String modeStr = options.getOrDefault("mode", options.getOrDefault("m", "add")).trim().toUpperCase();
            try {
                mode = VelocityMechanic.Mode.valueOf(modeStr);
            } catch (IllegalArgumentException ignored) {}
        }

        return new VelocityMechanic(x, y, z, force, mode, directional, propel);
    }
}
