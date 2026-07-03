package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.SetBlockTypeMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class SetBlockTypeParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        Material material = Material.STONE;
        String typeStr = options.getOrDefault("type", options.getOrDefault("t", options.getOrDefault("material", options.getOrDefault("m", "stone"))));
        Material matched = Material.matchMaterial(typeStr.toUpperCase());
        if (matched != null) material = matched;

        boolean physics = true;
        if (options.containsKey("physics")) physics = Boolean.parseBoolean(options.get("physics"));
        else if (options.containsKey("p")) physics = Boolean.parseBoolean(options.get("p"));

        return new SetBlockTypeMechanic(material, physics);
    }
}
