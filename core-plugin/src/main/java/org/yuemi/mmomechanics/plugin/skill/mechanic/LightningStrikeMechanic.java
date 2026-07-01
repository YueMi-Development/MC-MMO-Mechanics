package org.yuemi.mmomechanics.plugin.skill.mechanic;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

public final class LightningStrikeMechanic implements Mechanic {

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Location loc = target.getLocation();
            World world = loc.getWorld();
            if (world != null) {
                world.strikeLightning(loc);
            }
        }
    }
}
