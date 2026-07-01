package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;

public record MechanicWrapper(
        @NotNull Mechanic mechanic,
        @NotNull Targeter targeter,
        @NotNull Collection<Condition> conditions
) {}
