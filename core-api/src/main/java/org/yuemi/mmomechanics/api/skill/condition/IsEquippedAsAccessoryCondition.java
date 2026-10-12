package org.yuemi.mmomechanics.api.skill.condition;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmomechanics.api.skill.context.SkillContext;
import org.yuemi.mmomechanics.api.skill.target.Target;

/**
 * Condition that checks if the source item (provided via context variable "source_item")
 * is currently equipped as an accessory by the caster.
 *
 * If no source_item is set in context, falls back to checking whether the caster
 * has any accessory equipped.
 *
 * Requires MmoAccessories to be installed. Falls back to false if not available.
 */
public final class IsEquippedAsAccessoryCondition implements Condition {

    @Override
    public boolean test(@NotNull SkillContext context, @NotNull Target target) {
        // Get the source item from context (if available)
        ItemStack sourceItem = context.getVariable("source_item")
                .filter(ItemStack.class::isInstance)
                .map(ItemStack.class::cast)
                .orElse(null);

        // Get the player from the target
        Entity entity = target.getAsEntity();
        if (entity == null) {
            // Use caster if target isn't an entity
            entity = context.getCaster().getAsEntity();
        }
        if (!(entity instanceof Player player)) {
            return false;
        }

        AccessoriesApiAccessor api = AccessoriesApiHolder.get();
        if (api == null || !api.isAvailable()) {
            return false;
        }

        if (sourceItem != null) {
            return api.isItemEquipped(player, sourceItem);
        }
        return !api.getAllEquipped(player).isEmpty();
    }

    /**
     * Lazy holder for the MmoAccessories API accessed via reflection.
     */
    private static final class AccessoriesApiHolder {
        private static final AccessoriesApiAccessor INSTANCE = create();

        static AccessoriesApiAccessor get() {
            return INSTANCE;
        }

        private static AccessoriesApiAccessor create() {
            try {
                Class<?> providerClass = Class.forName("org.yuemi.mmoaccessories.api.AccessoriesApiProvider");
                Object provider = providerClass.getMethod("getApi").invoke(null);
                if (provider == null) return null;
                Object api = providerClass.getMethod("getApi").invoke(provider);
                if (api == null) return null;
                return new AccessoriesApiAccessor(api);
            } catch (Throwable t) {
                return null;
            }
        }
    }

    /**
     * Thin wrapper around MmoAccessories API using reflection.
     */
    private static final class AccessoriesApiAccessor {
        private final Object api;
        private final java.lang.reflect.Method isAvailable;
        private final java.lang.reflect.Method getAllEquipped;

        AccessoriesApiAccessor(Object api) {
            this.api = api;
            this.isAvailable = find("isAvailable");
            this.getAllEquipped = find("getAllEquipped", Player.class);
        }

        private java.lang.reflect.Method find(String name, Class<?>... params) {
            try {
                Class<?> apiClass = Class.forName("org.yuemi.mmoaccessories.api.AccessoriesApi");
                return apiClass.getMethod(name, params);
            } catch (Exception e) {
                return null;
            }
        }

        boolean isAvailable() {
            try {
                if (isAvailable == null) return true;
                return (Boolean) isAvailable.invoke(api);
            } catch (Exception e) {
                return false;
            }
        }

        java.util.List<ItemStack> getAllEquipped(Player player) {
            try {
                if (getAllEquipped == null) return java.util.List.of();
                @SuppressWarnings("unchecked")
                java.util.List<ItemStack> result = (java.util.List<ItemStack>) getAllEquipped.invoke(api, player);
                return result != null ? result : java.util.List.of();
            } catch (Exception e) {
                return java.util.List.of();
            }
        }

        boolean isItemEquipped(Player player, ItemStack target) {
            if (target == null) return false;
            for (ItemStack equipped : getAllEquipped(player)) {
                if (equipped != null && equipped.isSimilar(target)) {
                    return true;
                }
            }
            return false;
        }
    }
}
