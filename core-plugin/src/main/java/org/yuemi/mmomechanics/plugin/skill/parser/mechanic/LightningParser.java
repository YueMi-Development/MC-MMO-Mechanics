package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.LightningStrikeMechanic;

public final class LightningParser {
    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.equalsIgnoreCase("lightning")) {
            return new LightningStrikeMechanic();
        }
        return null;
    }
}
