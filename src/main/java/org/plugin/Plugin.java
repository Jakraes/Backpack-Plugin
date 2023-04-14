package org.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.commands.giveItem;
import org.plugin.items.Backpack;
import org.plugin.listeners.BackpackMenuListener;
import org.plugin.listeners.BackpackOpenListener;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class Plugin extends JavaPlugin implements Listener {
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        try {
            Backpack.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new BackpackOpenListener(), this);
        getServer().getPluginManager().registerEvents(new BackpackMenuListener(), this);
        getCommand("giveItem").setExecutor(new giveItem());
    }

    @Override
    public void onDisable() {
        try {
            Backpack.write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
