package greenlink.fruits.powers;

import greenlink.FruitsMain;
import greenlink.fruits.Fruit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Suke extends Fruit {

    public static ArrayList<Player> vanish_list = new ArrayList<>();

    public Suke() {
    }

    @Override
    public void onRightClick(Player player) {
        if (rightCoolDown.containsKey(player.getUniqueId())) {
            if (rightCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (rightCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка: " + ChatColor.GRAY + timeLeft + "с");

                return;
            }
        }

        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1000 * 60));
        player.hidePlayer(FruitsMain.getInstance(), player);

        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayer.hidePlayer(FruitsMain.getInstance(), player);
        }
        vanish_list.add(player);

        FruitsMain.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(FruitsMain.getInstance(), () -> {
            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                onlinePlayer.showPlayer(FruitsMain.getInstance(), player);
                vanish_list.remove(player);
            }
        }, 300L); //15sec
    }

    @Override
    public void onLeftClick(Player player) {

    }

    @Override
    public void onJoin(Player player) {
        for (Player vanishPlayer : Suke.vanish_list) {
            player.hidePlayer(FruitsMain.getInstance(), vanishPlayer);
        }
    }
}
