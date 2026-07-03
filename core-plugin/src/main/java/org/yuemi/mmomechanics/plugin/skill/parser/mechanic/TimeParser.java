package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.location.TimeMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class TimeParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String valStr = options.getOrDefault("time", options.getOrDefault("t", options.getOrDefault("value", options.getOrDefault("v", "1000")))).trim().toLowerCase();

        long timeValue = 1000;
        try {
            timeValue = Long.parseLong(valStr);
        } catch (NumberFormatException ignored) {
            switch (valStr) {
                case "day", "sunrise" -> timeValue = 1000;
                case "noon" -> timeValue = 6000;
                case "night", "sunset" -> timeValue = 13000;
                case "midnight" -> timeValue = 18000;
            }
        }

        boolean relative = false;
        if (options.containsKey("relative")) relative = Boolean.parseBoolean(options.get("relative"));
        else if (options.containsKey("r")) relative = Boolean.parseBoolean(options.get("r"));

        return new TimeMechanic(timeValue, relative);
    }
}
