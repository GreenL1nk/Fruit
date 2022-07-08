package greenlink.fruits.powers;

import greenlink.fruits.Fruit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class Goro extends Fruit {

    public Goro() {
    }

    @Override
    public void onRightClick(Player player) {
        if (rightCoolDown.containsKey(player.getUniqueId())) {
            if (rightCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (rightCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка " + ChatColor.DARK_GRAY + ChatColor.BOLD + "круг молний: " + ChatColor.GRAY + timeLeft + "с");

                return;
            }
        }
        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (2000 * 60));

        for (Entity nearbyEntity : player.getNearbyEntities(4, 0, 4)) {
            if (nearbyEntity instanceof LivingEntity) {
                World world = nearbyEntity.getWorld();
                Location location = nearbyEntity.getLocation();
                world.strikeLightning(location);
            }
        }
    }

    @Override
    public void onLeftClick(Player player) {
        Block targetBlock = player.getTargetBlock(null, 100);

        if (targetBlock.getType() != Material.AIR) {

            if (leftCoolDown.containsKey(player.getUniqueId())) {
                if (leftCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                    long timeLeft = (leftCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                    player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка " + ChatColor.DARK_GRAY + ChatColor.BOLD + "удар молнии: " + ChatColor.GRAY + timeLeft + "с");

                    return;
                }
            }

            leftCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (500 * 60));

            Location location = targetBlock.getLocation();
            location.setY(location.getBlockY()+1);
            World world = targetBlock.getWorld();
            world.strikeLightning(location);
        }
    }

    @Override
    public void passiveOnEntityDamage(Player player, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) event.setCancelled(true);
    }
}
