package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gomu extends Fruit {

    public Gomu() {
    }

    @Override
    public void onRightClick(Player player) {
        if (rightCoolDown.containsKey(player.getUniqueId())) {
            if (rightCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (rightCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка " + ChatColor.DARK_GRAY + ChatColor.BOLD + "высокого прыжка: " + ChatColor.GRAY + timeLeft + "с");

                return;
            }
        }

        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1000 * 60));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2400, 2));

    }

    @Override
    public void onLeftClick(Player player) {
        if (leftCoolDown.containsKey(player.getUniqueId())) {
            if (leftCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (leftCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка " + ChatColor.DARK_GRAY + ChatColor.BOLD + "длинного прыжка: " + ChatColor.GRAY + timeLeft + "с");

                return;
            }
        }

        leftCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1000 * 60));
        player.setVelocity(player.getLocation().getDirection().multiply(10));
    }
}
