package greenlink.listeners;

import greenlink.FruitPlayer;
import greenlink.PlayerManager;
import greenlink.fruits.FruitEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        FruitPlayer fruitPlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());

        fruitPlayer.setLeaveTime(null);
        FruitEnum activeFruit = PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit();
        if (activeFruit != null) activeFruit.getFruitPowers().onJoin(player);
    }
}
