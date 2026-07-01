package org.yuemi.mmomechanics.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;

public interface MmoMechanicsApi {

    void sendMessage(
            @NotNull Player player,
            @NotNull String message
    );

    boolean isFeatureEnabled(@NotNull Player player);

    @NotNull SkillExecutor getSkillExecutor();
}
