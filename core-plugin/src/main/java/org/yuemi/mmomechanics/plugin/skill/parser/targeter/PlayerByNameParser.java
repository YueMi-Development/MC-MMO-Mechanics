package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.PlayerByNameTargeter;

public final class PlayerByNameParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String targetName = "";
        if (clean.contains("{") && clean.contains("}")) {
            String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
            for (String pair : options.split(",")) {
                String[] parts = pair.split("=");
                if (parts.length == 2 && parts[0].trim().equalsIgnoreCase("name")) {
                    targetName = parts[1].trim();
                }
            }
        }
        return new PlayerByNameTargeter(targetName);
    }
}
