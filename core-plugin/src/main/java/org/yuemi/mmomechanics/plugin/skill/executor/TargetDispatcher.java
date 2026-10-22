package org.yuemi.mmomechanics.plugin.skill.executor;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.folia.FoliaSchedulers;
import org.yuemi.libs.api.platform.PlatformProvider;
import org.yuemi.mmomechanics.api.skill.target.Target;

/**
 * Routes mechanic work to the thread that owns the target.
 *
 * <p>On Folia the world is split into independently ticked regions, so mutating an entity
 * or block owned by another region throws. Each target is therefore dispatched to the
 * region that owns it: entity targets through the entity's scheduler (which follows the
 * entity if it moves), location targets through the region scheduler.
 *
 * <p>On Paper a single thread owns the world, so work runs inline. The Paper fallback in
 * {@link FoliaSchedulers} defers to the next tick, which would add latency and change
 * effect ordering on every Paper server.
 */
public final class TargetDispatcher {

    private static volatile TargetDispatcher instance;

    private final JavaPlugin plugin;

    public TargetDispatcher(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the shared dispatcher.
     *
     * @return the dispatcher
     * @throws IllegalStateException if it was not initialized yet
     */
    public static @NotNull TargetDispatcher getInstance() {
        TargetDispatcher result = instance;
        if (result == null) {
            throw new IllegalStateException("TargetDispatcher not initialized");
        }
        return result;
    }

    /**
     * Sets the shared dispatcher.
     *
     * @param dispatcher the dispatcher
     */
    public static void setInstance(@NotNull TargetDispatcher dispatcher) {
        instance = dispatcher;
    }

    /**
     * Checks whether the server runs on Folia, where targets may live in foreign regions.
     *
     * @return true when running on Folia
     */
    public boolean isFolia() {
        try {
            return PlatformProvider.isFolia();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Runs the given action on the thread that owns the target.
     *
     * <p>On Paper the action runs inline. On Folia it is dispatched to the owning region.
     * Targets whose world is gone are skipped, since no region can own them.
     *
     * @param target the target to act on
     * @param action the work to perform
     * @return true when the action was scheduled or run, false when it was skipped
     */
    public boolean dispatch(@NotNull Target target, @NotNull Runnable action) {
        if (!isFolia()) {
            action.run();
            return true;
        }

        Entity entity = target.getAsEntity();
        if (entity != null) {
            // The entity scheduler drops the task when the entity was retired in the
            // meantime, which the boolean return reports.
            return entity.getScheduler().execute(plugin, action, () -> { }, 0L);
        }

        Location location = target.getLocation();
        if (location.getWorld() == null) {
            return false;
        }
        FoliaSchedulers.executeRegion(plugin, location, action);
        return true;
    }

    /**
     * Checks whether a target can still be acted on.
     *
     * <p>A target may have died or been removed between resolution and dispatch.
     *
     * @param target the target to check
     * @return true when the target is still usable
     */
    public boolean isUsable(@NotNull Target target) {
        Entity entity = target.getAsEntity();
        if (entity != null) {
            return entity.isValid() && !entity.isDead();
        }
        return target.getLocation().getWorld() != null;
    }

    /**
     * Runs the given action on the thread that owns the caster.
     *
     * <p>Use this for work that mutates the caster itself, such as granting items to the
     * caster's inventory, when that work was triggered from a target in another region.
     * On Paper the action runs inline.
     *
     * @param caster the caster entity
     * @param action the work to perform
     */
    public void dispatchToCaster(@NotNull Entity caster, @NotNull Runnable action) {
        if (!isFolia()) {
            action.run();
            return;
        }
        caster.getScheduler().execute(plugin, action, () -> { }, 0L);
    }

}
