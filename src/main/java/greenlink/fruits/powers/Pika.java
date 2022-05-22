package greenlink.fruits.powers;

import greenlink.FruitsMain;
import greenlink.fruits.Fruit;
import greenlink.utils.GetBlocks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class Pika extends Fruit {

    public Pika() {
    }

    public static NamespacedKey namespacedKeyLeft = new NamespacedKey(FruitsMain.getInstance(), "left");
    NamespacedKey namespacedKey = new NamespacedKey(FruitsMain.getInstance(), "noDMG");

    Random r = new Random();

    @Override
    public void onRightClick(Player player) {
        if (rightCoolDown.containsKey(player.getUniqueId())) {
            if (rightCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (rightCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Cooldown " + ChatColor.DARK_GRAY + ChatColor.BOLD + "the storm of spectral arrows: " + ChatColor.GRAY + timeLeft + "s");

                return;
            }
        }
        rightCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1000 * 60));

        new BukkitRunnable() {
            int countWaves = 3;

            @Override
            public void run() {
                ArrayList<Block> blocksInRadius1 = GetBlocks.getBlocksInRadius(player.getLocation().getBlock(), 10);

                for (Block block : blocksInRadius1) {
                    Location location = block.getLocation();
                    location.setY(location.getY() + 8);

                    int countArrowsPerBlock = r.nextInt(2) + 1;

                    World world = location.getWorld();

                    for (int i1 = 0; i1 < countArrowsPerBlock; i1++) {
                        SpectralArrow spectral = (SpectralArrow) world.spawnEntity(location, EntityType.SPECTRAL_ARROW);
                        spectral.setShooter(player);
                        spectral.setVelocity(new Vector(0, -3, 0));
                        spectral.setPickupStatus(SpectralArrow.PickupStatus.DISALLOWED);
                        spectral.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "PIKA");

                        Bukkit.getScheduler().runTaskLater(FruitsMain.getInstance(), spectral::remove, 3 * 20L);
                    }
                }
                countWaves--;
                if (countWaves == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(FruitsMain.getInstance(), 20L, 20L);
    }

    @Override
    public void onLeftClick(Player player) {
        if (leftCoolDown.containsKey(player.getUniqueId())) {
            if (leftCoolDown.get(player.getUniqueId()) > System.currentTimeMillis()) {

                long timeLeft = (leftCoolDown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Cooldown " + ChatColor.DARK_GRAY + ChatColor.BOLD + "the spectral arrow: " + ChatColor.GRAY + timeLeft + "s");

                return;
            }
        }
        leftCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (70000));

        new BukkitRunnable() {

            int arrows = 15;

            @Override
            public void run() {
                Location eyeLoc = player.getPlayer().getEyeLocation();
                Location location = eyeLoc.add(eyeLoc.getDirection().multiply(1.5));
                SpectralArrow spectralArrow = (SpectralArrow) location.getWorld().spawnEntity(location, EntityType.SPECTRAL_ARROW);
                spectralArrow.setVelocity(location.getDirection().normalize().multiply(5.5)); //1.5

                int spreadF = r.nextInt(2);
                int spreadT = r.nextInt(2);

                spectralArrow.setVelocity(location.getDirection().normalize().rotateAroundX(spreadF));
                spectralArrow.setVelocity(location.getDirection().normalize().rotateAroundZ(spreadT));

                spectralArrow.setShooter(player);
                spectralArrow.setPickupStatus(SpectralArrow.PickupStatus.DISALLOWED);
                spectralArrow.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "PIKA");
                spectralArrow.getPersistentDataContainer().set(namespacedKeyLeft, PersistentDataType.STRING, "left");

                arrows--;
                if (arrows == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(FruitsMain.getInstance(), 0L, 4L);
    }

    @Override
    public void passiveOnDamageTaken(Player player, Entity damager, EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.SPECTRAL_ARROW && damager.getPersistentDataContainer().has(namespacedKey)) {
            event.getDamager().remove();
            event.setCancelled(true);
        }
    }

    @Override
    public void onSpacePower(Player player) {

        player.setFlySpeed(0.15f);

        FruitsMain.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(FruitsMain.getInstance(), () -> {
            player.setAllowFlight(false);
            player.setAllowFlight(true);
            if (!Fruit.spaceCoolDown.containsKey(player.getUniqueId())) Fruit.spaceCoolDown.put(player.getUniqueId(), System.currentTimeMillis() + (1750 * 60));
        }, 300L);
    }

    @Override
    public void onJoin(Player player) {
        player.setAllowFlight(true);
    }

    @Override
    public void onDeath(Player player) {
        player.setAllowFlight(false);
    }

    @Override
    public void onActivateFruit(Player player) {
        player.setAllowFlight(true);
    }
}
