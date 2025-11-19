package me.sasa.PlayerHome.GUIs.Menus.TestingMenu;

import me.sasa.PlayerHome.GUIs.MenuUtils.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

// kamathi command ekaka.. mee class eka call karahan mee widihata..
// new TestingGUI(Player oona_karana_player).open();

public class TestingGUI extends Menu {
    public TestingGUI(Player viewer) {
        super(viewer);
    }

    @Override
    public String getMenuName() {
        return "GUI ekee nama chest ekee nama";
    }

    @Override
    public int getSlots() {
        return 9 * 3; // 9 by 3 chest ekak...
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            // inv able.
            int rawSlot = event.getRawSlot();
            if (rawSlot < 0 || getSlots() - 1 < rawSlot) {
                return;
            }

            // cansel clickable if it's our gui
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                // null pointer exceptions
                return;
            }

            if (this.buttons.get(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) != null) {
                this.buttons.get(event.getCurrentItem().getItemMeta().getDisplayName()).onPressed(event);
            }
        }
    }

    @Override
    public void setMenuItems() {
        // Methana ubata oona widihata GUI ekata button hadaaganna puluwan..
        this.inventory.setItem(0, new ItemStack(Material.DIAMOND));
        this.inventory.setItem(1, new ItemStack(Material.IRON_AXE));
        this.inventory.setItem(2, new ItemStack(Material.EMERALD));
        this.inventory.setItem(3, new ItemStack(Material.BLUE_BED));
    }
}


