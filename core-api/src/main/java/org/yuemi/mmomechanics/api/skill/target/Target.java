package org.yuemi.mmomechanics.api.skill.target;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface Target permits EntityTarget, LocationTarget {
    @NotNull Location getLocation();
    
    @Nullable Entity getAsEntity();
    
    default boolean isEntity() {
        return getAsEntity() != null;
    }
}
