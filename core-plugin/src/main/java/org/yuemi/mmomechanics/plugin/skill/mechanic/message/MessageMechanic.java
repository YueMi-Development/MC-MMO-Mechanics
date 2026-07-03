package org.yuemi.mmomechanics.plugin.skill.mechanic.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Sends formatted chat messages to targeted players.
 */
public final class MessageMechanic implements Mechanic {
    private final String message;

    public MessageMechanic(@NotNull String message) {
        this.message = message;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api == null) return;
        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                api.sendMessage(player, message);
            }
        }
    }
}

