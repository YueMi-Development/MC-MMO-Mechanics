package org.yuemi.mmomechanics.api.skill;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Represents a meta-skill consisting of multiple sequenced mechanics.
 */
public interface Metaskill extends Mechanic {
    /**
     * Gets the name of the skill.
     *
     * @return the skill name
     */
    @NotNull String getName();
    
    /**
     * Gets the collection of mechanic wrappers that define the steps of this skill.
     *
     * @return collection of mechanic wrappers
     */
    @NotNull Collection<MechanicWrapper> getMechanics();

    /**
     * Gets the collection of registered trigger event IDs that can execute this skill.
     *
     * @return collection of trigger strings
     */
    default @NotNull Collection<String> getTriggers() {
        return java.util.Collections.emptyList();
    }

    /**
     * Gets the binding configuration defining which entities possess this skill.
     *
     * @return the binding config, or null if not set
     */
    default @org.jetbrains.annotations.Nullable BindConfig getBindConfig() {
        return null;
    }

    @Override
    default void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        org.yuemi.mmomechanics.api.MmoMechanicsApi api = org.bukkit.Bukkit.getServicesManager().load(org.yuemi.mmomechanics.api.MmoMechanicsApi.class);
        if (api != null) {
            api.getSkillExecutor().run(context, targets, getMechanics());
        }
    }
}
