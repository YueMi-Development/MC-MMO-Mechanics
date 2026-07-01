package org.yuemi.mmomechanics.api.skill.target;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class LocationTarget implements Target {
    private final Location location;

    public LocationTarget(@NotNull Location location) {
        this.location = Objects.requireNonNull(location, "Location cannot be null");
    }

    @Override
    public @NotNull Location getLocation() {
        return location.clone();
    }

    @Override
    public @Nullable Entity getAsEntity() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationTarget that = (LocationTarget) o;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    @Override
    public String toString() {
        return "LocationTarget{location=" + location + "}";
    }
}
