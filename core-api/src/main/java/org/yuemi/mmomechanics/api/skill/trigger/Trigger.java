package org.yuemi.mmomechanics.api.skill.trigger;

import org.jetbrains.annotations.NotNull;

import org.yuemi.mmomechanics.api.skill.target.Target;
import java.util.Optional;

/**
 * Represents a trigger context associated with a skill cast.
 */
public interface Trigger {
    /**
     * Gets the unique ID of the trigger event type (e.g. "onAttack").
     *
     * @return the trigger ID
     */
    @NotNull String getId();

    /**
     * Gets the target entity or location that triggered this skill, if applicable.
     *
     * @return an Optional containing the trigger target, or empty if none
     */
    default @NotNull Optional<Target> getTriggerTarget() {
        return Optional.empty();
    }
}
