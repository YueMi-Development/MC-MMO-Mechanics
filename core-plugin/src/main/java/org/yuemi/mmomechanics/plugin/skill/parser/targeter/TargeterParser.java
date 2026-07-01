package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

public final class TargeterParser {

    public static @Nullable Targeter parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.startsWith("self")) {
            return new SelfParser().parse(clean);
        } else if (clean.startsWith("near")) {
            return new NearParser().parse(clean);
        }
        return null;
    }
}
