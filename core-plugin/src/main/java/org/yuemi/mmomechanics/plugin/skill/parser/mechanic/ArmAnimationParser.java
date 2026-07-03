package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.ArmAnimationMechanic;

public final class ArmAnimationParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim().toLowerCase();
        boolean offhand = clean.startsWith("swingoffhand");
        return new ArmAnimationMechanic(offhand);
    }
}
