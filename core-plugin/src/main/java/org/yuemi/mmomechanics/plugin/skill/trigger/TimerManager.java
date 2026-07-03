package org.yuemi.mmomechanics.plugin.skill.trigger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages executing the 'onTimer' skills at specified intervals.
 */
public final class TimerManager {

    private final JavaPlugin plugin;
    private final TriggerBindingManager bindingManager;
    private final TriggerManager triggerManager;
    private final Set<Entity> activeEntities = ConcurrentHashMap.newKeySet();
    private BukkitRunnable timerTask;

    public TimerManager(@NotNull JavaPlugin plugin, @NotNull TriggerBindingManager bindingManager, @NotNull TriggerManager triggerManager) {
        this.plugin = plugin;
        this.bindingManager = bindingManager;
        this.triggerManager = triggerManager;
    }

    public void start() {
        if (timerTask != null) {
            timerTask.cancel();
        }

        timerTask = new BukkitRunnable() {
            private int scanTicks = 0;

            @Override
            public void run() {
                scanTicks++;
                // Refresh the active entities set from loaded worlds once a second (20 ticks)
                if (scanTicks >= 20) {
                    scanTicks = 0;
                    refreshActiveEntities();
                }

                int currentTick = Bukkit.getCurrentTick();
                activeEntities.removeIf(entity -> {
                    if (!entity.isValid() || entity.isDead()) {
                        return true;
                    }

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
                    return false;
                });
            }
        };
        timerTask.runTaskTimer(plugin, 1L, 1L);
    }

    public void stop() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        activeEntities.clear();
    }

    /**
     * Tracks an entity immediately (e.g. when spawned or loaded).
     */
    public void trackEntity(@NotNull Entity entity) {
        if (entity.isValid() && !entity.isDead()) {
            activeEntities.add(entity);
        }
    }

    private void refreshActiveEntities() {
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.isValid() && !entity.isDead()) {
                    Collection<Metaskill> skills = bindingManager.getBoundSkills(entity);
                    boolean hasTimer = false;
                    for (Metaskill skill : skills) {
                        for (String triggerStr : skill.getTriggers()) {
                            if (triggerStr.toLowerCase().startsWith("ontimer")) {
                                hasTimer = true;
                                break;
                            }
                        }
                        if (hasTimer) break;
                    }
                    if (hasTimer) {
                        activeEntities.add(entity);
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
