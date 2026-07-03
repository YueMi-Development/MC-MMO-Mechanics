package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Sittable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Toggles or forces sitting states for sittable pets like dogs, cats, foxes, and parrots.
 */
public final class ToggleSittingMechanic implements Mechanic {
    private final Boolean forceState; // if null, toggle; otherwise, set to true/false

    public ToggleSittingMechanic(@Nullable Boolean forceState) {
        this.forceState = forceState;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity instanceof Sittable sittable) {
                if (forceState == null) {
                    sittable.setSitting(!sittable.isSitting());
                } else {
                    sittable.setSitting(forceState);
                }
            }
        }
    }
}

