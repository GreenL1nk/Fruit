package greenlink;


import greenlink.db.DatabaseConnector;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    private final HashMap<UUID, FruitPlayer> players = new HashMap<>();
    private final DatabaseConnector dataBase;
    private static PlayerManager instance;

    public PlayerManager() {
        dataBase = DatabaseConnector.getInstance();
    }

    public static PlayerManager getInstance() {
        if(instance == null) instance = new PlayerManager();
        return instance;
    }

    public FruitPlayer getPlayer(UUID uuid) {
        FruitPlayer fruitPlayer = players.get(uuid);

        if(fruitPlayer == null) {
            fruitPlayer = dataBase.getPlayer(uuid);
            players.put(uuid, fruitPlayer);
        }
        return fruitPlayer;
    }

    public void savePlayer(UUID uuid, long leaveTime) {
        dataBase.saveFruitPlayer(players.get(uuid), leaveTime);
    }
}
