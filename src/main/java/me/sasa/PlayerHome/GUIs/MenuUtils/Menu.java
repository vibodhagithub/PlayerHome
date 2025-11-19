package me.sasa.PlayerHome.GUIs.MenuUtils;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public abstract class Menu implements InventoryHolder {
    protected Inventory inventory;
    protected Player viewer;
    protected ItemStack FILLER_GLASS = makeItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ", null);
    protected HashMap<String, IconButton> buttons = new HashMap<>();

    public Menu(Player viewer) {
        this.viewer = viewer;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent event);

    public abstract void setMenuItems();

    public void open() {
        if (!(viewer.hasPermission("playerhome.home"))) {
            return;
        }

        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();

        this.setFillerGlass();
        viewer.openInventory(inventory);
    }


    public void setFillerGlass() {
        // fill empty slot with filler glass to fix some bug occur while TPS drops.
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    public ItemStack makeItem(Material material, String displayName, String[] lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(displayName);

            if (lore != null) {
                itemMeta.setLore(Arrays.asList(lore));
            }
        }
        item.setItemMeta(itemMeta);

        return item;
    }

    public void addButton(String name, IconButton button) {
        buttons.put(name, button);
    }


}
