package org.yuemi.mmomechanics.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.jetbrains.annotations.Nullable;

public interface MmoMechanicsApi {

    void sendMessage(
            @NotNull Player player,
            @NotNull String message
    );

    boolean isFeatureEnabled(@NotNull Player player);

    @NotNull SkillExecutor getSkillExecutor();

    @NotNull String parsePlaceholders(@Nullable Target target, @NotNull String text);
}
