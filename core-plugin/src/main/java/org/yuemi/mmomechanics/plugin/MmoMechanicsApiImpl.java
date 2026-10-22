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
    private final org.yuemi.mmomechanics.plugin.skill.trigger.TriggerManager triggerManager;

    public MmoMechanicsApiImpl(@NotNull MmoMechanicsPlugin plugin,
                               @NotNull SkillExecutor skillExecutor,
                               @NotNull org.yuemi.mmomechanics.plugin.skill.trigger.TriggerManager triggerManager) {
        this.plugin = plugin;
        this.skillExecutor = skillExecutor;
        this.triggerManager = triggerManager;
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

    @Override
    public boolean castSkill(@NotNull java.util.UUID casterUuid, @NotNull String skillId) {
        org.bukkit.entity.Entity entity = org.bukkit.Bukkit.getEntity(casterUuid);
        if (entity == null) {
            return false;
        }
        java.util.Optional<org.yuemi.mmomechanics.api.skill.Metaskill> skillOpt = plugin.getSkillManager().getSkill(skillId);
        if (skillOpt.isEmpty()) {
            return false;
        }
        org.yuemi.mmomechanics.api.skill.Metaskill skill = skillOpt.get();
        org.yuemi.mmomechanics.api.skill.trigger.Trigger trigger = () -> "API";
        Runnable run = () -> {
            org.yuemi.mmomechanics.api.skill.context.SkillContext context =
                    new org.yuemi.mmomechanics.api.skill.context.SkillContext(
                            new org.yuemi.mmomechanics.api.skill.target.EntityTarget(entity),
                            trigger
                    );
            skill.execute(context, java.util.Collections.singletonList(context.getCaster()));
        };
        // The caller may be on any thread, so on Folia the skill starts on the caster's region.
        if (isFolia()) {
            entity.getScheduler().execute(plugin, run, () -> { }, 0L);
        } else {
            run.run();
        }
        return true;
    }

    private boolean isFolia() {
        try {
            return org.yuemi.libs.api.platform.PlatformProvider.isFolia();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public boolean fireTrigger(@NotNull java.util.UUID casterUuid, @NotNull String triggerId) {
        if (triggerManager == null) return false;
        org.bukkit.entity.Entity entity = org.bukkit.Bukkit.getEntity(casterUuid);
        if (entity == null) return false;
        triggerManager.executeTrigger(entity, triggerId, null);
        return true;
    }
}
