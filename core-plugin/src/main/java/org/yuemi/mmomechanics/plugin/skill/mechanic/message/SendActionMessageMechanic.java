package org.yuemi.mmomechanics.plugin.skill.mechanic.message;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Displays action bar text above the hotbar.
 */
public final class SendActionMessageMechanic implements Mechanic {
    private final String message;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public SendActionMessageMechanic(@NotNull String message) {
        this.message = message;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                String parsed = message;
                if (api != null) {
                    parsed = api.parsePlaceholders(target, message);
                }
                player.sendActionBar(miniMessage.deserialize(parsed));
            }
        }
    }
}

