package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.PoseArmorStandMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class PoseArmorStandParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        EulerAngle head = parseAngle(options.get("head"));
        EulerAngle body = parseAngle(options.get("body"));
        EulerAngle leftArm = parseAngle(options.getOrDefault("leftarm", options.get("larm")));
        EulerAngle rightArm = parseAngle(options.getOrDefault("rightarm", options.get("rarm")));
        EulerAngle leftLeg = parseAngle(options.getOrDefault("leftleg", options.get("lleg")));
        EulerAngle rightLeg = parseAngle(options.getOrDefault("rightleg", options.get("rleg")));

        return new PoseArmorStandMechanic(head, body, leftArm, rightArm, leftLeg, rightLeg);
    }

    private @Nullable EulerAngle parseAngle(@Nullable String str) {
        if (str == null || str.isEmpty()) return null;
        String[] parts = str.split(",");
        if (parts.length == 3) {
            try {
                double x = Math.toRadians(Double.parseDouble(parts[0].trim()));
                double y = Math.toRadians(Double.parseDouble(parts[1].trim()));
                double z = Math.toRadians(Double.parseDouble(parts[2].trim()));
                return new EulerAngle(x, y, z);
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}
