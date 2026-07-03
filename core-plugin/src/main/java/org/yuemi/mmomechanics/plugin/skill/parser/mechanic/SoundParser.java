package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.location.SoundMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SoundParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean stop = lower.startsWith("stopsound");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String sound = options.getOrDefault("sound", options.getOrDefault("s", ""));

        float volume = 1.0f;
        try {
            if (options.containsKey("volume")) volume = Float.parseFloat(options.get("volume"));
            else if (options.containsKey("v")) volume = Float.parseFloat(options.get("v"));
        } catch (NumberFormatException ignored) {}

        float pitch = 1.0f;
        try {
            if (options.containsKey("pitch")) pitch = Float.parseFloat(options.get("pitch"));
            else if (options.containsKey("p")) pitch = Float.parseFloat(options.get("p"));
        } catch (NumberFormatException ignored) {}

        return new SoundMechanic(sound, volume, pitch, stop);
    }
}
