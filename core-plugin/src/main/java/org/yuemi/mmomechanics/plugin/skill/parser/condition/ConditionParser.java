package org.yuemi.mmomechanics.plugin.skill.parser.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.condition.Condition;

public final class ConditionParser {

    public static @NotNull Condition parse(@Nullable String name) {
        return (context, target) -> true;
    }
}
