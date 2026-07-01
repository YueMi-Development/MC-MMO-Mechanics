package org.yuemi.mmomechanics.plugin.skill.condition;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

public final class IsNightCondition implements Condition {

    @Override
    public boolean test(@NotNull SkillContext context, @NotNull Target target) {
        World world = context.getCaster().getLocation().getWorld();
        if (world == null) return false;
        long time = world.getTime() % 24000;
        return time >= 13000 && time < 24000;
    }
}
