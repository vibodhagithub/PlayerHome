package me.sasa.PlayerHome.GUIs.MenuUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {
    @EventHandler
    public void playerInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) {
            menu.handleMenu(event);
        }
    }
}
