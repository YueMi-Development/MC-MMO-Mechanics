package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.general;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Adjusts entity speed and velocity vectors, including relative pushes/throws.
 */
public final class VelocityMechanic implements Mechanic {
    public enum Mode {
        ADD,
        SET,
        MULTIPLY
    }

    private final double x;
    private final double y;
    private final double z;
    private final double force;
    private final Mode mode;
    private final boolean directional;
    private final boolean propel;

    public VelocityMechanic(
            double x,
            double y,
            double z,
            double force,
            @NotNull Mode mode,
            boolean directional,
            boolean propel
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.force = force;
        this.mode = mode;
        this.directional = directional;
        this.propel = propel;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Entity caster = context.getCaster().getAsEntity();

        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity == null) continue;

            Vector vel;
            if (propel && caster != null) {
                Location casterLoc = caster.getLocation();
                Location targetLoc = entity.getLocation();
                Vector direction = targetLoc.toVector().subtract(casterLoc.toVector());
                if (direction.lengthSquared() > 0) {
                    direction.normalize();
                } else {
                    direction = new Vector(0, 1, 0);
                }
                vel = direction.multiply(force);
            } else if (directional && caster != null) {
                vel = caster.getLocation().getDirection().clone().multiply(force);
                vel.setY(vel.getY() + y);
            } else {
                vel = new Vector(x, y, z);
                if (force != 1.0) {
                    vel.multiply(force);
                }
            }

            Vector current = entity.getVelocity();
            Vector result = switch (mode) {
                case ADD -> current.add(vel);
                case SET -> vel;
                case MULTIPLY -> current.multiply(vel);
            };

            entity.setVelocity(result);
        }
    }
}

