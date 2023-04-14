package org.plugin.items;


import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Backpack extends ItemStack {
    static int currentBackpack = 0;
    static ArrayList<Inventory> inventories = new ArrayList<>();

    public Backpack() {
        super(Material.LEATHER);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Backpack");
        meta.setLocalizedName("Backpack");
        meta.setCustomModelData(currentBackpack++);
        setItemMeta(meta);

        inventories.add(Bukkit.createInventory(null, 9, ChatColor.RESET + "Backpack"));
    }

    public static Inventory get(int i) {
        return inventories.get(i);
    }

    public static void read() throws FileNotFoundException {
        Gson gson = new Gson();
        File f = new File(Plugin.getPlugin().getDataFolder() + "/backpacks.json");
        if (!f.exists()) return;

        Reader reader = new FileReader(f);

        byte[][][] input = gson.fromJson(reader, byte[][][].class);

        for (byte[][] b : input) {
            Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RESET + "Backpack");
            for (byte[] bytes : b) {
                if (bytes != null) {
                    inv.addItem(ItemStack.deserializeBytes(bytes));
                }
            }
            inventories.add(inv);
        }

        currentBackpack = inventories.size();

        System.out.println("Backpacks loaded.");
    }

    public static void write() throws IOException {
        List<byte[][]> l = new ArrayList<>();

        for (Inventory inventory : inventories) {
            ItemStack[] stack = inventory.getContents();
            byte[][] bytes = new byte[stack.length][];
            for (int j = 0; j < stack.length; j++) {
                if (stack[j] != null) bytes[j] = stack[j].serializeAsBytes();
            }
            l.add(bytes);
        }

        byte[][][] output = new byte[l.size()][9][];
        l.toArray(output);

        Gson gson = new Gson();
        File f = new File(Plugin.getPlugin().getDataFolder() + "/backpacks.json");
        f.getParentFile().mkdir();
        f.createNewFile();
        Writer writer = new FileWriter(f, false);


        gson.toJson(output, writer);
        writer.flush();
        writer.close();

        System.out.println("Backpacks saved.");
    }
}
