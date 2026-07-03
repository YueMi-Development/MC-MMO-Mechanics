package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.PushBlockMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class PushBlockParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String direction = options.getOrDefault("direction", options.getOrDefault("dir", options.getOrDefault("d", "caster")));
        
        int distance = 1;
        try {
            if (options.containsKey("distance")) distance = Integer.parseInt(options.get("distance"));
            else if (options.containsKey("dist")) distance = Integer.parseInt(options.get("dist"));
        } catch (NumberFormatException ignored) {}

        return new PushBlockMechanic(direction, distance);
    }
}
