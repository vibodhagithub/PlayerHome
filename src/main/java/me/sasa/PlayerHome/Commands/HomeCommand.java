package me.sasa.PlayerHome.Commands;

import me.sasa.PlayerHome.Databases.HomesDatabase;
import me.sasa.PlayerHome.PlayerHome;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeCommand implements CommandExecutor {

    private final PlayerHome playerHomePlugin;

    private final Map<UUID, Long> tpCooldown = new HashMap<>();
    private final Map<UUID, Location> startLocation = new HashMap<>();

    public HomeCommand(PlayerHome playerHomePlugin) {
        this.playerHomePlugin = playerHomePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            if (sender.hasPermission("playerhome.player")) {
                if (sender.hasPermission("playerhome.home")) {
                    if (args.length == 0) {
                        sender.sendMessage("Give home name or number /home <name>");
                    } else if (args.length > 1) {
                        sender.sendMessage("/home <name>");
                    } else {
                        Player player = (Player) sender;
                        String homename = args[0];

                        // Tp after 5 seconds
                        long tpafterSec = playerHomePlugin.getConfig().getLong("settings.cooldowns.before_tp");

                        UUID uuid = player.getUniqueId();

                        // Cooldown check for tp again
                        long nexttp = playerHomePlugin.getConfig().getLong("settings.cooldowns.next_tp") * 1000;
                        if (tpCooldown.containsKey(uuid)) {
                            long last = tpCooldown.get(uuid);
                            long now = System.currentTimeMillis();

                            long left = nexttp - (now - last);
                            if (left > 0) {
                                String tpAgainMsg = playerHomePlugin.getConfig().getString("messages.tp_again_cooldown");
                                int tpAgainTime = (int) (left / 1000.0);
                                tpAgainMsg = tpAgainMsg.replace("%time%", String.valueOf(tpAgainTime));
                                player.sendMessage(tpAgainMsg);
                                return true;
                            }
                        }

                        try {
                            // Get home
                            HomesDatabase.HomeData homedata = playerHomePlugin.getHomeDatabase().getHomeData(player, homename);

                            // Save start location (to detect movement)
                            startLocation.put(uuid, player.getLocation());

                            // Start countdown
                            String tpAgainMsg = playerHomePlugin.getConfig().getString("messages.tp_timer");
                            tpAgainMsg = tpAgainMsg.replace("%time%", String.valueOf(tpafterSec));
                            player.sendMessage(tpAgainMsg);
                            AtomicInteger timer = new AtomicInteger((int) tpafterSec);

                            Bukkit.getScheduler().runTaskTimer(playerHomePlugin, (task) -> {

                                if (playerHomePlugin.getConfig().getBoolean("settings.cancel_when_player_move")){

                                    // If player moved → cancel
                                    if (!player.getLocation().getBlock().equals(startLocation.get(uuid).getBlock())) {
                                        player.sendMessage(playerHomePlugin.getConfig().getString("messages.tp_cancel_move"));
                                        task.cancel();
                                        return;
                                    }
                                }

                                int timeLeft = timer.getAndDecrement();

                                // Show countdown in ActionBar
                                player.spigot().sendMessage(
                                        ChatMessageType.ACTION_BAR,
                                        new TextComponent("§eTeleporting in §c" + timeLeft + "§es...")
                                );

                                // When timer ends → teleport
                                if (timeLeft <= 0) {
                                    task.cancel();

                                    World world = Bukkit.getWorld(homedata.getWorld());
                                    Location loc = new Location(world, homedata.getX(), homedata.getY(), homedata.getZ());

                                    player.teleport(loc);
                                    String tpSucMsg = playerHomePlugin.getConfig().getString("messages.teleported_home");
                                    tpSucMsg = tpSucMsg.replace("%home%", homedata.getName());
                                    player.sendMessage(tpSucMsg);

                                    tpCooldown.put(uuid, System.currentTimeMillis());
                                }

                            }, 0, 20); // 20 ticks = 1 second
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        return true;
    }
}
