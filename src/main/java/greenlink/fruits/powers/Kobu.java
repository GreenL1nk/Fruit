package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kobu extends Fruit {

    public Kobu() {
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

        for (Entity nearbyEntity : player.getNearbyEntities(5, 0, 5)) {
            if (nearbyEntity instanceof LivingEntity) {
                ((LivingEntity) nearbyEntity).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 0));
                ((LivingEntity) nearbyEntity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 0));
            }
        }


        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 0));

    }

    @Override
    public void onLeftClick(Player player) {

    }
}
