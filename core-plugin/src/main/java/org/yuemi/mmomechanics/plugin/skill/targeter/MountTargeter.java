package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class MountTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        Entity casterEntity = context.getCaster().getAsEntity();
        if (casterEntity != null) {
            Entity vehicle = casterEntity.getVehicle();
            if (vehicle != null) {
                return Collections.singletonList(new EntityTarget(vehicle));
            }
        }
        return Collections.emptyList();
    }
}
