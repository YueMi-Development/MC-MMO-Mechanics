package org.yuemi.mmomechanics.plugin.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.mechanic.LightningStrikeMechanic;
import org.yuemi.mmomechanics.plugin.skill.targeter.NearTargeter;
import org.yuemi.mmomechanics.plugin.skill.targeter.SelfTargeter;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public final class SkillManager {

    private final JavaPlugin plugin;
    private final Map<String, Metaskill> loadedSkills = new HashMap<>();
    private final ObjectMapper mapper;

    public SkillManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.mapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .enable(com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .enable(com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_TRAILING_COMMA)
                .build();
    }

    public void loadSkills() {
        loadedSkills.clear();
        File skillsFolder = new File(plugin.getDataFolder(), "skills");
        if (!skillsFolder.exists()) {
            skillsFolder.mkdirs();
        }

        File[] files = skillsFolder.listFiles();
        if (files == null || files.length == 0) {
            saveDefaultSkill(skillsFolder, "self_lightning.json5");
            files = skillsFolder.listFiles();
        }

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json5")) {
                    try {
                        SkillConfigDto dto = mapper.readValue(file, SkillConfigDto.class);
                        Metaskill skill = createSkill(dto);
                        loadedSkills.put(file.getName().substring(0, file.getName().lastIndexOf('.')).toLowerCase(), skill);
                        plugin.getLogger().info("Loaded skill: " + dto.name() + " (" + file.getName() + ")");
                    } catch (Exception e) {
                        plugin.getLogger().severe("Failed to load skill file " + file.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveDefaultSkill(File folder, String filename) {
        File targetFile = new File(folder, filename);
        try (InputStream in = plugin.getResource("skills/" + filename)) {
            if (in != null) {
                Files.copy(in, targetFile.toPath());
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save default skill: " + filename);
        }
    }

    private Metaskill createSkill(SkillConfigDto dto) {
        List<MechanicWrapper> wrappers = new ArrayList<>();
        if (dto.mechanics() != null) {
            for (MechanicConfigDto mechDto : dto.mechanics()) {
                Mechanic mechanic = parseMechanic(mechDto.mechanic());
                Targeter targeter = parseTargeter(mechDto.targeter());
                List<Condition> conditions = new ArrayList<>();
                if (mechDto.conditions() != null) {
                    for (String condStr : mechDto.conditions()) {
                        conditions.add(parseCondition(condStr));
                    }
                }
                if (mechanic != null && targeter != null) {
                    wrappers.add(new MechanicWrapper(mechanic, targeter, conditions));
                }
            }
        }

        return new Metaskill() {
            @Override
            public @NotNull String getName() {
                return dto.name() != null ? dto.name() : "Unnamed Skill";
            }

            @Override
            public @NotNull Collection<MechanicWrapper> getMechanics() {
                return wrappers;
            }
        };
    }

    private @Nullable Mechanic parseMechanic(String name) {
        if (name == null) return null;
        if (name.trim().equalsIgnoreCase("lightning")) {
            return new LightningStrikeMechanic();
        }
        return null;
    }

    private @Nullable Targeter parseTargeter(String name) {
        if (name == null) return null;
        String cleanName = name.trim().toLowerCase();
        if (cleanName.equalsIgnoreCase("self")) {
            return new SelfTargeter();
        } else if (cleanName.startsWith("near")) {
            double radius = 5.0;
            if (cleanName.contains("{") && cleanName.contains("}")) {
                String options = cleanName.substring(cleanName.indexOf("{") + 1, cleanName.indexOf("}"));
                for (String pair : options.split(",")) {
                    String[] parts = pair.split("=");
                    if (parts.length == 2 && parts[0].trim().equalsIgnoreCase("r")) {
                        try {
                            radius = Double.parseDouble(parts[1].trim());
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
            return new NearTargeter(radius);
        }
        return null;
    }

    private @NotNull Condition parseCondition(String name) {
        return (context, target) -> true;
    }

    public @NotNull Optional<Metaskill> getSkill(@NotNull String name) {
        return Optional.ofNullable(loadedSkills.get(name.toLowerCase()));
    }

    public @NotNull Collection<String> getSkillNames() {
        return loadedSkills.keySet();
    }

    public static record SkillConfigDto(
        String name,
        List<MechanicConfigDto> mechanics
    ) {}

    public static record MechanicConfigDto(
        String mechanic,
        String targeter,
        List<String> conditions
    ) {}
}
