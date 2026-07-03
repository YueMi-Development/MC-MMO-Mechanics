package org.yuemi.mmomechanics.plugin.skill.mechanic.location;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Spawns visual shapes (point particles, circles, ring outlines, box borders) around targets.
 */
public final class ParticleMechanic implements Mechanic {
    public enum Shape {
        NORMAL,
        BOX,
        RING
    }

    private final Particle particle;
    private final int amount;
    private final double speed;
    private final double hSpread;
    private final double vSpread;
    private final Shape shape;
    private final double radius; // For rings
    private final double size;   // For boxes

    public ParticleMechanic(
            @NotNull Particle particle,
            int amount,
            double speed,
            double hSpread,
            double vSpread,
            @NotNull Shape shape,
            double radius,
            double size
    ) {
        this.particle = particle;
        this.amount = amount;
        this.speed = speed;
        this.hSpread = hSpread;
        this.vSpread = vSpread;
        this.shape = shape;
        this.radius = radius;
        this.size = size;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Location loc = target.getLocation();
            World world = loc.getWorld();
            if (world == null) continue;

            switch (shape) {
                case NORMAL -> {
                    world.spawnParticle(particle, loc, amount, hSpread, vSpread, hSpread, speed);
                }
                case RING -> {
                    double step = (2.0 * Math.PI) / amount;
                    for (int i = 0; i < amount; i++) {
                        double angle = i * step;
                        double x = loc.getX() + radius * Math.cos(angle);
                        double z = loc.getZ() + radius * Math.sin(angle);
                        Location ringLoc = new Location(world, x, loc.getY() + 0.1, z);
                        world.spawnParticle(particle, ringLoc, 1, 0, 0, 0, 0);
                    }
                }
                case BOX -> {
                    double minX = loc.getX() - size / 2.0;
                    double maxX = loc.getX() + size / 2.0;
                    double minY = loc.getY();
                    double maxY = loc.getY() + size;
                    double minZ = loc.getZ() - size / 2.0;
                    double maxZ = loc.getZ() + size / 2.0;

                    // Draw bottom ring
                    drawBoxOutline(world, minX, maxX, minY, minZ, maxZ);
                    // Draw top ring
                    drawBoxOutline(world, minX, maxX, maxY, minZ, maxZ);
                    // Draw vertical pillars
                    drawVerticalLine(world, minX, minZ, minY, maxY);
                    drawVerticalLine(world, maxX, minZ, minY, maxY);
                    drawVerticalLine(world, minX, maxZ, minY, maxY);
                    drawVerticalLine(world, maxX, maxZ, minY, maxY);
                }
            }
        }
    }

    private void drawBoxOutline(World world, double minX, double maxX, double y, double minZ, double maxZ) {
        for (double x = minX; x <= maxX; x += 0.25) {
            world.spawnParticle(particle, new Location(world, x, y, minZ), 1, 0, 0, 0, 0);
            world.spawnParticle(particle, new Location(world, x, y, maxZ), 1, 0, 0, 0, 0);
        }
        for (double z = minZ; z <= maxZ; z += 0.25) {
            world.spawnParticle(particle, new Location(world, minX, y, z), 1, 0, 0, 0, 0);
            world.spawnParticle(particle, new Location(world, maxX, y, z), 1, 0, 0, 0, 0);
        }
    }

    private void drawVerticalLine(World world, double x, double z, double minY, double maxY) {
        for (double y = minY; y <= maxY; y += 0.25) {
            world.spawnParticle(particle, new Location(world, x, y, z), 1, 0, 0, 0, 0);
        }
    }
}

