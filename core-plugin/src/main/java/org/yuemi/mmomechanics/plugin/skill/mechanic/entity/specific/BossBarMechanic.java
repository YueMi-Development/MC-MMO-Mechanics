package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shows, removes, or modifies progress bars on top of player screens.
 */
public final class BossBarMechanic implements Mechanic {
    public enum Action {
        CREATE,
        REMOVE,
        SET
    }

    private static final Map<UUID, Map<String, BossBar>> PLAYER_BARS = new ConcurrentHashMap<>();

    private final Action action;
    private final String barId;
    private final String title;
    private final float progress;
    private final BossBar.Color color;
    private final BossBar.Overlay overlay;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BossBarMechanic(
            @NotNull Action action,
            @NotNull String barId,
            @NotNull String title,
            float progress,
            @NotNull BossBar.Color color,
            @NotNull BossBar.Overlay overlay
    ) {
        this.action = action;
        this.barId = barId.toLowerCase().trim();
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.overlay = overlay;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);

        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                UUID playerUuid = player.getUniqueId();
                Map<String, BossBar> activeBars = PLAYER_BARS.computeIfAbsent(playerUuid, k -> new ConcurrentHashMap<>());

                switch (action) {
                    case CREATE -> {
                        if (activeBars.containsKey(barId)) {
                            player.hideBossBar(activeBars.remove(barId));
                        }
                        String parsedTitle = title;
                        if (api != null) {
                            parsedTitle = api.parsePlaceholders(target, title);
                        }
                        BossBar bar = BossBar.bossBar(
                                miniMessage.deserialize(parsedTitle),
                                progress,
                                color,
                                overlay
                        );
                        activeBars.put(barId, bar);
                        player.showBossBar(bar);
                    }
                    case REMOVE -> {
                        BossBar bar = activeBars.remove(barId);
                        if (bar != null) {
                            player.hideBossBar(bar);
                        }
                    }
                    case SET -> {
                        BossBar bar = activeBars.get(barId);
                        if (bar != null) {
                            if (!title.isEmpty()) {
                                String parsedTitle = title;
                                if (api != null) {
                                    parsedTitle = api.parsePlaceholders(target, title);
                                }
                                bar.name(miniMessage.deserialize(parsedTitle));
                            }
                            if (progress >= 0.0f && progress <= 1.0f) {
                                bar.progress(progress);
                            }
                            bar.color(color);
                            bar.overlay(overlay);
                        }
                    }
                }
            }
        }
    }
}

