package org.yuemi.mmomechanics.api.skill;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Metaskill extends Mechanic {
    @NotNull String getName();
    
    @NotNull Collection<MechanicWrapper> getMechanics();

    @Override
    default void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        org.yuemi.mmomechanics.api.MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(org.yuemi.mmomechanics.api.MmoMechanicsApi.class);
        if (api != null) {
            api.getSkillExecutor().run(context, targets, getMechanics());
        }
    }
}
