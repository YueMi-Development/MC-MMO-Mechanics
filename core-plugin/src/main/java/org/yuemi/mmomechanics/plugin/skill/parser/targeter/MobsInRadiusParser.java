package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.entity.multi.MobsInRadiusTargeter;

public final class MobsInRadiusParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String radius = "5.0";
        String types = "";
        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    if (key.equals("r") || key.equals("radius")) {
                        radius = val;
                    } else if (key.equals("type") || key.equals("types") || key.equals("m")) {
                        types = val;
                    }
                }
            }
        }
        return new MobsInRadiusTargeter(radius, types);
    }
}
