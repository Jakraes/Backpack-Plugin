package org.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackpackMenuListener implements Listener {

    @EventHandler
    public void onBackpackClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.RESET + "Backpack")) return;

        ItemStack item;

        if (event.getClickedInventory().getSize() == 9) item = event.getCursor();
        else item = event.getCurrentItem();

        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasLocalizedName()) return;
        if (item.getItemMeta().getLocalizedName().equals("Backpack")) event.setCancelled(true);
    }

}
