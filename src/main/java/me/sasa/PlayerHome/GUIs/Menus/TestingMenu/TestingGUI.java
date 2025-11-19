package me.sasa.PlayerHome.GUIs.Menus.TestingMenu;

import me.sasa.PlayerHome.Databases.HomesDatabase;
import me.sasa.PlayerHome.GUIs.MenuUtils.IconButton;
import me.sasa.PlayerHome.GUIs.MenuUtils.Menu;
import me.sasa.PlayerHome.Requests.HomeRequests;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TestingGUI extends Menu {

    private final HomesDatabase database;

    public TestingGUI(Player viewer, HomesDatabase database) {
        super(viewer);
        this.database = database;
    }

    @Override
    public String getMenuName() {
        return "§bHomes";
    }

    @Override
    public int getSlots() {
        return 9 * 3; // 9 by 3 chest ekak...
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == null) return;

            String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
            if (buttons.containsKey(displayName)) {
                buttons.get(displayName).onPressed(event, event.getClick());
            }
        }
    }

    public void setMenuItems() {

        try {
            List<HomesDatabase.HomeData> homes = database.getPlayerAllHomes(viewer);
            int slot = 11;

            for (HomesDatabase.HomeData data : homes) {
                String homeName = data.getName();

                ItemStack homeItem = makeItem(Material.LIGHT_BLUE_BED, "§b" + homeName,
                        new String[]{
                                "§eLeft-Click: §aTeleport",
                                "§eRight-Click: §cDelete§e/§bSet"
                        });

                inventory.setItem(slot, homeItem);

                addButton("§b" + homeName, new IconButton() {
                    @Override
                    public void onPressed(InventoryClickEvent event, ClickType clickType) {
                        Player player = (Player) event.getWhoClicked();
                        try {
                            if (clickType == ClickType.LEFT) {
                                // Run command
                                String cmd = "home " + homeName; // Example command
                                player.performCommand(cmd);
                                player.closeInventory();
                            }else {

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                slot++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            viewer.sendMessage("§cCould not load your homes!");
        }

    }
}


