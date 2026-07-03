package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.SetNameMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SetNameParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String displayName = options.getOrDefault("name", options.getOrDefault("n", options.getOrDefault("display", "")));

        boolean visible = true;
        if (options.containsKey("visible")) visible = Boolean.parseBoolean(options.get("visible"));
        else if (options.containsKey("v")) visible = Boolean.parseBoolean(options.get("v"));
        else if (options.containsKey("show")) visible = Boolean.parseBoolean(options.get("show"));

        return new SetNameMechanic(displayName, visible);
    }
}
