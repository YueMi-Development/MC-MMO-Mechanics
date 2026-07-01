package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.ForwardWallTargeter;

public final class ForwardWallParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String width = "3.0";
        String height = "3.0";
        String distance = "5.0";

        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    switch (key) {
                        case "width", "w" -> width = val;
                        case "height", "h" -> height = val;
                        case "distance", "d" -> distance = val;
                    }
                }
            }
        }
        return new ForwardWallTargeter(width, height, distance);
    }
}
