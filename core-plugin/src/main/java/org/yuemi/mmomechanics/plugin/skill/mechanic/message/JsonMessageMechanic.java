package org.yuemi.mmomechanics.plugin.skill.mechanic.message;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Sends raw JSON component messages to targeted players.
 */
public final class JsonMessageMechanic implements Mechanic {
    private final String json;

    public JsonMessageMechanic(@NotNull String json) {
        this.json = json;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        for (Target target : targets) {
            if (target.getAsEntity() instanceof Player player) {
                String parsed = json;
                if (api != null) {
                    parsed = api.parsePlaceholders(target, json);
                }
                try {
                    player.sendMessage(GsonComponentSerializer.gson().deserialize(parsed));
                } catch (Exception e) {
                    player.sendMessage(parsed); // Fallback to raw text if json parsing fails
                }
            }
        }
    }
}

