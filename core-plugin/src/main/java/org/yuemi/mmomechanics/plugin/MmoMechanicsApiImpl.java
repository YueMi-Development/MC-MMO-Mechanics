package org.yuemi.mmomechanics.plugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;
import org.jetbrains.annotations.Nullable;

final class MmoMechanicsApiImpl implements MmoMechanicsApi {

    private final MmoMechanicsPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final SkillExecutor skillExecutor;

    public MmoMechanicsApiImpl(@NotNull MmoMechanicsPlugin plugin, @NotNull SkillExecutor skillExecutor) {
        this.plugin = plugin;
        this.skillExecutor = skillExecutor;
    }

    @Override
    public void sendMessage(
            @NotNull Player player,
            @NotNull String message
    ) {
        String parsed = message;
        if (plugin.isPlaceholderApiEnabled()) {
            parsed = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, message);
        }
        player.sendMessage(miniMessage.deserialize(parsed));
    }

    @Override
    public boolean isFeatureEnabled(@NotNull Player player) {
        return player.hasPermission("mmomechanics.feature");
    }

    @Override
    public @NotNull SkillExecutor getSkillExecutor() {
        return skillExecutor;
    }

    @Override
    public @NotNull String parsePlaceholders(@Nullable Target target, @NotNull String text) {
        if (target != null && target.isEntity() && target.getAsEntity() instanceof Player player) {
            if (plugin.isPlaceholderApiEnabled()) {
                return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
            }
        }
        return text;
    }
}
