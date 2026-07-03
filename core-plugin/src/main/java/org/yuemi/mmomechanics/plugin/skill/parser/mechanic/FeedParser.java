package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.player.FeedMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class FeedParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        int amount = 20;
        try {
            if (options.containsKey("amount")) amount = Integer.parseInt(options.get("amount"));
            else if (options.containsKey("value")) amount = Integer.parseInt(options.get("value"));
            else if (options.containsKey("a")) amount = Integer.parseInt(options.get("a"));
            else if (options.containsKey("v")) amount = Integer.parseInt(options.get("v"));
        } catch (NumberFormatException ignored) {}

        float saturation = 0.0f;
        try {
            if (options.containsKey("saturation")) saturation = Float.parseFloat(options.get("saturation"));
            else if (options.containsKey("sat")) saturation = Float.parseFloat(options.get("sat"));
            else if (options.containsKey("s")) saturation = Float.parseFloat(options.get("s"));
        } catch (NumberFormatException ignored) {}

        return new FeedMechanic(amount, saturation);
    }
}
