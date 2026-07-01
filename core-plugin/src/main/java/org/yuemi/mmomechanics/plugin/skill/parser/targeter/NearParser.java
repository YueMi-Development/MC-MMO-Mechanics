package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.NearTargeter;

public final class NearParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String cleanName = name.trim().toLowerCase();
        if (cleanName.startsWith("near")) {
            String radiusExpr = "5.0";
            if (cleanName.contains("{") && cleanName.contains("}")) {
                String options = cleanName.substring(cleanName.indexOf("{") + 1, cleanName.indexOf("}"));
                for (String pair : options.split(",")) {
                    String[] parts = pair.split("=");
                    if (parts.length == 2 && parts[0].trim().equalsIgnoreCase("r")) {
                        radiusExpr = parts[1].trim();
                    }
                }
            }
            return new NearTargeter(radiusExpr);
        }
        return null;
    }
}
