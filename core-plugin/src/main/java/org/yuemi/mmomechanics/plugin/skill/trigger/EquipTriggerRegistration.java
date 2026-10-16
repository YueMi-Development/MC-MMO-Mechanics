package org.yuemi.mmomechanics.plugin.skill.trigger;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventApi;
import org.yuemi.libs.api.event.EventApiProvider;

/**
 * Registers {@code onEquip} / {@code onUnequip} trigger sources.
 *
 * <p>Two paths are wired:
 * <ol>
 *   <li>Paper's {@link PlayerArmorChangeEvent} when the server runs Paper.</li>
 *   <li>Spigot fallback: {@code InventoryClickEvent} / {@code InventoryDragEvent}
 *       on the player's own inventory; armor contents 36..39 are watched for
 *       empty to non-empty / non-empty to empty transitions.</li>
 * </ol>
 *
 * <p>Both paths route through {@link TriggerManager#executeTrigger}, so skill
 * JSON5 {@code triggers[]} declarations drive the actual firing.
 */
public final class EquipTriggerRegistration {

    private final org.bukkit.plugin.java.JavaPlugin plugin;
    private final TriggerManager triggerManager;
    private final Listener fallbackListener;

    public EquipTriggerRegistration(@NotNull org.bukkit.plugin.java.JavaPlugin plugin,
                                    @NotNull TriggerManager triggerManager) {
        this.plugin = plugin;
        this.triggerManager = triggerManager;
        this.fallbackListener = new ArmorFallbackListener();
    }

    public void register() {
        EventApi eventApi = EventApiProvider.getApi();
        if (eventApi == null) {
            plugin.getLogger().warning("YueMiLibs EventApi service not found. onEquip/onUnequip triggers will not be registered!");
            return;
        }

        // Path 1: Paper (preferred).
        try {
            eventApi.bukkit().subscribe(PlayerArmorChangeEvent.class)
                    .priority(EventPriority.MONITOR)
                    .ignoreCancelled(true)
                    .handler(this::handlePaper);
            plugin.getLogger().info("onEquip / onUnequip registered via Paper PlayerArmorChangeEvent.");
            return;
        } catch (Throwable t) {
            plugin.getLogger().warning("Paper PlayerArmorChangeEvent unavailable; falling back to InventoryClickEvent tracking. Cause: "
                    + t.getMessage());
        }

        // Path 2: Spigot / non-Paper fallback.
        org.bukkit.Bukkit.getPluginManager().registerEvents(fallbackListener, plugin);
        plugin.getLogger().info("onEquip / onUnequip registered via Spigot fallback listener.");
    }

    private void handlePaper(@NotNull PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = event.getNewItem();
        ItemStack oldItem = event.getOldItem();

        if (isNonEmpty(newItem) && isEmpty(oldItem)) {
            triggerManager.executeTrigger(player, "onEquip", null);
        }
        if (isNonEmpty(oldItem) && isEmpty(newItem)) {
            triggerManager.executeTrigger(player, "onUnequip", null);
        }
        if (isNonEmpty(oldItem) && isNonEmpty(newItem) && !itemsEqual(newItem, oldItem)) {
            triggerManager.executeTrigger(player, "onUnequip", null);
            triggerManager.executeTrigger(player, "onEquip", null);
        }
    }

    private static boolean isEmpty(ItemStack item) {
        return item == null || item.getType().isAir() || item.getAmount() <= 0;
    }

    private static boolean isNonEmpty(ItemStack item) {
        return !isEmpty(item);
    }

    private static boolean itemsEqual(ItemStack a, ItemStack b) {
        if (a == null || b == null) return a == b;
        return a.isSimilar(b);
    }

    private final class ArmorFallbackListener implements Listener {
        private final java.util.Map<java.util.UUID, ItemStack[]> lastArmorByPlayer = new java.util.concurrent.ConcurrentHashMap<>();

        @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
        public void onClick(org.bukkit.event.inventory.InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player player)) return;
            org.bukkit.inventory.PlayerInventory pInv = player.getInventory();
            org.yuemi.libs.api.folia.FoliaSchedulers.executeEntity(plugin, player, () -> checkArmor(player, pInv));
        }

        @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
        public void onDrag(org.bukkit.event.inventory.InventoryDragEvent event) {
            if (!(event.getWhoClicked() instanceof Player player)) return;
            org.yuemi.libs.api.folia.FoliaSchedulers.executeEntity(plugin, player, () -> checkArmor(player, player.getInventory()));
        }

        private void checkArmor(Player player, org.bukkit.inventory.PlayerInventory inv) {
            ItemStack[] current = inv.getArmorContents();
            ItemStack[] previous = lastArmorByPlayer.put(player.getUniqueId(), current.clone());
            if (previous == null) return;

            // PlayerInventory.getArmorContents(): boots, leggings, chestplate, helmet.
            for (int i = 0; i < current.length; i++) {
                ItemStack oldPiece = previous[i];
                ItemStack newPiece = current[i];
                if (isNonEmpty(oldPiece) && isEmpty(newPiece)) {
                    triggerManager.executeTrigger(player, "onUnequip", null);
                } else if (isEmpty(oldPiece) && isNonEmpty(newPiece)) {
                    triggerManager.executeTrigger(player, "onEquip", null);
                } else if (isNonEmpty(oldPiece) && isNonEmpty(newPiece) && !itemsEqual(oldPiece, newPiece)) {
                    triggerManager.executeTrigger(player, "onUnequip", null);
                    triggerManager.executeTrigger(player, "onEquip", null);
                }
            }
        }
    }
}
