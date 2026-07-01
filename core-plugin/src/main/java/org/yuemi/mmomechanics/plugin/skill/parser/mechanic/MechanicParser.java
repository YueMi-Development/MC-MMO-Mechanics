package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;

public final class MechanicParser {

    public static @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.startsWith("lightning")) {
            return new LightningParser().parse(clean);
        } else if (clean.startsWith("delay")) {
            return new DelayParser().parse(clean);
        }
        return null;
    }
}
