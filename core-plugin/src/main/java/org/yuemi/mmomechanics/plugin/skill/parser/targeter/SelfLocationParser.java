package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.SelfLocationTargeter;

public final class SelfLocationParser {
    public @Nullable Targeter parse(@Nullable String name) {
        return new SelfLocationTargeter();
    }
}
