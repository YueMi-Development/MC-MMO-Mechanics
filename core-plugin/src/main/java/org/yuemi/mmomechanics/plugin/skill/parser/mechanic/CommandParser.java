package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.location.CommandMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class CommandParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String cmd = options.getOrDefault("command", options.getOrDefault("cmd", options.getOrDefault("c", "")));

        CommandMechanic.SenderType senderType = CommandMechanic.SenderType.CONSOLE;
        String asStr = options.getOrDefault("as", "console").trim().toUpperCase();
        try {
            senderType = CommandMechanic.SenderType.valueOf(asStr);
        } catch (IllegalArgumentException ignored) {}

        return new CommandMechanic(cmd, senderType);
    }
}
