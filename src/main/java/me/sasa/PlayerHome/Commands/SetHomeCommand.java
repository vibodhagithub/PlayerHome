package me.sasa.PlayerHome.Commands;

import me.sasa.PlayerHome.PlayerHome;
import me.sasa.PlayerHome.Requests.HomeRequests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class SetHomeCommand implements CommandExecutor {

    private final PlayerHome playerHomePlugin;

    public SetHomeCommand(PlayerHome playerHomePlugin) {
        this.playerHomePlugin = playerHomePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            if(sender.hasPermission("playerhome.player")) {
                if(sender.hasPermission("playerhome.sethome")) {
                    if (args.length == 0) {
                        sender.sendMessage("Give home name or number /sethome <name/number>");
                    } else if (args.length > 1) {
                        sender.sendMessage("/sethome <name/number>");
                    } else {
                        Player player = (Player) sender;
                        String homename = args[0];
                        int maxHomes = HomeRequests.getMaxHomes(player);

                        List<String> notAllowedWorlds = playerHomePlugin.getConfig().getStringList("settings.not_allowed_worlds");

                        if (!(notAllowedWorlds.contains(player.getWorld().getName()))) {
                            int currentHomeCount = 0;
                            try {
                                currentHomeCount = playerHomePlugin.getHomeDatabase().getHomeCount(player);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            if (currentHomeCount >= maxHomes) {
                                sender.sendMessage(playerHomePlugin.getConfig().getString("messages.home_limit"));
                            } else {
                                try {
                                    if (!(playerHomePlugin.getHomeDatabase().homeExists(player, homename))) {
                                        try {
                                            playerHomePlugin.getHomeDatabase().addHome(player, homename);
                                            String setHomeMsg = playerHomePlugin.getConfig().getString("messages.home_set");
                                            setHomeMsg = setHomeMsg.replace("%home%", homename);
                                            sender.sendMessage(setHomeMsg);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            return true;
                                        }
                                    } else {
                                        sender.sendMessage(playerHomePlugin.getConfig().getString("messages.home_exists"));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    return true;
                                }
                            }
                        }else {
                            sender.sendMessage(playerHomePlugin.getConfig().getString("messages.home_world_not_allowd"));
                        }
                    }
                }else{
                    sender.sendMessage(playerHomePlugin.getConfig().getString("messages.no_permission"));
                }
            }else{
                sender.sendMessage(playerHomePlugin.getConfig().getString("messages.no_permission"));
            }
        }else{
            sender.sendMessage(playerHomePlugin.getConfig().getString("messages.player_only"));
        }
        return true;
    }

}
