package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.SetBlockOpenMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SetBlockOpenParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        boolean open = true;
        if (options.containsKey("open")) open = Boolean.parseBoolean(options.get("open"));
        else if (options.containsKey("o")) open = Boolean.parseBoolean(options.get("o"));

        return new SetBlockOpenMechanic(open);
    }
}
