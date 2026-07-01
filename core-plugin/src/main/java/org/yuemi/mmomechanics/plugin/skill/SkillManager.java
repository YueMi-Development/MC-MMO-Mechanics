package org.yuemi.mmomechanics.plugin.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.Metaskill;
import org.yuemi.mmomechanics.api.skill.condition.Condition;
import org.yuemi.mmomechanics.api.skill.mechanic.DelayMechanic;
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

        try {
            java.net.URI jarUri = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            try (java.util.zip.ZipFile zip = new java.util.zip.ZipFile(new File(jarUri))) {
                Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    java.util.zip.ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("skills/") && name.endsWith(".json5")) {
                        try {
                            plugin.saveResource(name, false);
                        } catch (IllegalArgumentException ignored) {
                            // Thrown if already exists
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
        String clean = name.trim().toLowerCase();
        if (clean.equalsIgnoreCase("lightning")) {
            return new LightningStrikeMechanic();
        } else if (clean.startsWith("delay")) {
            long ticks = 20; // default 1 second
            if (clean.contains("{") && clean.contains("}")) {
                String options = clean.substring(clean.indexOf("{") + 1, clean.indexOf("}"));
                for (String pair : options.split(",")) {
                    String[] parts = pair.split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String val = parts[1].trim();
                        if (key.equalsIgnoreCase("ticks")) {
                            try {
                                ticks = Long.parseLong(val);
                            } catch (NumberFormatException ignored) {}
                        } else if (key.equalsIgnoreCase("seconds")) {
                            try {
                                ticks = (long) (Double.parseDouble(val) * 20);
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
            return new DelayMechanic(ticks);
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
