package me.sasa.PlayerHome.Commands;

import me.sasa.PlayerHome.Databases.HomesDatabase;
import me.sasa.PlayerHome.PlayerHome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class Test implements CommandExecutor {

    private final PlayerHome playerHomePlugin;

    public Test(PlayerHome playerHomePlugin) {
        this.playerHomePlugin = playerHomePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Ensure sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        List<String> notAllowedWorlds = playerHomePlugin.getConfig().getStringList("settings.not_allowed_worlds");
        for (String world : notAllowedWorlds) {
            player.sendMessage("§cNot allowed: " + world);
        }
        return true;
    }
}
