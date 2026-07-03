package org.yuemi.mmomechanics.plugin.skill.parser.targeter;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.entity.multi.TrackedPlayersTargeter;

public final class TrackedPlayersParser {
    public @Nullable Targeter parse(@Nullable String name) {
        return new TrackedPlayersTargeter();
    }
}
