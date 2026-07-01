package org.yuemi.mmomechanics.plugin.skill.parser.condition;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.plugin.skill.condition.IsNightCondition;

public final class IsNightParser {
    public @Nullable Condition parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.equalsIgnoreCase("isnight")) {
            return new IsNightCondition();
        }
        return null;
    }
}
