package org.yuemi.mmomechanics.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import org.yuemi.mmomechanics.api.skill.executor.SkillExecutor;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.jetbrains.annotations.Nullable;

/**
 * Main API interface for accessing MMO Mechanics features and executing skills.
 */
public interface MmoMechanicsApi {

    /**
     * Sends a formatted chat message to a player, supporting PlaceholderAPI expansion.
     *
     * @param player the recipient player
     * @param message the message to send
     */
    void sendMessage(
            @NotNull Player player,
            @NotNull String message
    );

    /**
     * Checks if the player has permission to use mechanics features.
     *
     * @param player the player to check
     * @return true if enabled, false otherwise
     */
    boolean isFeatureEnabled(@NotNull Player player);

    /**
     * Gets the active skill executor instance.
     *
     * @return the skill executor
     */
    @NotNull SkillExecutor getSkillExecutor();

    /**
     * Parses PlaceholderAPI placeholders for the target recipient player.
     *
     * @param target the target context recipient
     * @param text the text to parse placeholders on
     * @return the parsed placeholder text
     */
    @NotNull String parsePlaceholders(@Nullable Target target, @NotNull String text);

    /**
     * Casts a skill on behalf of the entity with the specified UUID.
     *
     * @param casterUuid the UUID of the casting entity
     * @param skillId the ID of the skill to execute
     * @return true if the skill was successfully cast, false otherwise
     */
    boolean castSkill(@NotNull java.util.UUID casterUuid, @NotNull String skillId);
}
