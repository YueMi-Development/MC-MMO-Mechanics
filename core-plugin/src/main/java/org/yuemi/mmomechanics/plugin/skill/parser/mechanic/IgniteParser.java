package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general.IgniteMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class IgniteParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean extinguish = lower.startsWith("extinguish");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        int ticks = 100;
        try {
            if (options.containsKey("duration")) ticks = Integer.parseInt(options.get("duration"));
            else if (options.containsKey("ticks")) ticks = Integer.parseInt(options.get("ticks"));
            else if (options.containsKey("t")) ticks = Integer.parseInt(options.get("t"));
        } catch (NumberFormatException ignored) {}

        return new IgniteMechanic(ticks, extinguish);
    }
}
