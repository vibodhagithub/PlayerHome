package me.sasa.PlayerHome.GUIs.MenuUtils;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class IconButton {
    public abstract void onPressed(InventoryClickEvent event, ClickType clickType);
}
