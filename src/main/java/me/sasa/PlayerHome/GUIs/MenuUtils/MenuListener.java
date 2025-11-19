package me.sasa.PlayerHome.GUIs.MenuUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return; // player clicked outside gui

        InventoryHolder holder = event.getClickedInventory().getHolder();

        // Only handle clicks inside menus
        if (holder instanceof Menu menu) {
            menu.handleMenu(event);
        }
    }
}
