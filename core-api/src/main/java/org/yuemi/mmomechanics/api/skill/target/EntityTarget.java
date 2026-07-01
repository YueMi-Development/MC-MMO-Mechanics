package org.yuemi.mmomechanics.api.skill.target;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EntityTarget implements Target {
    private final Entity entity;

    public EntityTarget(@NotNull Entity entity) {
        this.entity = Objects.requireNonNull(entity, "Entity cannot be null");
    }

    @Override
    public @NotNull Location getLocation() {
        return entity.getLocation();
    }

    @Override
    public @NotNull Entity getAsEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityTarget that = (EntityTarget) o;
        return entity.equals(that.entity);
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }

    @Override
    public String toString() {
        return "EntityTarget{entity=" + entity.getUniqueId() + ", type=" + entity.getType() + "}";
    }
}
