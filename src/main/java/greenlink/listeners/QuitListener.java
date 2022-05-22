package greenlink.listeners;

import greenlink.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long timeLeave = System.currentTimeMillis();

        PlayerManager.getInstance().savePlayer(player.getUniqueId(), timeLeave);
    }
}
