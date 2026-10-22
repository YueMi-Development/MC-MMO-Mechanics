package org.yuemi.mmomechanics.plugin.skill.mechanic.system;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.plugin.MmoMechanicsPlugin;
import org.yuemi.mmomechanics.plugin.skill.executor.TargetDispatcher;

import java.util.Collection;
import java.util.Optional;

/**
 * Casts another skill by name on the target.
 */
public final class SkillMechanic implements Mechanic {
    private final String skillName;

    public SkillMechanic(@NotNull String skillName) {
        this.skillName = skillName;
    }

    @Override
    public void execute(@NotNull SkillContext context, @NotNull Collection<Target> targets) {
        MmoMechanicsPlugin plugin = JavaPlugin.getPlugin(MmoMechanicsPlugin.class);
        Optional<Metaskill> skillOpt = plugin.getSkillManager().getSkill(skillName);
        if (skillOpt.isEmpty()) {
            plugin.getLogger().warning("Sub-skill not found: " + skillName);
            return;
        }

        Metaskill skill = skillOpt.get();
        Entity caster = context.getCaster().getAsEntity();

        // The sub-skill resolves its targets around the caster, so it has to start on
        // the caster's own thread rather than on the region of the current target.
        if (caster != null && TargetDispatcher.getInstance().isFolia()) {
            TargetDispatcher.getInstance().dispatchToCaster(caster, () -> skill.execute(context, targets));
        } else {
            skill.execute(context, targets);
        }
    }
}
