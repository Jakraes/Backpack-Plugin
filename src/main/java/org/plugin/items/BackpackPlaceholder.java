package org.plugin.items;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.plugin.JaksBackpack;

public class BackpackPlaceholder extends ItemStack {
    public BackpackPlaceholder() {
        super(Material.MILK_BUCKET);

        ItemMeta meta = getItemMeta();
        meta.displayName(Component.text(ChatColor.AQUA + "Backpack"));

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        NamespacedKey k1 = new NamespacedKey(JaksBackpack.getPlugin(), "item-name");

        meta.getPersistentDataContainer().set(k1, PersistentDataType.STRING, "jaks-backpack");

        meta.setCustomModelData(2);

        setItemMeta(meta);
    }

    public static ShapedRecipe getRecipe() {
        ItemStack item = new BackpackPlaceholder();

        NamespacedKey key = new NamespacedKey(JaksBackpack.getPlugin(), "jaks-backpack");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("LIL", "ICI", "LIL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('C', Material.CHEST);

        return recipe;
    }
}
