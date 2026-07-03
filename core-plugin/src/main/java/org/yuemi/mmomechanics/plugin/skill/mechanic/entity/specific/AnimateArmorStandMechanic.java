package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Animates armor stands by rotating their limbs over a duration of ticks.
 */
public final class AnimateArmorStandMechanic implements Mechanic {
    private final String limb;
    private final double rotX; // in radians per tick
    private final double rotY;
    private final double rotZ;
    private final int durationTicks;

    public AnimateArmorStandMechanic(
            @NotNull String limb,
            double rotX,
            double rotY,
            double rotZ,
            int durationTicks
    ) {
        this.limb = limb.toLowerCase();
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.durationTicks = durationTicks;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());
        if (plugin == null) return;

        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity instanceof ArmorStand stand) {
                new BukkitRunnable() {
                    int elapsed = 0;

                    @Override
                    public void run() {
                        if (!stand.isValid() || elapsed >= durationTicks) {
                            cancel();
                            return;
                        }
                        
                        switch (limb) {
                            case "head" -> stand.setHeadPose(stand.getHeadPose().add(rotX, rotY, rotZ));
                            case "body" -> stand.setBodyPose(stand.getBodyPose().add(rotX, rotY, rotZ));
                            case "leftarm" -> stand.setLeftArmPose(stand.getLeftArmPose().add(rotX, rotY, rotZ));
                            case "rightarm" -> stand.setRightArmPose(stand.getRightArmPose().add(rotX, rotY, rotZ));
                            case "leftleg" -> stand.setLeftLegPose(stand.getLeftLegPose().add(rotX, rotY, rotZ));
                            case "rightleg" -> stand.setRightLegPose(stand.getRightLegPose().add(rotX, rotY, rotZ));
                        }
                        elapsed++;
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }
    }
}

