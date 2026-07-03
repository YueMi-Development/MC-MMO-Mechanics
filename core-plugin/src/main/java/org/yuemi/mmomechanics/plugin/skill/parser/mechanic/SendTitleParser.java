package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.message.SendTitleMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SendTitleParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String title = options.getOrDefault("title", options.getOrDefault("t", ""));
        String subtitle = options.getOrDefault("subtitle", options.getOrDefault("s", ""));

        int fadeIn = 10;
        int stay = 70;
        int fadeOut = 20;

        try {
            if (options.containsKey("fadein")) fadeIn = Integer.parseInt(options.get("fadein"));
            else if (options.containsKey("fi")) fadeIn = Integer.parseInt(options.get("fi"));
            else if (options.containsKey("fadeinticks")) fadeIn = Integer.parseInt(options.get("fadeinticks"));
        } catch (NumberFormatException ignored) {}

        try {
            if (options.containsKey("stay")) stay = Integer.parseInt(options.get("stay"));
            else if (options.containsKey("st")) stay = Integer.parseInt(options.get("st"));
            else if (options.containsKey("stayticks")) stay = Integer.parseInt(options.get("stayticks"));
        } catch (NumberFormatException ignored) {}

        try {
            if (options.containsKey("fadeout")) fadeOut = Integer.parseInt(options.get("fadeout"));
            else if (options.containsKey("fo")) fadeOut = Integer.parseInt(options.get("fo"));
            else if (options.containsKey("fadeoutticks")) fadeOut = Integer.parseInt(options.get("fadeoutticks"));
        } catch (NumberFormatException ignored) {}

        return new SendTitleMechanic(title, subtitle, fadeIn, stay, fadeOut);
    }
}
