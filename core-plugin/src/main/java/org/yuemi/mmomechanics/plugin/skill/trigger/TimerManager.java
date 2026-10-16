package org.yuemi.mmomechanics.plugin.skill.trigger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.folia.FoliaSchedulers;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Manages executing the 'onTimer' skills at specified intervals.
 *
 * <p>Each tracked entity gets its own repeating task on that entity's scheduler, so the
 * work always runs on the region thread owning the entity. This replaces an earlier
 * global per-tick task that scanned every loaded entity in every world, which is not
 * valid on Folia.
 */
public final class TimerManager {

    private final JavaPlugin plugin;
    private final TriggerBindingManager bindingManager;
    private final TriggerManager triggerManager;
    private final Map<UUID, Object> entityTasks = new ConcurrentHashMap<>();

    public TimerManager(@NotNull JavaPlugin plugin, @NotNull TriggerBindingManager bindingManager, @NotNull TriggerManager triggerManager) {
        this.plugin = plugin;
        this.bindingManager = bindingManager;
        this.triggerManager = triggerManager;
    }

    public void start() {
        // Timer work is per-entity and scheduled by trackEntity, so there is nothing
        // global to start. Reset in case start() is called again after a reload.
        stop();
    }

    public void stop() {
        for (Object task : entityTasks.values()) {
            FoliaSchedulers.cancel(task);
        }
        entityTasks.clear();
    }

    /**
     * Tracks an entity immediately (e.g. when spawned or loaded).
     */
    public void trackEntity(@NotNull Entity entity) {
        if (!entity.isValid() || entity.isDead()) {
            return;
        }
        UUID id = entity.getUniqueId();
        if (entityTasks.containsKey(id)) {
            return;
        }

        AtomicReference<Object> handle = new AtomicReference<>();
        Object task = FoliaSchedulers.runEntityTimer(plugin, entity, () -> {
            if (!entity.isValid() || entity.isDead()) {
                FoliaSchedulers.cancel(handle.get());
                entityTasks.remove(id);
                return;
            }
            tickEntity(entity);
        }, 1L, 1L);

        handle.set(task);
        if (task == null) {
            return;
        }
        Object previous = entityTasks.putIfAbsent(id, task);
        if (previous != null) {
            // Another caller won the race; keep the first task only.
            FoliaSchedulers.cancel(task);
        }
    }

    private void tickEntity(@NotNull Entity entity) {
        int currentTick = Bukkit.getCurrentTick();
        Collection<Metaskill> skills = bindingManager.getBoundSkills(entity);
        for (Metaskill skill : skills) {
            for (String triggerStr : skill.getTriggers()) {
                if (triggerStr.toLowerCase().startsWith("ontimer")) {
                    int interval = parseInterval(triggerStr);
                    if (currentTick % interval == 0) {
                        triggerManager.executeTrigger(entity, "onTimer", null);
                    }
                }
            }
        }
    }

    private int parseInterval(String triggerStr) {
        int bracketIndex = triggerStr.indexOf('{');
        if (bracketIndex == -1) {
            return 20; // Default to 20 ticks
        }
        Map<String, String> options = ParserUtils.parseOptions(triggerStr);
        String val = options.getOrDefault("interval", options.getOrDefault("i", "20"));
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 20;
        }
    }
}
