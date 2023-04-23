package org.plugin.listeners;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.plugin.JaksBackpack;
import org.plugin.items.Backpack;

public class BackpackListener implements Listener {
    private boolean isBackpack(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return false;

        NamespacedKey k = new NamespacedKey(JaksBackpack.getPlugin(), "item-name");
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (!pdc.getKeys().contains(k)) return false;
        return pdc.get(k, PersistentDataType.STRING).equals("jaks-backpack");
    }

    @EventHandler
    public void onCraftBackpack(CraftItemEvent event) {
        if (!isBackpack(event.getCurrentItem())) return;

        event.setCurrentItem(new Backpack());
    }

    @EventHandler
    public void onBackpackOpen(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if (!isBackpack(item)) return;


        NamespacedKey k = new NamespacedKey(JaksBackpack.getPlugin(), "item-id");
        int i = meta.getPersistentDataContainer().get(k, PersistentDataType.INTEGER);

        event.getPlayer().openInventory(Backpack.get(i));
    }

    @EventHandler
    public void onBackpackClickMenu(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getView().title().equals(Component.text("Backpack"))) return;

        ItemStack item;

        if (event.getClickedInventory().getSize() == 9) item = event.getCursor();
        else item = event.getCurrentItem();

        if (item == null) return;
        if (isBackpack(item)) event.setCancelled(true);
    }

    @EventHandler
    public void onBackpackDespawn(ItemDespawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        ItemMeta meta = item.getItemMeta();

        if (!isBackpack(item)) return;

        NamespacedKey k = new NamespacedKey(JaksBackpack.getPlugin(), "item-id");
        int i = meta.getPersistentDataContainer().get(k, PersistentDataType.INTEGER);

        Backpack.remove(i);
    }

    @EventHandler
    public void onBackpackDestroy(EntityRemoveFromWorldEvent event) {
        if (!(event.getEntity() instanceof Item)) return;

        ItemStack item = ((Item) event.getEntity()).getItemStack();
        ItemMeta meta = item.getItemMeta();

        if (!isBackpack(item)) return;

        NamespacedKey k = new NamespacedKey(JaksBackpack.getPlugin(), "item-id");
        int i = meta.getPersistentDataContainer().get(k, PersistentDataType.INTEGER);

        Backpack.remove(i);
    }
}
