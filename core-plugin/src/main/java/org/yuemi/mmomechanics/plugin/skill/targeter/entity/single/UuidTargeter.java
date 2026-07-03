package org.yuemi.mmomechanics.plugin.skill.targeter.entity.single;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.MmoMechanicsApi;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Targets a specific entity by its unique UUID.
 */
public final class UuidTargeter implements Targeter {

    private final String uuidExpression;

    public UuidTargeter(@NotNull String uuidExpression) {
        this.uuidExpression = uuidExpression;
    }

    @Override
    public @NotNull Collection<Target> getTargets(@NotNull SkillContext context) {
        String targetUuidStr = uuidExpression;
        MmoMechanicsApi api = Bukkit.getServicesManager().load(MmoMechanicsApi.class);
        if (api != null) {
            targetUuidStr = api.parsePlaceholders(context.getCaster(), uuidExpression);
        }

        try {
            UUID uuid = UUID.fromString(targetUuidStr.trim());
            Entity entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                return Collections.singletonList(new EntityTarget(entity));
            }
        } catch (IllegalArgumentException ignored) {}

        return Collections.emptyList();
    }
}
