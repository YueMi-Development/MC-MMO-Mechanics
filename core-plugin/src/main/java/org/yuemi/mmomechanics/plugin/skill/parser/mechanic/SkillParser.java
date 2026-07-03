package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.system.SkillMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SkillParser {
    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        String skill = options.getOrDefault("skill", options.getOrDefault("s", options.getOrDefault("name", options.getOrDefault("n", ""))));

        if (!skill.isEmpty()) {
            return new SkillMechanic(skill);
        }
        return null;
    }
}
