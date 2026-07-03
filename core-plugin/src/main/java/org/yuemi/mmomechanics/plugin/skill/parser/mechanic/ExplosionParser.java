package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.location.ExplosionMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class ExplosionParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean fake = lower.startsWith("fakeexplosion");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        float yield = 2.0f;
        try {
            if (options.containsKey("yield")) yield = Float.parseFloat(options.get("yield"));
            else if (options.containsKey("y")) yield = Float.parseFloat(options.get("y"));
            else if (options.containsKey("force")) yield = Float.parseFloat(options.get("force"));
            else if (options.containsKey("f")) yield = Float.parseFloat(options.get("f"));
        } catch (NumberFormatException ignored) {}

        boolean fire = false;
        if (options.containsKey("fire")) fire = Boolean.parseBoolean(options.get("fire"));

        boolean breakBlocks = true;
        if (options.containsKey("breakblocks")) breakBlocks = Boolean.parseBoolean(options.get("breakblocks"));
        else if (options.containsKey("break")) breakBlocks = Boolean.parseBoolean(options.get("break"));

        return new ExplosionMechanic(yield, fire, breakBlocks, fake);
    }
}
