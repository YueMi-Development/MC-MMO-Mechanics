package org.yuemi.mmomechanics.plugin.skill.mechanic.system;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.target.Target;
import org.yuemi.mmomechanics.plugin.MmoMechanicsPlugin;

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
        if (skillOpt.isPresent()) {
            Metaskill skill = skillOpt.get();
            skill.execute(context, targets);
        } else {
            plugin.getLogger().warning("Sub-skill not found: " + skillName);
        }
    }
}
