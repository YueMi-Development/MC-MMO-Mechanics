package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.CoordinateLocationTargeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.SelfLocationTargeter;

public final class LocationParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        if (clean.startsWith("origin") || clean.startsWith("source")) {
            return new SelfLocationTargeter();
        }

        String xExpr = "0";
        String yExpr = "0";
        String zExpr = "0";

        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    switch (key) {
                        case "x" -> xExpr = val;
                        case "y" -> yExpr = val;
                        case "z" -> zExpr = val;
                    }
                }
            }
        }
        return new CoordinateLocationTargeter(xExpr, yExpr, zExpr);
    }
}
