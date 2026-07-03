package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.GameMode;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.player.SetGameModeMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SetGameModeParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String modeStr = options.getOrDefault("gamemode", options.getOrDefault("mode", options.getOrDefault("m", options.getOrDefault("g", "survival")))).trim().toUpperCase();

        GameMode mode = GameMode.SURVIVAL;
        try {
            mode = GameMode.valueOf(modeStr);
        } catch (IllegalArgumentException ignored) {
            switch (modeStr) {
                case "0", "S", "SURV" -> mode = GameMode.SURVIVAL;
                case "1", "C", "CREAT" -> mode = GameMode.CREATIVE;
                case "2", "A", "ADVENT" -> mode = GameMode.ADVENTURE;
                case "3", "SP", "SPEC", "SPECT" -> mode = GameMode.SPECTATOR;
            }
        }

        return new SetGameModeMechanic(mode);
    }
}
