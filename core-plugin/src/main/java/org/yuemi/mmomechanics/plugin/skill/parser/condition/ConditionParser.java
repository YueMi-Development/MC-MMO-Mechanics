package org.yuemi.mmomechanics.plugin.skill.parser.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.condition.Condition;

public final class ConditionParser {

    public static @NotNull Condition parse(@Nullable String name) {
        if (name == null) {
            return (context, target) -> true;
        }
        String clean = name.trim().toLowerCase();
        
        Condition parsed = null;
        if (clean.startsWith("isday")) {
            parsed = new IsDayParser().parse(clean);
        } else if (clean.startsWith("isnight")) {
            parsed = new IsNightParser().parse(clean);
        }
        
        if (parsed != null) {
            return parsed;
        }
        
        return (context, target) -> true;
    }
}
