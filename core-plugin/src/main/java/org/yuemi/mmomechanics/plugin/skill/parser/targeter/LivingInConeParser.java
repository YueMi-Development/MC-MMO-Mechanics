package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.LivingInConeTargeter;

public final class LivingInConeParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String angle = "45";
        String length = "10";
        String rotation = "0";

        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    switch (key) {
                        case "angle" -> angle = val;
                        case "length" -> length = val;
                        case "rotation" -> rotation = val;
                    }
                }
            }
        }
        return new LivingInConeTargeter(angle, length, rotation);
    }
}
