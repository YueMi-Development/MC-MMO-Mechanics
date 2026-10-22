package org.yuemi.mmomechanics.api.skill.context;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A point-in-time capture of the caster's state, taken on the caster's own thread.
 *
 * <p>On Folia a mechanic may run on the region thread of a target that lives in another
 * region. Reading the caster's live position from that thread is a cross-region access,
 * so the caster's position, direction and base damage are captured up front instead.
 *
 * @param location the caster's position when the snapshot was taken
 * @param direction the caster's facing direction when the snapshot was taken
 * @param baseAttackDamage the caster's base attack damage, or null when not applicable
 */
public record CasterSnapshot(
        @NotNull Location location,
        @NotNull Vector direction,
        @Nullable Double baseAttackDamage
) {

    /**
     * Returns the horizontal distance between this snapshot and the given location.
     *
     * @param other the location to measure against
     * @return the distance in blocks
     */
    public double distanceTo(@NotNull Location other) {
        return location.distance(other);
    }

}
