package greenlink.listeners;

import greenlink.PlayerManager;
import greenlink.fruits.Fruit;
import greenlink.fruits.FruitEnum;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Map;
import java.util.UUID;

public class FlightToggleListener implements Listener {

    @EventHandler
    public void onFlightToggle(PlayerToggleFlightEvent event) {

        Player player = event.getPlayer();

        if (PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit() == null) return;
        if (PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().equals(FruitEnum.PIKA) || PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().equals(FruitEnum.MERA)) {

            Map<UUID, Long> spaceCoolDown = Fruit.spaceCoolDown;

            if (spaceCoolDown.containsKey(player.getUniqueId())) {
                if (spaceCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                    event.setCancelled(true);

                    long timeLeft = (spaceCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                    player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Cooldown: " + ChatColor.GRAY + timeLeft + "s");
                    return;
                }
            }

            PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().getFruitPowers().onSpacePower(player);
        }
    }

}
