package org.yuemi.mmomechanics.plugin.skill.targeter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.LocationTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;

public final class PlayerLocationByNameTargeter implements Targeter {

    private final String nameExpression;

    public PlayerLocationByNameTargeter(@NotNull String nameExpression) {
        this.nameExpression = nameExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        String targetName = nameExpression;
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            targetName = api.parsePlaceholders(context.getCaster(), nameExpression);
        }

        Player player = Bukkit.getPlayerExact(targetName);
        if (player != null) {
            return Collections.singletonList(new LocationTarget(player.getLocation()));
        }
        return Collections.emptyList();
    }
}
