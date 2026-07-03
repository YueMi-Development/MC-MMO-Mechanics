package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.BlockDestabilizeMechanic;

public final class BlockDestabilizeParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        return new BlockDestabilizeMechanic();
    }
}
