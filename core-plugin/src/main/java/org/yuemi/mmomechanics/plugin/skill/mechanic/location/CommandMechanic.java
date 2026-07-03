package org.yuemi.mmomechanics.plugin.skill.mechanic.location;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Executes console commands or target-performed player commands.
 */
public final class CommandMechanic implements Mechanic {
    public enum SenderType {
        CONSOLE,
        PLAYER,
        OP
    }

    private final String command;
    private final SenderType senderType;

    public CommandMechanic(@NotNull String command, @NotNull SenderType senderType) {
        this.command = command;
        this.senderType = senderType;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);

        for (Target target : targets) {
            String parsed = command;
            if (api != null) {
                parsed = api.parsePlaceholders(target, command);
            }
            if (parsed.startsWith("/")) {
                parsed = parsed.substring(1);
            }

            if (senderType == SenderType.PLAYER && target.getAsEntity() instanceof Player player) {
                player.performCommand(parsed);
            } else if (senderType == SenderType.OP && target.getAsEntity() instanceof Player player) {
                boolean wasOp = player.isOp();
                try {
                    player.setOp(true);
                    player.performCommand(parsed);
                } finally {
                    player.setOp(wasOp);
                }
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
            }
        }
    }
}

