package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.ToggleSittingMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class ToggleSittingParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        Boolean forceState = null;
        if (lower.startsWith("wolfsit")) {
            forceState = true;
        } else {
            Map<String, String> options = ParserUtils.parseOptions(clean);
            String sitVal = options.getOrDefault("sitting", options.getOrDefault("sit", options.getOrDefault("s", ""))).trim();
            if (!sitVal.isEmpty()) {
                forceState = Boolean.parseBoolean(sitVal);
            }
        }

        return new ToggleSittingMechanic(forceState);
    }
}
