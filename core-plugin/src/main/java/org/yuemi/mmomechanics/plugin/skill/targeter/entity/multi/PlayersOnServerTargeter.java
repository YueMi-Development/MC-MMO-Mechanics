package org.yuemi.mmomechanics.plugin.skill.targeter.entity.multi;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Targets all players online on the server.
 */
public final class PlayersOnServerTargeter implements Targeter {
    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        return Bukkit.getOnlinePlayers().stream()
                .map(EntityTarget::new)
                .collect(Collectors.toList());
    }
}
