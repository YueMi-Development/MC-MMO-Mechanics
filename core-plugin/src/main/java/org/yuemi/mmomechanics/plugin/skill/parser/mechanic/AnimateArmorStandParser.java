package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.AnimateArmorStandMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class AnimateArmorStandParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String limb = options.getOrDefault("limb", "head");

        double rotX = 0;
        double rotY = 0;
        double rotZ = 0;

        String rotStr = options.getOrDefault("rotation", options.getOrDefault("rot", ""));
        if (!rotStr.isEmpty()) {
            String[] parts = rotStr.split(",");
            if (parts.length == 3) {
                try {
                    rotX = Math.toRadians(Double.parseDouble(parts[0].trim()));
                    rotY = Math.toRadians(Double.parseDouble(parts[1].trim()));
                    rotZ = Math.toRadians(Double.parseDouble(parts[2].trim()));
                } catch (NumberFormatException ignored) {}
            }
        }

        int duration = 20;
        try {
            if (options.containsKey("duration")) duration = Integer.parseInt(options.get("duration"));
            else if (options.containsKey("ticks")) duration = Integer.parseInt(options.get("ticks"));
            else if (options.containsKey("t")) duration = Integer.parseInt(options.get("t"));
        } catch (NumberFormatException ignored) {}

        return new AnimateArmorStandMechanic(limb, rotX, rotY, rotZ, duration);
    }
}
