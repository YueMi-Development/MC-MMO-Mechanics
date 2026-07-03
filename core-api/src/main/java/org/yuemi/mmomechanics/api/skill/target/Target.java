package org.yuemi.mmomechanics.api.skill.target;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a target location or entity within a skill execution context.
 */
public sealed interface Target permits EntityTarget, LocationTarget {
    /**
     * Gets the location of this target.
     *
     * @return the target location
     */
    @NotNull Location getLocation();
    
    /**
     * Gets this target as a Bukkit Entity, if applicable.
     *
     * @return the target entity, or null if it represents a location
     */
    @Nullable Entity getAsEntity();
    
    /**
     * Checks if this target is an entity.
     *
     * @return true if the target is an entity, false otherwise
     */
    default boolean isEntity() {
        return getAsEntity() != null;
    }
}
