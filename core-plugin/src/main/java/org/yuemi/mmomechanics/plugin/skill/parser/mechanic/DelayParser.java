package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.DelayMechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;

public final class DelayParser {
    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.startsWith("delay")) {
            String expression = "20";
            boolean isSeconds = false;
            if (clean.contains("{") && clean.contains("}")) {
                String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
                for (String pair : options.split(",")) {
                    String[] parts = pair.split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String val = parts[1].trim();
                        if (key.equalsIgnoreCase("ticks")) {
                            expression = val;
                            isSeconds = false;
                        } else if (key.equalsIgnoreCase("seconds")) {
                            expression = val;
                            isSeconds = true;
                        }
                    }
                }
            }
            return new DelayMechanic(expression, isSeconds);
        }
        return null;
    }
}
