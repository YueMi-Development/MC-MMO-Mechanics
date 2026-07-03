package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.ToggleLeverMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class ToggleLeverParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        Boolean power = null;
        if (options.containsKey("power")) power = Boolean.parseBoolean(options.get("power"));
        else if (options.containsKey("powered")) power = Boolean.parseBoolean(options.get("powered"));
        else if (options.containsKey("p")) power = Boolean.parseBoolean(options.get("p"));

        int pulseTicks = 0;
        if (lower.startsWith("pushbutton")) {
            pulseTicks = 20; // Default button press duration
            power = true;
            try {
                if (options.containsKey("duration")) pulseTicks = Integer.parseInt(options.get("duration"));
                else if (options.containsKey("ticks")) pulseTicks = Integer.parseInt(options.get("ticks"));
                else if (options.containsKey("t")) pulseTicks = Integer.parseInt(options.get("t"));
            } catch (NumberFormatException ignored) {}
        }

        return new ToggleLeverMechanic(power, pulseTicks);
    }
}
