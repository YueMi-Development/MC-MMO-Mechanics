package org.yuemi.mmomechanics.plugin.skill.trigger;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.platform.PlatformProvider;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.EntityTarget;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.api.skill.trigger.Trigger;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Coordinates routing gameplay events to skill executions.
 */
public final class TriggerManager {

    private final JavaPlugin plugin;
    private final TriggerBindingManager bindingManager;

    public TriggerManager(@NotNull JavaPlugin plugin, @NotNull TriggerBindingManager bindingManager) {
        this.plugin = plugin;
        this.bindingManager = bindingManager;
    }

    /**
     * Executes skills registered on the caster entity for the given trigger event.
     *
     * <p>On Folia the event may fire on a thread that does not own the caster, such as
     * {@code EntityDamageByEntityEvent}, which runs on the victim's region. The skills
     * are therefore dispatched to the caster's own region before they resolve their
     * targets, since target resolution reads state around the caster.
     */
    public void executeTrigger(@NotNull Entity caster, @NotNull String triggerId, @Nullable Target triggerTarget) {
        Collection<Metaskill> boundSkills = bindingManager.getBoundSkills(caster);
        if (boundSkills.isEmpty()) {
            return;
        }

        Trigger trigger = new Trigger() {
            @Override
            public @NotNull String getId() {
                return triggerId;
            }

            @Override
            public @NotNull Optional<Target> getTriggerTarget() {
                return Optional.ofNullable(triggerTarget);
            }
        };

        for (Metaskill skill : boundSkills) {
            boolean matches = false;
            for (String trig : skill.getTriggers()) {
                // Handle triggers with parameter matching, like onTimer{i=20}
                String baseTrig = trig;
                int bracketIndex = trig.indexOf('{');
                if (bracketIndex != -1) {
                    baseTrig = trig.substring(0, bracketIndex).trim();
                }

                if (baseTrig.equalsIgnoreCase(triggerId)) {
                    matches = true;
                    break;
                }
            }

            if (matches) {
                if (isFolia()) {
                    caster.getScheduler().execute(plugin,
                            () -> runSkill(caster, skill, trigger, triggerId), () -> { }, 0L);
                } else {
                    runSkill(caster, skill, trigger, triggerId);
                }
            }
        }
    }

    /**
     * Checks whether the server runs on Folia, where events may fire on a thread that
     * does not own the caster.
     *
     * @return true when running on Folia
     */
    private boolean isFolia() {
        try {
            return PlatformProvider.isFolia();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void runSkill(
            @NotNull Entity caster,
            @NotNull Metaskill skill,
            @NotNull Trigger trigger,
            @NotNull String triggerId
    ) {
        SkillContext context = new SkillContext(new EntityTarget(caster), trigger);
        try {
            skill.execute(context, Collections.singletonList(context.getCaster()));
        } catch (Exception e) {
            caster.getServer().getLogger().severe("Error executing skill " + skill.getName() + " on trigger " + triggerId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
