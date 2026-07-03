package org.yuemi.mmomechanics.api.skill.mechanic;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;

/**
 * A wrapper record holding a mechanic, its targeter, and conditional checks.
 *
 * @param mechanic the mechanic action to run
 * @param targeter the targeter determining where to apply the mechanic
 * @param conditions condition checks that targets must pass
 */
public record MechanicWrapper(
        @NotNull Mechanic mechanic,
        @NotNull Targeter targeter,
        @NotNull Collection<Condition> conditions
) {}
