package org.yuemi.mmomechanics.api.skill;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;

/**
 * Configuration detailing which entities a skill automatically binds to.
 */
public interface BindConfig {
    /**
     * Gets the collection of entity types (e.g. "ZOMBIE") that this skill automatically binds to.
     *
     * @return collection of entity type name strings
     */
    @NotNull Collection<String> getEntityTypes();

    /**
     * Checks if this skill is bound globally to all entities.
     *
     * @return true if global, false otherwise
     */
    boolean isGlobal();

    /**
     * Gets the collection of specific entity UUID strings that this skill binds to.
     *
     * @return collection of UUID strings
     */
    default @NotNull Collection<String> getUuids() {
        return java.util.Collections.emptyList();
    }
}
