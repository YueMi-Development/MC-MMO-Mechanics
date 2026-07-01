package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.EscapeLocationTargeter;

public final class EscapeLocationParser {
    public @Nullable Targeter parse(@Nullable String name) {
        return new EscapeLocationTargeter();
    }
}
