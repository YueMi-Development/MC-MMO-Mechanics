package org.yuemi.mmomechanics.plugin.skill.parser.mechanic;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.yuemi.mmomechanics.api.skill.mechanic.Mechanic;
import org.yuemi.mmomechanics.plugin.skill.mechanic.entity.specific.EquipMechanic;
import org.yuemi.mmomechanics.plugin.skill.parser.ParserUtils;

import java.util.Map;

public final class EquipParser {

    public @Nullable Mechanic parse(@Nullable String name) {
        if (name == null) return null;
        String clean = name.trim();
        String lower = clean.toLowerCase();

        boolean isCopy = lower.startsWith("equipcopy");

        Map<String, String> options = ParserUtils.parseOptions(clean);
        
        EquipMechanic.Slot slot = EquipMechanic.Slot.MAINHAND;
        String slotStr = options.getOrDefault("slot", options.getOrDefault("s", "mainhand")).toUpperCase();
        try {
            slot = EquipMechanic.Slot.valueOf(slotStr);
        } catch (IllegalArgumentException ignored) {
            switch (slotStr) {
                case "HAND", "MAIN_HAND" -> slot = EquipMechanic.Slot.MAINHAND;
                case "OFF_HAND" -> slot = EquipMechanic.Slot.OFFHAND;
                case "HELM", "HEAD" -> slot = EquipMechanic.Slot.HELMET;
                case "CHEST", "BODY" -> slot = EquipMechanic.Slot.CHESTPLATE;
                case "LEGS" -> slot = EquipMechanic.Slot.LEGGINGS;
            }
        }

        Material material = Material.AIR;
        String itemStr = options.getOrDefault("item", options.getOrDefault("i", options.getOrDefault("material", options.getOrDefault("m", ""))));
        if (!itemStr.isEmpty()) {
            Material matched = Material.matchMaterial(itemStr.toUpperCase());
            if (matched != null) material = matched;
        }

        int amount = 1;
        try {
            if (options.containsKey("amount")) amount = Integer.parseInt(options.get("amount"));
            else if (options.containsKey("a")) amount = Integer.parseInt(options.get("a"));
        } catch (NumberFormatException ignored) {}

        return new EquipMechanic(isCopy, slot, material, amount);
    }
}
