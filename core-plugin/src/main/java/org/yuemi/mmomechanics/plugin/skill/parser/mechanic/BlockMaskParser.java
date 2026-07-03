package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.block.BlockMaskMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class BlockMaskParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean unmask = lower.startsWith("blockunmask");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        Material maskMaterial = Material.DIRT;
        String materialStr = options.getOrDefault("material", options.getOrDefault("m", options.getOrDefault("type", options.getOrDefault("t", "dirt"))));
        
        Material matched = Material.matchMaterial(materialStr.toUpperCase());
        if (matched != null) maskMaterial = matched;

        return new BlockMaskMechanic(maskMaterial, unmask);
    }
}
