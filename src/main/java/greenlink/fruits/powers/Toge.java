package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class Toge extends Fruit {

    public Toge() {
    }

    @Override
    public void onRightClick(Player player) {

    }

    @Override
    public void onLeftClick(Player player) {

    }

    @Override
    public void passiveOnDamageTaken(Player player, Entity damager, EntityDamageByEntityEvent event) {
        if (damager instanceof LivingEntity) {
            LivingEntity castDamager = (LivingEntity) damager;

            Random r = new Random();
            int i = r.nextInt(100);
            if (i > 85) {
                int damage = r.nextInt(6 - 1) + 1;
                castDamager.damage(damage);
            }
        }
    }
}
