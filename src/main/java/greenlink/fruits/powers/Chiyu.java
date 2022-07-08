package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Chiyu extends Fruit {

    public Chiyu() {
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
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
    }

    @Override
    public void onLeftClick(Player player) {

    }
}
