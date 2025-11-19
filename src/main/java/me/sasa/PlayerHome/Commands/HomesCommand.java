package me.sasa.PlayerHome.Commands;

import me.sasa.PlayerHome.Databases.HomesDatabase;
import me.sasa.PlayerHome.GUIs.Menus.TestingMenu.TestingGUI;
import me.sasa.PlayerHome.PlayerHome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomesCommand implements CommandExecutor {

    private final PlayerHome plugin;

    public HomesCommand(PlayerHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            new TestingGUI(player, plugin.getHomeDatabase()).open();
        }
        return true;
    }
}
