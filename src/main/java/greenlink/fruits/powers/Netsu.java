package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Netsu extends Fruit {

    public Netsu() {
    }

    @Override
    public void onRightClick(Player player) {

    }

    @Override
    public void onLeftClick(Player player) {

    }

    @Override
    public void passiveOnEntityDamage(Player player, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) event.setCancelled(true);
    }

    @Override
    public void passiveOnDamageTaken(Player player, Entity damager, EntityDamageByEntityEvent event) {
        damager.setFireTicks(80);
    }
}
