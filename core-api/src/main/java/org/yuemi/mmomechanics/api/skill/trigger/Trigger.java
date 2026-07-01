package org.yuemi.mmomechanics.api.skill.trigger;

import org.jetbrains.annotations.NotNull;

import org.yuemi.mmomechanics.api.skill.target.Target;
import java.util.Optional;

public interface Trigger {
    @NotNull String getId();

    default @NotNull Optional<Target> getTriggerTarget() {
        return Optional.empty();
    }
}
