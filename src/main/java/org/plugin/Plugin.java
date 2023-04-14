package org.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.plugin.items.Backpack;
import org.plugin.listeners.BackpackCraftListener;
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
        getServer().getPluginManager().registerEvents(new BackpackCraftListener(), this);

        NamespacedKey key = new NamespacedKey(this, "backpack");
        ShapedRecipe recipe = new ShapedRecipe(key, new Backpack());

        recipe.shape("SLS", "LIL", "SLS");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('I', Material.IRON_INGOT);

        Bukkit.addRecipe(recipe);
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
