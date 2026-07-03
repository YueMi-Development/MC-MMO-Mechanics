package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.location.multi.RandomLocationsTargeter;

public final class RandomLocationsParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String radius = "5.0";
        String amount = "3";

        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    if (key.equals("radius") || key.equals("r")) {
                        radius = val;
                    } else if (key.equals("amount") || key.equals("a")) {
                        amount = val;
                    }
                }
            }
        }
        return new RandomLocationsTargeter(radius, amount);
    }
}
