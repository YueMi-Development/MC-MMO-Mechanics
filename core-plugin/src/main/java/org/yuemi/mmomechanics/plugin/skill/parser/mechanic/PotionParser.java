package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general.PotionMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class PotionParser {

    @SuppressWarnings("deprecation")
    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean clear = lower.startsWith("potionclear");
        boolean clearAll = false;

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String typeStr = options.getOrDefault("type", options.getOrDefault("t", ""));
        
        PotionEffectType effectType = null;
        if (!typeStr.isEmpty()) {
            try {
                effectType = PotionEffectType.getByName(typeStr.toUpperCase());
            } catch (Throwable t) {
                try {
                    org.bukkit.NamespacedKey key = org.bukkit.NamespacedKey.fromString(typeStr.toLowerCase());
                    if (key != null) {
                        effectType = PotionEffectType.getByKey(key);
                    }
                } catch (Throwable ignored) {}
            }
        }

        if (clear && typeStr.isEmpty()) {
            clearAll = true;
        }

        int duration = 200;
        try {
            if (options.containsKey("duration")) duration = Integer.parseInt(options.get("duration"));
            else if (options.containsKey("d")) duration = Integer.parseInt(options.get("d"));
        } catch (NumberFormatException ignored) {}

        int amplifier = 0;
        try {
            if (options.containsKey("level")) amplifier = Integer.parseInt(options.get("level")) - 1;
            else if (options.containsKey("amplifier")) amplifier = Integer.parseInt(options.get("amplifier"));
            else if (options.containsKey("l")) amplifier = Integer.parseInt(options.get("l")) - 1;
            else if (options.containsKey("a")) amplifier = Integer.parseInt(options.get("a"));
        } catch (NumberFormatException ignored) {}
        if (amplifier < 0) amplifier = 0;

        boolean ambient = true;
        if (options.containsKey("ambient")) ambient = Boolean.parseBoolean(options.get("ambient"));

        boolean particles = true;
        if (options.containsKey("particles")) particles = Boolean.parseBoolean(options.get("particles"));

        boolean icon = true;
        if (options.containsKey("icon")) icon = Boolean.parseBoolean(options.get("icon"));

        return new PotionMechanic(effectType, duration, amplifier, ambient, particles, icon, clear, clearAll);
    }
}
