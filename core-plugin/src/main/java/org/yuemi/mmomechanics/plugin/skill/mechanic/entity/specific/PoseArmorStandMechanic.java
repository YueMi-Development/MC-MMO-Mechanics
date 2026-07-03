package org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;

import java.util.Collection;

/**
 * Sets specific rotations (head, body, arms, legs) on targeted armor stands.
 */
public final class PoseArmorStandMechanic implements Mechanic {
    private final EulerAngle head;
    private final EulerAngle body;
    private final EulerAngle leftArm;
    private final EulerAngle rightArm;
    private final EulerAngle leftLeg;
    private final EulerAngle rightLeg;

    public PoseArmorStandMechanic(
            @Nullable EulerAngle head,
            @Nullable EulerAngle body,
            @Nullable EulerAngle leftArm,
            @Nullable EulerAngle rightArm,
            @Nullable EulerAngle leftLeg,
            @Nullable EulerAngle rightLeg
    ) {
        this.head = head;
        this.body = body;
        this.leftArm = leftArm;
        this.rightArm = rightArm;
        this.leftLeg = leftLeg;
        this.rightLeg = rightLeg;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        for (Target target : targets) {
            Entity entity = target.getAsEntity();
            if (entity instanceof ArmorStand stand) {
                if (head != null) stand.setHeadPose(head);
                if (body != null) stand.setBodyPose(body);
                if (leftArm != null) stand.setLeftArmPose(leftArm);
                if (rightArm != null) stand.setRightArmPose(rightArm);
                if (leftLeg != null) stand.setLeftLegPose(leftLeg);
                if (rightLeg != null) stand.setRightLegPose(rightLeg);
            }
        }
    }
}

