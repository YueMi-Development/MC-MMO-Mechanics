package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.message.SendActionMessageMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SendActionMessageParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String msg = options.getOrDefault("msg", options.getOrDefault("message", options.getOrDefault("m", "")));

        return new SendActionMessageMechanic(msg);
    }
}
