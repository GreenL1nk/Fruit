package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ton extends Fruit {

    public Ton() {
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

        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (500 * 60));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 2));
    }

    @Override
    public void onLeftClick(Player player) {

    }
}
