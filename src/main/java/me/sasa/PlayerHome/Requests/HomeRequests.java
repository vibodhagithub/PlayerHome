package me.sasa.PlayerHome.Requests;

import me.sasa.PlayerHome.Databases.HomesDatabase;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeRequests {

    public static int getMaxHomes(Player player) {
        LuckPerms lp = LuckPermsProvider.get();

        User user = lp.getUserManager().getUser(player.getUniqueId());
        if (user == null) return 1;

        String groupName = user.getPrimaryGroup();
        Group group = lp.getGroupManager().getGroup(groupName);
        if (group == null) return 1;

        int maxHomes = 1;

        for (Node node : group.getNodes()) {
            String key = node.getKey();
            if (key.startsWith("playerhome.maxhomes.") && node.getValue()) {
                try {
                    int value = Integer.parseInt(key.split("\\.")[2]);
                    if (value > maxHomes) maxHomes = value;
                } catch (NumberFormatException ignored) {}
            }
        }

        return maxHomes;
    }

}
