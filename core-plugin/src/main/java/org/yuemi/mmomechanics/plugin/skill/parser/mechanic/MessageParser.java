package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.message.JsonMessageMechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.message.MessageMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class MessageParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String msg = options.getOrDefault("msg", options.getOrDefault("message", ""));

        if (lower.startsWith("jsonmessage")) {
            return new JsonMessageMechanic(msg);
        } else {
            return new MessageMechanic(msg);
        }
    }
}
