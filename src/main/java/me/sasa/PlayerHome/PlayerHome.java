package me.sasa.PlayerHome;

import me.sasa.PlayerHome.Commands.*;
import me.sasa.PlayerHome.Databases.HomesDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class PlayerHome extends JavaPlugin {

    private HomesDatabase homeDatabase;
    private static PlayerHome instance;

    @Override
    public void onEnable() {
        instance = this; // save instance

        getLogger().info("Home Plugin Started!");

        saveDefaultConfig();

        HomeTabCompleter tabCompleter = new HomeTabCompleter(this);
        getCommand("delhome").setTabCompleter(tabCompleter);
        getCommand("home").setTabCompleter(tabCompleter);

        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("test").setExecutor(new Test(this));

        try {
            homeDatabase = new HomesDatabase(getDataFolder().getAbsolutePath() + "/homes.db");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Failed to connect to database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            if (homeDatabase != null)
                homeDatabase.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HomesDatabase getHomeDatabase() {
        return homeDatabase;
    }

    public static PlayerHome getInstance() {
        return instance;
    }
}
