package org.yuemi.mmomechanics.plugin.skill.mechanic.location;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Spawns block-breaking explosions or client-side cosmetic visual explosions.
 */
public final class ExplosionMechanic implements Mechanic {
    private final float yield;
    private final boolean fire;
    private final boolean breakBlocks;
    private final boolean fake;

    public ExplosionMechanic(float yield, boolean fire, boolean breakBlocks, boolean fake) {
        this.yield = yield;
        this.fire = fire;
        this.breakBlocks = breakBlocks;
        this.fake = fake;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        Entity caster = context.getCaster().getAsEntity();

        for (Target target : targets) {
            Location loc = target.getLocation();
            World world = loc.getWorld();
            if (world == null) continue;

            if (fake) {
                world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                world.spawnParticle(Particle.EXPLOSION, loc, 1);
            } else {
                world.createExplosion(loc, yield, fire, breakBlocks, caster);
            }
        }
    }
}

