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

    /**
     * Executes any skill bound to the given caster that has the specified trigger
     * in its {@code triggers} list (e.g. {@code "onEquip"}, {@code "onUnequip"},
     * {@code "onAttack"}). Unlike {@link #castSkill(UUID, String)}, this dispatches
     * through {@code TriggerManager} and respects the standard JSON5 {@code triggers}
     * declarations.
     *
     * @param casterUuid the UUID of the entity that triggered the event
     * @param triggerId  the trigger id (case-insensitive), matching a skill's
     *                   {@code triggers[]} entry
     * @return true if at least one skill was found and executed
     */
    boolean fireTrigger(@NotNull java.util.UUID casterUuid, @NotNull String triggerId);
}
