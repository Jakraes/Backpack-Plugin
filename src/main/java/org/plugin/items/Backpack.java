package org.plugin.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.plugin.JaksBackpack;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Backpack extends ItemStack {
    private static int backpackId = 0;
    private static final Map<Integer, Inventory> inventories = new HashMap<>();

    public Backpack() {
        super(Material.LEATHER);

        ItemMeta meta = getItemMeta();
        meta.displayName(Component.text(ChatColor.AQUA + "Backpack"));

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        NamespacedKey k1 = new NamespacedKey(JaksBackpack.getPlugin(), "item-name");
        NamespacedKey k2 = new NamespacedKey(JaksBackpack.getPlugin(), "item-id");

        meta.getPersistentDataContainer().set(k1, PersistentDataType.STRING, "jaks-backpack");
        meta.getPersistentDataContainer().set(k2, PersistentDataType.INTEGER, backpackId++);

        meta.setCustomModelData(2);

        setItemMeta(meta);

        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Backpack"));
        inventories.put(backpackId - 1, inv);
    }

    public static Inventory get(int i) {
        if (inventories.containsKey(i)) return inventories.get(i);
        return null;
    }

    public static void remove(int i) {
        inventories.remove(i);
    }

    public static void read() throws IOException {
        Gson gson = new Gson();
        File f = new File(JaksBackpack.getPlugin().getDataFolder() + "/jaks_backpacks.json");
        if (!f.exists()) return;

        Reader reader = new FileReader(f);

        /* This one is actually quite weird, I get keys as strings
         * and the values are a bunch ArrayLists with doubles as
         * values. I was having quite a bit of problems at first but
         * a working solution has been found. (This definitely doesn't
         * mean it's acceptable, I just can't find a better workaround)
         */
        Map<Integer, byte[][]> input = (Map) gson.fromJson(reader, Map.class);

        Iterator<Integer> keys = input.keySet().iterator(); // Funny iterators, probably a leftover
        Iterator<byte[][]> values = input.values().iterator(); // Same here

        // Prepare yourself for the worst piece of code I've ever written
        while (keys.hasNext() && values.hasNext()) {
            Object ktemp = keys.next(); // No idea why I need to get it as an object first
            int key = Integer.parseInt((String) ktemp); // Cool cast lmao

            Object vtemp = values.next(); // Also no idea why I need to get it as an object first
            ArrayList value = (ArrayList) vtemp; // Another cool cast god knows why

            Inventory inv = Bukkit.createInventory(null, 9, Component.text("Backpack"));

            for (int i = 0; i < value.size(); i++) {
                Object b = value.get(i);
                if (b == null) continue;

                byte[] btemp = new byte[((ArrayList) b).size()];
                for (int j = 0; j < btemp.length; j++) {
                    int itemp = (int) (double) ((ArrayList) b).get(j); // What the fuck? Goes from Double to double to int
                    btemp[j] = (byte) itemp; // Finally a decent value
                }

                inv.setItem(i, ItemStack.deserializeBytes(btemp));
            }

            inventories.put(key, inv);
        }

        backpackId = inventories.size();
    }

    public static void write() throws IOException {
        Map<Integer, byte[][]> output = new HashMap<>();

        for (int i : inventories.keySet()) {
            ItemStack[] itemList = inventories.get(i).getContents();
            byte[][] val = new byte[9][];
            for (int j = 0; j < 9; j++) {
                if (itemList[j] != null) val[j] = itemList[j].serializeAsBytes();
            }
            output.put(i, val);
        }

        Gson gson = new GsonBuilder().create();
        File f = new File(JaksBackpack.getPlugin().getDataFolder() + "/jaks_backpacks.json");
        f.getParentFile().mkdir();
        f.createNewFile();
        Writer writer = new FileWriter(f, false);

        gson.toJson(output, writer);
        writer.flush();
        writer.close();
    }
}
