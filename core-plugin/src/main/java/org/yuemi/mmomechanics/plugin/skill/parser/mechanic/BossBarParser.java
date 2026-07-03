package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.BossBarMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class BossBarParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        BossBarMechanic.Action action = BossBarMechanic.Action.CREATE;
        if (lower.startsWith("barremove")) {
            action = BossBarMechanic.Action.REMOVE;
        } else if (lower.startsWith("barset")) {
            action = BossBarMechanic.Action.SET;
        }

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String id = options.getOrDefault("id", options.getOrDefault("barid", "default"));
        String title = options.getOrDefault("title", options.getOrDefault("name", options.getOrDefault("t", "")));

        float progress = 1.0f;
        try {
            if (options.containsKey("progress")) progress = Float.parseFloat(options.get("progress"));
            else if (options.containsKey("value")) progress = Float.parseFloat(options.get("value"));
            else if (options.containsKey("p")) progress = Float.parseFloat(options.get("p"));
            else if (options.containsKey("v")) progress = Float.parseFloat(options.get("v"));
        } catch (NumberFormatException ignored) {}

        BossBar.Color color = BossBar.Color.PINK;
        String colorStr = options.getOrDefault("color", options.getOrDefault("c", "pink")).toUpperCase();
        try {
            color = BossBar.Color.valueOf(colorStr);
        } catch (IllegalArgumentException ignored) {}

        BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;
        String overlayStr = options.getOrDefault("overlay", options.getOrDefault("style", options.getOrDefault("s", "progress"))).toUpperCase();
        try {
            overlay = BossBar.Overlay.valueOf(overlayStr);
        } catch (IllegalArgumentException ignored) {}

        return new BossBarMechanic(action, id, title, progress, color, overlay);
    }
}
