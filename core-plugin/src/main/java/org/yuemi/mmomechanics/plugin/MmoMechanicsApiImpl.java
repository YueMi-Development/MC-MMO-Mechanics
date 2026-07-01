package org.yuemi.mmomechanics.plugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;

final class MmoMechanicsApiImpl implements MmoMechanicsApi {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final SkillExecutor skillExecutor;

    public MmoMechanicsApiImpl(@NotNull SkillExecutor skillExecutor) {
        this.skillExecutor = skillExecutor;
    }

    @Override
    public void sendMessage(
            @NotNull Player player,
            @NotNull String message
    ) {
        player.sendMessage(miniMessage.deserialize(message));
    }

    @Override
    public boolean isFeatureEnabled(@NotNull Player player) {
        return player.hasPermission("mmomechanics.feature");
    }

    @Override
    public @NotNull SkillExecutor getSkillExecutor() {
        return skillExecutor;
    }
}
