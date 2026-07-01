package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.ItemsInRadiusTargeter;

public final class ItemsInRadiusParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String radius = "5.0";
        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2 && (parts[0].trim().equalsIgnoreCase("r") || parts[0].trim().equalsIgnoreCase("radius"))) {
                    radius = parts[1].trim();
                }
            }
        }
        return new ItemsInRadiusTargeter(radius);
    }
}
