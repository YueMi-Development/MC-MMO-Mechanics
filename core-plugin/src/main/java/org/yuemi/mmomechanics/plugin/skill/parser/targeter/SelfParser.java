package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.SelfTargeter;

public final class SelfParser {
    public @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.equalsIgnoreCase("self")) {
            return new SelfTargeter();
        }
        return null;
    }
}
