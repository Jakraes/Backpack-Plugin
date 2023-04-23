package org.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.items.Backpack;
import org.plugin.items.BackpackPlaceholder;
import org.plugin.listeners.BackpackListener;
import org.plugin.listeners.PlayerJoinListener;

import java.io.IOException;

public final class JaksBackpack extends JavaPlugin {
    private static JaksBackpack plugin;
    @Override
    public void onEnable() {
        plugin = this;

        try {
            Backpack.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new BackpackListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        Bukkit.addRecipe(BackpackPlaceholder.getRecipe());
    }

    @Override
    public void onDisable() {
        try {
            Backpack.write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JaksBackpack getPlugin() {
        return plugin;
    }
}
