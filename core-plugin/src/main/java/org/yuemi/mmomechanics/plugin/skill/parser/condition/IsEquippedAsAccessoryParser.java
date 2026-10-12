package org.yuemi.mmomechanics.plugin.skill.parser.condition;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.condition.IsEquippedAsAccessoryCondition;

public final class IsEquippedAsAccessoryParser {

    public @Nullable Condition parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        if (clean.equals("isequippedasaccessory")) {
            return new IsEquippedAsAccessoryCondition();
        }
        return null;
    }
}
