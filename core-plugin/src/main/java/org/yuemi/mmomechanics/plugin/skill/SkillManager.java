package org.yuemi.mmomechanics.plugin.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.api.skill.mechanic.MechanicWrapper;
import org.yuemi.mmomechanics.api.skill.targeter.Targeter;
import org.yuemi.mmomechanics.plugin.skill.parser.condition.ConditionParser;
import org.yuemi.mmomechanics.plugin.skill.parser.mechanic.MechanicParser;
import org.yuemi.mmomechanics.plugin.skill.parser.targeter.TargeterParser;
import org.yuemi.mmomechanics.api.skill.BindConfig;

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

        try {
            java.net.URI jarUri = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            try (java.util.zip.ZipFile zip = new java.util.zip.ZipFile(new File(jarUri))) {
                Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    java.util.zip.ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("skills/") && name.endsWith(".json5")) {
                        File outFile = new File(plugin.getDataFolder(), name);
                        if (!outFile.exists()) {
                            try {
                                plugin.saveResource(name, false);
                            } catch (IllegalArgumentException ignored) {
                                // Thrown if already exists
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to extract default skills dynamically: " + e.getMessage());
        }

        File skillsFolder = new File(plugin.getDataFolder(), "skills");
        File[] files = skillsFolder.listFiles();
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

    private Metaskill createSkill(SkillConfigDto dto) {
        List<MechanicWrapper> wrappers = new ArrayList<>();
        if (dto.mechanics() != null) {
            for (MechanicConfigDto mechDto : dto.mechanics()) {
                Mechanic mechanic = MechanicParser.parse(mechDto.mechanic());
                Targeter targeter = TargeterParser.parse(mechDto.targeter());
                List<Condition> conditions = new ArrayList<>();
                if (mechDto.conditions() != null) {
                    for (String condStr : mechDto.conditions()) {
                        conditions.add(ConditionParser.parse(condStr));
                    }
                }
                if (mechanic != null && targeter != null) {
                    wrappers.add(new MechanicWrapper(mechanic, targeter, conditions));
                }
            }
        }

        List<String> triggers = dto.triggers() != null ? dto.triggers() : Collections.emptyList();
        BindConfig bindConfig = null;
        if (dto.bind() != null) {
            BindConfigDto bindDto = dto.bind();
            bindConfig = new BindConfig() {
                @Override
                public @NotNull Collection<String> getEntityTypes() {
                    return bindDto.types() != null ? bindDto.types() : Collections.emptyList();
                }

                @Override
                public boolean isGlobal() {
                    return bindDto.global() != null && bindDto.global();
                }

                @Override
                public @NotNull Collection<String> getUuids() {
                    return bindDto.uuids() != null ? bindDto.uuids() : Collections.emptyList();
                }
            };
        }

        BindConfig finalBindConfig = bindConfig;
        return new Metaskill() {
            @Override
            public @NotNull String getName() {
                return dto.name() != null ? dto.name() : "Unnamed Skill";
            }

            @Override
            public @NotNull Collection<MechanicWrapper> getMechanics() {
                return wrappers;
            }

            @Override
            public @NotNull Collection<String> getTriggers() {
                return triggers;
            }

            @Override
            public @org.jetbrains.annotations.Nullable BindConfig getBindConfig() {
                return finalBindConfig;
            }
        };
    }

    public @NotNull Optional<Metaskill> getSkill(@NotNull String name) {
        return Optional.ofNullable(loadedSkills.get(name.toLowerCase()));
    }

    public @NotNull Collection<String> getSkillNames() {
        return loadedSkills.keySet();
    }

    public @NotNull Collection<Metaskill> getLoadedSkills() {
        return loadedSkills.values();
    }

    public static record SkillConfigDto(
        String name,
        List<String> triggers,
        BindConfigDto bind,
        List<MechanicConfigDto> mechanics
    ) {}

    public static record BindConfigDto(
        List<String> types,
        Boolean global,
        List<String> uuids
    ) {}

    public static record MechanicConfigDto(
        String mechanic,
        String targeter,
        List<String> conditions
    ) {}
}
