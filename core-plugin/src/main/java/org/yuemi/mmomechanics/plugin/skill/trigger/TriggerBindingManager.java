package org.yuemi.mmomechanics.plugin.skill.trigger;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.BindConfig;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.plugin.skill.SkillManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages resolving which skills are bound to an entity.
 */
public final class TriggerBindingManager {

    private final SkillManager skillManager;
    private final NamespacedKey skillsKey;

    public TriggerBindingManager(@NotNull JavaPlugin plugin, @NotNull SkillManager skillManager) {
        this.skillManager = skillManager;
        this.skillsKey = new NamespacedKey(plugin, "skills");
    }

    /**
     * Resolves all skills currently bound to the given entity.
     */
    public @NotNull Collection<Metaskill> getBoundSkills(@NotNull Entity entity) {
        List<Metaskill> bound = new ArrayList<>();
        List<String> pdcSkills = getPdcSkills(entity);

        for (Metaskill skill : skillManager.getLoadedSkills()) {
            String skillName = skill.getName().toLowerCase();
            String skillKey = skillName.replace(" ", "_");

            // 1. Check Scoreboard Tags
            if (entity.getScoreboardTags().contains("mmo-skill-" + skillKey) || entity.getScoreboardTags().contains("mmo-skill-" + skillName)) {
                bound.add(skill);
                continue;
            }

            // 2. Check PDC Metadata
            if (pdcSkills.contains(skillKey) || pdcSkills.contains(skillName)) {
                bound.add(skill);
                continue;
            }

            // 3. Check Config-based Bindings (Type, Global, UUID)
            BindConfig bind = skill.getBindConfig();
            if (bind != null) {
                if (bind.isGlobal()) {
                    bound.add(skill);
                    continue;
                }
                String entityTypeName = entity.getType().name();
                if (bind.getEntityTypes().stream().anyMatch(t -> t.equalsIgnoreCase(entityTypeName))) {
                    bound.add(skill);
                    continue;
                }
                String entityUuid = entity.getUniqueId().toString();
                if (bind.getUuids().stream().anyMatch(u -> u.equalsIgnoreCase(entityUuid))) {
                    bound.add(skill);
                }
            }
        }
        return bound;
    }

    /**
     * Binds a skill to an entity using PDC metadata.
     */
    public void bindSkill(@NotNull Entity entity, @NotNull String skillName) {
        List<String> skills = new ArrayList<>(getPdcSkills(entity));
        String cleanName = skillName.toLowerCase().replace(" ", "_");
        if (!skills.contains(cleanName)) {
            skills.add(cleanName);
            setPdcSkills(entity, skills);
        }
    }

    /**
     * Unbinds a skill from an entity.
     */
    public void unbindSkill(@NotNull Entity entity, @NotNull String skillName) {
        List<String> skills = new ArrayList<>(getPdcSkills(entity));
        String cleanName = skillName.toLowerCase().replace(" ", "_");
        if (skills.remove(cleanName)) {
            setPdcSkills(entity, skills);
        }
    }

    private @NotNull List<String> getPdcSkills(@NotNull Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        String stored = pdc.get(skillsKey, PersistentDataType.STRING);
        if (stored == null || stored.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(stored.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private void setPdcSkills(@NotNull Entity entity, @NotNull List<String> skills) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (skills.isEmpty()) {
            pdc.remove(skillsKey);
        } else {
            pdc.set(skillsKey, PersistentDataType.STRING, String.join(",", skills));
        }
    }
}
