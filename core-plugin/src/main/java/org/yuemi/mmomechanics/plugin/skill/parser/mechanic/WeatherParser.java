package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.location.WeatherMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class WeatherParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String weatherStr = options.getOrDefault("weather", options.getOrDefault("w", "clear")).trim().toUpperCase();
        
        WeatherMechanic.Type type = WeatherMechanic.Type.CLEAR;
        try {
            type = WeatherMechanic.Type.valueOf(weatherStr);
        } catch (IllegalArgumentException ignored) {
            if (weatherStr.contains("SUN") || weatherStr.contains("DAY")) {
                type = WeatherMechanic.Type.CLEAR;
            } else if (weatherStr.contains("STORM") || weatherStr.contains("THUNDER")) {
                type = WeatherMechanic.Type.STORM;
            } else if (weatherStr.contains("RAIN")) {
                type = WeatherMechanic.Type.RAIN;
            }
        }

        int duration = 6000;
        try {
            if (options.containsKey("duration")) duration = Integer.parseInt(options.get("duration"));
            else if (options.containsKey("ticks")) duration = Integer.parseInt(options.get("ticks"));
            else if (options.containsKey("t")) duration = Integer.parseInt(options.get("t"));
        } catch (NumberFormatException ignored) {}

        return new WeatherMechanic(type, duration);
    }
}
