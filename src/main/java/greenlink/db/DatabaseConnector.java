package greenlink.db;


import greenlink.FruitPlayer;
import greenlink.FruitsMain;
import greenlink.fruits.FruitEnum;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseConnector {

    private String user;
    private String password;
    private String url;
    private Connection connection;

    private static DatabaseConnector instance;

    private static String sql = "";

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private DatabaseConnector() {
        try {

            FileConfiguration config = FruitsMain.getInstance().getConfig();
            if(config.getBoolean("mySQL.enable")) {
                sql = "mySQL";
                String host = config.getString("mySQL.host");
                int port = config.getInt("mySQL.port");
                user = config.getString("mySQL.user");
                String dbname = config.getString("mySQL.dbname");
                password = config.getString("mySQL.password");
                url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;

                try {
                    Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
                    connection = getConnection();
                    Bukkit.getServer().getLogger().log(Level.INFO, "[Fruits]Using mySQL");
                }
                catch (Exception e){
                    sql = "sqlite";
                    url = "jdbc:sqlite:" + FruitsMain.getInstance().getDataFolder() + File.separator + "database.db";
                    connection = getConnection();
                    Bukkit.getServer().getLogger().log(Level.INFO, "[Fruits]Failed connection to mySQL. Using sqlite");
                    Bukkit.getServer().getLogger().log(Level.INFO, "" + e.getMessage());
                }
            }
            else {

                sql = "sqlite";
                url = "jdbc:sqlite:" + FruitsMain.getInstance().getDataFolder() + File.separator + "database.db";
                connection = getConnection();
                Bukkit.getServer().getLogger().log(Level.INFO, "[Fruits]Using sqlite");

            }

            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `fruits_players` (\n" +
                        "\t`uuid` VARCHAR(36) NOT NULL,\n" +
                        "\t`level` INT NULL DEFAULT NULL,\n" +
                        "\t`xp` INT NULL DEFAULT NULL,\n" +
                        "\t`fruit` VARCHAR(16) NULL DEFAULT NULL,\n" +
                        "\t`leave_time` LONG NULL DEFAULT NULL,\n" +
                        "\tPRIMARY KEY (`uuid`)\n" +
                        ");");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `fruit_holders` (\n" +
                    "\t`fruit` VARCHAR(16) NOT NULL,\n" +
                    "\t`holder_uuid` VARCHAR(36) NULL DEFAULT NULL,\n" +
                    "\tPRIMARY KEY (`fruit`)\n" +
                    ");");

//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `players_to_remove` (\n" +
//                    "\t`uuid` VARCHAR(16) NOT NULL,\n" +
//                    "\tPRIMARY KEY (`uuid`)\n" +
//                    ");");

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FruitPlayer getPlayer(UUID uuid) {

        FruitPlayer fruitPlayer;
        try {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(String.format("SELECT level, xp, fruit, leave_time FROM fruits_players WHERE uuid='%s'", uuid));

            if(!result.next()) {
                fruitPlayer = new FruitPlayer(uuid);
                String sql = String.format("INSERT INTO fruits_players (uuid, level, xp, fruit, leave_time) VALUES ('%s', %d, %d, '%s', '%d')",
                        uuid, 0, 0, null, 0L);
                statement.executeUpdate(sql);
            }
            else {
                try {
                    fruitPlayer = new FruitPlayer(uuid, result.getInt("level"), result.getInt("xp"), FruitEnum.valueOf(result.getString("fruit")), result.getLong("leave_time"));
                }
                catch (IllegalArgumentException e) {
                    fruitPlayer = new FruitPlayer(uuid, result.getInt("level"), result.getInt("xp"), null, result.getLong("leave_time"));
                }
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            fruitPlayer = new FruitPlayer(uuid);
        }
        return fruitPlayer;
    }


    public Long getPlayerLeaveTime(UUID uuid){
        try {

            Statement statement = this.connection.createStatement();
            String sqlRequest = String.format("SELECT leave_time FROM fruits_players WHERE uuid='%s'", uuid);
            ResultSet result = statement.executeQuery(sqlRequest);
            if (!result.next()) {
                return null;
            } else {
                return result.getLong("leave_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveFruitPlayer(FruitPlayer fruitPlayer, long leaveTime) {
        try {
            Statement statement = connection.createStatement();

            if (sql.equals("sqlite")) {
                statement.executeUpdate(String.format("INSERT INTO fruits_players (uuid, level, xp, fruit, leave_time) VALUES ('%s', %d, %d, '%s', '%d') " +
                                "ON CONFLICT(uuid) DO UPDATE SET level='%d', xp='%d', fruit='%s', leave_time='%d'",
                        fruitPlayer.getUuid(), fruitPlayer.getLevel(), fruitPlayer.getXp(), fruitPlayer.getActiveFruit(), leaveTime,
                        fruitPlayer.getLevel(), fruitPlayer.getXp(), fruitPlayer.getActiveFruit(), leaveTime));
            }
            else {
                statement.executeUpdate(String.format("INSERT INTO fruits_players (uuid, level, xp, fruit, leave_time) VALUES ('%s', %d, %d, '%s', '%d') " +
                                "ON DUPLICATE KEY UPDATE uuid='%s', level='%d', xp='%d', fruit='%s', leave_time='%d'",
                        fruitPlayer.getUuid(), fruitPlayer.getLevel(), fruitPlayer.getXp(), fruitPlayer.getActiveFruit(), leaveTime,
                        fruitPlayer.getUuid(), fruitPlayer.getLevel(), fruitPlayer.getXp(), fruitPlayer.getActiveFruit(), leaveTime));
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return this.user != null ? DriverManager.getConnection(this.url, this.user, this.password) : DriverManager.getConnection(this.url);
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
