package greenlink.listeners;

import greenlink.FruitPlayer;
import greenlink.FruitsMain;
import greenlink.PlayerManager;
import greenlink.fruits.FruitEnum;
import org.bukkit.ChatColor;
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

        if (FruitsMain.getInstance().playersToRemoveFruits.contains(fruitPlayer)) {
            for (FruitEnum fruit : FruitEnum.values()) {
                if (player.getInventory().contains(fruit.getFruitStack())) {
                    player.getInventory().remove(fruit.getFruitStack());
                }
            }
            player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "You've been gone too long. Your powers are lost");
            FruitsMain.getInstance().playersToRemoveFruits.remove(fruitPlayer);
        }

        PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().getFruitPowers().onJoin(player);
    }
}
