package org.plugin.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.plugin.JaksBackpack;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().discoverRecipe(new NamespacedKey(JaksBackpack.getPlugin(), "jaks-backpack"));

        event.getPlayer().setResourcePack("https://www.dropbox.com/s/ywx2ivxwpviv2lg/Jak%27s%20Backpacks.zip?dl=1");
    }
}
