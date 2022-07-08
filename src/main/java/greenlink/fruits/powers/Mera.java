package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;

public class Mera extends Fruit {

    public Mera() {
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

        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1500 * 60));

        for (Entity nearbyEntity : player.getNearbyEntities(10, 0, 10)) {
            if (nearbyEntity instanceof LivingEntity) {
                nearbyEntity.setFireTicks(80);
            }
        }
    }

    @Override
    public void onLeftClick(Player player) {
        if (leftCoolDown.containsKey(player.getUniqueId())) {
            if (leftCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (leftCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка " + ChatColor.DARK_GRAY + ChatColor.BOLD + "огненного шара: " + ChatColor.GRAY + timeLeft + "с");

                return;
            }
        }

        leftCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (250 * 60));

        Location eye = player.getPlayer().getEyeLocation();
        Location loc = eye.add(eye.getDirection().multiply(1.2));

        Fireball fireball = (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);

        fireball.setVelocity(loc.getDirection().normalize().multiply(1));
        fireball.setShooter(player);
    }

    @Override
    public void passiveOnEntityDamage(Player player, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) event.setCancelled(true);
    }

    @Override
    public void passiveWalk(Player player) {
        if (player.isFlying()) {
            Location location = player.getLocation();
            location.getWorld().spawnParticle(Particle.LAVA, location.add(0, -0.2, 0), 1, 0, 0, 0, 1);
        }
    }

    @Override
    public void onSpacePower(Player player) {
        player.setFlySpeed(0.15f);
    }

    @Override
    public void onActivateFruit(Player player) {
        player.setAllowFlight(true);
    }

    @Override
    public void onJoin(Player player) {
        player.setAllowFlight(true);
    }

    @Override
    public void onDeath(Player player) {
        player.setAllowFlight(false);
    }
}
