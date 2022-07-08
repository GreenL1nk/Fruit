package greenlink.fruits.powers;

import greenlink.FruitsMain;
import greenlink.fruits.Fruit;
import greenlink.utils.GetBlocks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Magu extends Fruit {

    public Magu() {
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

        ArrayList<Block> blocksInRadius = GetBlocks.getBlocksInRadius(player.getLocation().getBlock(), 5);

        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1000 * 60));

        for (Block block : blocksInRadius) {
            if (block.getType() == Material.AIR) {
                block.setType(Material.LAVA, false);
            }
        }

        Bukkit.getScheduler().runTaskLater(FruitsMain.getInstance(), () -> {

            for (Block block : blocksInRadius) {
                if (block.getType() == Material.LAVA) block.setType(Material.AIR);
            }

        }, 3 * 20L);
    }

    @Override
    public void onLeftClick(Player player) {
        if (leftCoolDown.containsKey(player.getUniqueId())) {
            if (leftCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (leftCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Перезарядка: " + ChatColor.GRAY + timeLeft + "с");

                return;
            }
        }

        leftCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (5000));

        Vector jump = player.getLocation().getDirection().multiply(0.01).setY(1.1);
        player.setVelocity(player.getVelocity().add(jump));

        for (Entity nearbyEntity : player.getNearbyEntities(5, 0, 5)) {
            if (nearbyEntity instanceof LivingEntity) {
                nearbyEntity.setFireTicks(80);
            }
        }
    }

    @Override
    public void passiveOnEntityDamage(Player player, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                || event.getCause() == EntityDamageEvent.DamageCause.LAVA) event.setCancelled(true);
    }

    @Override
    public void passiveWalk(Player player) {
        Block underBlock = player.getLocation().add(0, -1 ,0).getBlock();

        ArrayList<Block> blocksInRadius = GetBlocks.getBlocksInRadius(underBlock, 1);

        for (Block block : blocksInRadius) {
            if (block.getBlockData().getMaterial().equals(Material.WATER)) {
                block.setType(Material.OBSIDIAN);
                FruitsMain.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(FruitsMain.getInstance(), () -> {
                    block.setType(Material.WATER);
                }, 150L);
            }
        }
    }
}
