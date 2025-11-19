package me.sasa.PlayerHome.Commands;

import me.sasa.PlayerHome.PlayerHome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelHomeCommand implements CommandExecutor {

    private final PlayerHome playerHomePlugin;

    public DelHomeCommand(PlayerHome playerHomePlugin) {
        this.playerHomePlugin = playerHomePlugin;
    }

    private final Map<UUID, Long> tpCooldown = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            if (sender.hasPermission("playerhome.player")) {
                if (sender.hasPermission("playerhome.delhome")) {
                    if (args.length == 0) {
                        sender.sendMessage("Give home name or number /delhome <home>");
                    } else if (args.length > 1) {
                        sender.sendMessage("/delhome <home>");
                    } else {
                        Player player = (Player) sender;
                        String homename = args[0];

                        try{
                            if(playerHomePlugin.getHomeDatabase().homeExists(player, homename)){
                                playerHomePlugin.getHomeDatabase().delHome(player, homename);
                                sender.sendMessage(playerHomePlugin.getConfig().getString("messages.home_deleted"));
                            }else {
                                sender.sendMessage(playerHomePlugin.getConfig().getString("messages.home_not_found"));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return true;
                        }
                    }
                }
            }
        }

        return true;
    }
}
