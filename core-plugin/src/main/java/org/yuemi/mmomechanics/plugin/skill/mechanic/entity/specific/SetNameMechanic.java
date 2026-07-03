package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Sets a custom name visible above targeted entities with full text coloring and placeholders.
 */
public final class SetNameMechanic implements Mechanic {
    private final String name;
    private final boolean visible;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public SetNameMechanic(@NotNull String name, boolean visible) {
        this.name = name;
        this.visible = visible;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity != null) {
                String parsed = name;
                if (api != null) {
                    parsed = api.parsePlaceholders(target, name);
                }
                entity.customName(miniMessage.deserialize(parsed));
                entity.setCustomNameVisible(visible);
            }
        }
    }
}

