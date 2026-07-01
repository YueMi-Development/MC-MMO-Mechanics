package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class VariableLocationTargeter implements Targeter {

    private final String varName;

    public VariableLocationTargeter(@NotNull String varName) {
        this.varName = varName;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        return context.getVariable(varName)
                .filter(val -> val instanceof Location)
                .map(val -> (Location) val)
                .map(loc -> Collections.singletonList((Target) new LocationTarget(loc)))
                .orElse(Collections.emptyList());
    }
}
