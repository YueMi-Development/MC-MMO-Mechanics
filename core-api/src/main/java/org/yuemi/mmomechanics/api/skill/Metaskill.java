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
        for (MechanicWrapper wrapper : getMechanics()) {
            Collection<Target> stepTargets = wrapper.targeter().getTargets(context);
            
            Collection<Target> filtered = stepTargets.stream()
                    .filter(target -> wrapper.conditions().stream().allMatch(cond -> cond.test(context, target)))
                    .collect(Collectors.toList());
            
            if (!filtered.isEmpty()) {
                wrapper.mechanic().execute(context, filtered);
            }
        }
    }
}
