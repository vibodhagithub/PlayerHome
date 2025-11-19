package me.sasa.PlayerHome.Databases;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class HomesDatabase {

    private final Connection connection;

    public HomesDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS players (
                        uuid TEXT PRIMARY KEY,
                        role TEXT DEFAULT 'default'
                    );
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS homes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        player_uuid TEXT,
                        homename TEXT,
                        world TEXT,
                        x DOUBLE,
                        y DOUBLE,
                        z DOUBLE,
                        yaw FLOAT,
                        pitch FLOAT
                    );
                    """);
        }
    }

    public class HomeData {
        private final String homename;
        private final String world;
        private final double x;
        private final double y;
        private final double z;
        private final double yaw;
        private final double pitch;

        public HomeData(String homename, String world, double x, double y, double z, double yaw, double pitch) {
            this.homename = homename;
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public String getName() { return homename; }
        public String getWorld() { return world; }
        public double getX() { return x; }
        public double getY() { return y; }
        public double getZ() { return z; }
    }


    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public boolean homeExists(Player player, String homename) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM homes WHERE player_uuid = ? AND homename = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, homename);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public int getHomeCount(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM homes WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public List<String> getHomeNamesArray(Player player) throws SQLException {
        List<String> homes = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT homename FROM homes WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    homes.add(resultSet.getString("homename"));
                }
            }
        }
        return homes;
    }

    public void addHome(Player player, String homename) throws SQLException {

           if (!playerExists(player)) {
               try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid, role) VALUES (?, ?)")) {
                   preparedStatement.setString(1, player.getUniqueId().toString());
                   preparedStatement.setString(2, "default");
                   preparedStatement.executeUpdate();
               }
           }
        Location loc = player.getLocation();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO homes (player_uuid, homename, world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?,?,?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, homename);
            preparedStatement.setString(3, loc.getWorld().getName());
            preparedStatement.setString(4, String.valueOf(loc.getX()));
            preparedStatement.setString(5, String.valueOf(loc.getY()));
            preparedStatement.setString(6, String.valueOf(loc.getZ()));
            preparedStatement.setString(7, String.valueOf(loc.getYaw()));
            preparedStatement.setString(8, String.valueOf(loc.getPitch()));
            preparedStatement.executeUpdate();
        }
    }

    public void delHome(Player player, String homename) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM homes WHERE player_uuid = ? AND homename = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, homename);
            preparedStatement.executeUpdate();
        }
    }

    public HomeData getHomeData(Player player, String homename) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM homes WHERE player_uuid = ? AND homename = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, homename);
            ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new HomeData(
                            resultSet.getString("homename"),
                            resultSet.getString("world"),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getDouble("yaw"),
                            resultSet.getDouble("pitch")
                    );
                }
        }
        return null;
    }

    public List<HomeData> getPlayerAllHomes(Player player) throws SQLException {
        List<HomeData> homes = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM homes WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    HomeData home = new HomeData(
                            resultSet.getString("homename"),
                            resultSet.getString("world"),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getDouble("yaw"),
                            resultSet.getDouble("pitch")
                    );
                    homes.add(home);
                }
            }
        }

        return homes;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
