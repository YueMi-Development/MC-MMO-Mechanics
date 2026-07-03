package org.yuemi.mmomechanics.api.skill;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;

/**
 * Configuration detailing which entities a skill automatically binds to.
 */
public interface BindConfig {
    @NotNull Collection<String> getEntityTypes();
    boolean isGlobal();
    default @NotNull Collection<String> getUuids() {
        return java.util.Collections.emptyList();
    }
}
