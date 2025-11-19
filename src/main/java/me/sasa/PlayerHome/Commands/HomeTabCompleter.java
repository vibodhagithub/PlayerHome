package me.sasa.PlayerHome.Commands;

import me.sasa.PlayerHome.PlayerHome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {

    private final PlayerHome playerHomePlugin;

    public HomeTabCompleter(PlayerHome playerHomePlugin) {
        this.playerHomePlugin = playerHomePlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player)) return Collections.emptyList();

        List<String> suggestions = new ArrayList<>();

        switch (command.getName().toLowerCase()) {

            case "delhome", "home":
                if (args.length == 1) {
                    try {
                        List<String> playerHomes = playerHomePlugin.getHomeDatabase().getHomeNamesArray(player);
                        String typed = args[0].toLowerCase();
                        for (String home : playerHomes) {
                            if (home.toLowerCase().startsWith(typed)) {
                                suggestions.add(home);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                return Collections.emptyList();
        }

        return suggestions;
    }

}
