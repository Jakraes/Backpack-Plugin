package org.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.plugin.items.Backpack;

public class BackpackCraftListener implements Listener {

    @EventHandler
    public void onBackpackCraft(CraftItemEvent event) {
        ItemStack item = event.getRecipe().getResult();

        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasLocalizedName()) return;
        if (!item.getItemMeta().getLocalizedName().equals("Backpack")) return;

        event.setCurrentItem(new Backpack());
    }
}
