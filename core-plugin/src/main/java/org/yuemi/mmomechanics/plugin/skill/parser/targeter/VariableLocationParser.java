package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.VariableLocationTargeter;

public final class VariableLocationParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String varName = "";
        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim().toLowerCase();
                    String val = parts[1].trim();
                    if (key.equals("var") || key.equals("variable") || key.equals("v")) {
                        varName = val;
                    }
                }
            }
        }
        return new VariableLocationTargeter(varName);
    }
}
