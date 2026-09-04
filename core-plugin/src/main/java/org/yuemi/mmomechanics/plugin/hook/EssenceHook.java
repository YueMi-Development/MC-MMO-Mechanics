package org.yuemi.mmomechanics.plugin.hook;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmoessence.api.EssenceApi;
import org.yuemi.mmoessence.api.EssenceApiProvider;
import org.yuemi.mmoessence.api.ElementType;

/**
 * Helper class for MmoEssence integration.
 * Provides utility functions for accessing essence data.
 */
public class EssenceHook {

    private EssenceHook() {}

    /**
     * Check if MmoEssence hook is enabled.
     * Requires MmoEssence plugin to be loaded AND config hook enabled.
     */
    public static boolean isEnabled(JavaPlugin plugin) {
        return plugin.getServer().getPluginManager().isPluginEnabled("MmoEssence")
            && plugin.getConfig().getBoolean("hooks.mmo-essence", true);
    }

    /**
     * Get the active JavaPlugin instance for MmoMechanics.
     */
    public static JavaPlugin getPlugin() {
        return (JavaPlugin) org.bukkit.Bukkit.getPluginManager().getPlugin("MmoMechanics");
    }

    /**
     * Calculate damage multiplier based on caster's essence level.
     * Returns 1.0 if hook is disabled or caster is not a player.
     *
     * @param element The elemental type
     * @param caster  The living entity dealing damage
     * @return Damage multiplier (1.0 = no bonus)
     */
    public static double getDamageMultiplier(ElementType element, LivingEntity caster) {
        if (!isEnabled(getPlugin()) || !(caster instanceof Player player)) {
            return 1.0;
        }

        // TODO: Implement scaling formula
        // Example: multiplier = 1.0 + (essenceLevel * scalingFactor)
        return 1.0;
    }

    /**
     * Get caster's essence level for a specific element.
     *
     * @param caster  The player to check
     * @param element The element type
     * @return Essence level, or 0 if not available
     */
    public static int getEssenceLevel(Player caster, ElementType element) {
        EssenceApi api = EssenceApiProvider.getApi();
        if (api != null && api.isAvailable()) {
            return api.getEssence(caster, element);
        }
        return 0;
    }

    /**
     * Get caster's essence percentage for a specific element (0.0 - 1.0).
     *
     * @param caster  The player to check
     * @param element The element type
     * @return Essence percentage, or 0.0 if not available
     */
    public static double getEssencePercent(Player caster, ElementType element) {
        EssenceApi api = EssenceApiProvider.getApi();
        if (api != null && api.isAvailable()) {
            return api.getEssencePercent(caster, element);
        }
        return 0.0;
    }

    /**
     * Get all essence values for a player.
     *
     * @param caster The player to check
     * @return Map of element to essence level
     */
    public static java.util.Map<ElementType, Integer> getAllEssence(Player caster) {
        EssenceApi api = EssenceApiProvider.getApi();
        if (api != null && api.isAvailable()) {
            return api.getAllEssence(caster);
        }
        return java.util.Map.of();
    }
}
