package greenlink.listeners;

import greenlink.FruitPlayer;
import greenlink.FruitsMain;
import greenlink.PlayerManager;
import greenlink.fruits.FruitEnum;
import greenlink.fruits.powers.Pika;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class ActionListener implements Listener {

    HashMap<UUID, Long> playerInWater = new HashMap<>();

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (entity instanceof Monster) {
            if (killer != null) {
                double minX = 0f;
                double maxX = 100.0f;

                Random r = new Random();
                double randomNum = r.nextFloat() * (maxX - minX) + minX;

                if (randomNum <= (FruitsMain.getInstance().getConfig().getDouble("fruit.drop_chance"))) {
                    FruitEnum fruit = FruitEnum.getRandomNotActiveFruit();
                    if (fruit != null) event.getDrops().add(fruit.getFruitStack());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickUpItem(EntityPickupItemEvent event) {

        if (event.getEntity() instanceof Player) {
            if (event.getItem().getItemStack().getType() != Material.CHORUS_FRUIT) return;

            for (FruitEnum fruit : FruitEnum.values()) {
                if (fruit.getFruitStack().equals(event.getItem().getItemStack())) {
                    fruit.setHolder(PlayerManager.getInstance().getPlayer(event.getEntity().getUniqueId()));
                    fruit.setActive(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {

            FruitPlayer fruitPlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            if (fruitPlayer.getActiveFruit() != null && mainHandItem.getType().isAir() && offHandItem.getType().isAir()) {

                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().getFruitPowers().onRightClick(player);
                    return;
                }
                PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().getFruitPowers().onLeftClick(player);
            }

            else if (event.hasItem()) {
                if (event.getItem().getItemMeta() == null) return;
                FruitEnum fruit = Arrays.stream(FruitEnum.values()).filter(f -> f.getFruitStack().getItemMeta().equals(event.getItem().getItemMeta())).findFirst().orElse(null);

                if (fruit != null) {

                    event.setCancelled(true);

                    if (fruitPlayer.getActiveFruit() != null) {
                        player.sendMessage(ChatColor.DARK_GRAY + "You already have an active fruit");
                        return;
                    }
                    if (fruit.getFruitStack().getItemMeta().equals(offHandItem.getItemMeta())) {
                        fruitPlayer.setActiveFruit(fruit);
                        offHandItem.setAmount(offHandItem.getAmount() - 1);

                        fruitPlayer.getActiveFruit().getFruitPowers().onActivateFruit(player);
                        return;
                    }
                    fruitPlayer.setActiveFruit(fruit);
                    mainHandItem.setAmount(mainHandItem.getAmount() - 1);

                    fruitPlayer.getActiveFruit().getFruitPowers().onActivateFruit(player);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity().getPlayer();

        if (player == null) return;

        FruitPlayer fruitPlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());

        if (fruitPlayer.getActiveFruit() == null && !FruitEnum.playerHaveFruits(fruitPlayer)) return;

        if(fruitPlayer.getActiveFruit() != null) {
            fruitPlayer.getActiveFruit().getFruitPowers().onDeath(player);
            playerInWater.remove(player.getUniqueId());

            //TODO: Подумать/Проверить, нужно ли вообще setActive()
            fruitPlayer.getActiveFruit().setActive(false);

            fruitPlayer.getActiveFruit().getFruitPowers().resetCoolDowns(player);
            fruitPlayer.removeActiveFruit();
        }

        for (FruitEnum fruit : FruitEnum.values()) {
            if (fruit.getHolder() != null) {
                if (fruit.getHolder().equals(fruitPlayer)) {
                    if (player.getInventory().contains(fruit.getFruitStack())) {
                        player.getInventory().remove(fruit.getFruitStack());
                        player.getWorld().dropItemNaturally(player.getLocation(), fruit.getFruitStack());
                    }
                    fruit.setHolder(null);
                }
            }
        }
        player.sendMessage("" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Your fruits and powers are lost");
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player){

            Player player = (Player) event.getEntity();

            FruitPlayer fruitPlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());

            if(fruitPlayer.getActiveFruit() == null) return;

            fruitPlayer.getActiveFruit().getFruitPowers().passiveOnEntityDamage(player, event);

        }
    }

    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent event) {


        BukkitAPIHelper mythicMobsAPI = new BukkitAPIHelper();
        if (mythicMobsAPI.isMythicMob(event.getEntity()) && event.getDamager() instanceof Player) {
            ActiveMob mythicMobInstance = mythicMobsAPI.getMythicMobInstance(event.getEntity());
            FruitPlayer player = PlayerManager.getInstance().getPlayer(event.getDamager().getUniqueId());
            if (player.getLevel() < mythicMobInstance.getLevel()) {
                event.getDamager().sendMessage(ChatColor.RED + "You cannot attack a mob of a level higher than you");
                event.setCancelled(true);
            }
        }

        if (!(event.getEntity() instanceof Player)) return;
        Player playerWhoDamaged = ((Player) event.getEntity()).getPlayer();

        if (playerWhoDamaged == null) return;

        FruitPlayer fruitPlayerWhoDamaged = PlayerManager.getInstance().getPlayer(playerWhoDamaged.getUniqueId());
        if(fruitPlayerWhoDamaged.getActiveFruit() == null) return;

        fruitPlayerWhoDamaged.getActiveFruit().getFruitPowers().passiveOnDamageTaken(playerWhoDamaged, event.getDamager(), event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock();
        if(PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit() == null) return;

        if (block.getType() == Material.WATER) {

            if (playerInWater.containsKey(player.getUniqueId())) if (System.currentTimeMillis() > playerInWater.get(player.getUniqueId())) {
                if (player.getLocation().add(0, -0.5, 0).getBlock().getType() == Material.WATER) {
                    player.teleport(player.getLocation().add(0, -0.3, 0));
                }
            }
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                event.setCancelled(true);
                if (!playerInWater.containsKey(player.getUniqueId())) {
                    playerInWater.put(player.getUniqueId(), System.currentTimeMillis() + 3000);
                }
            }

        }
        PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit().getFruitPowers().passiveWalk(player);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity hitEntity = event.getHitEntity();
        if (event.getEntity() instanceof SpectralArrow) {
            if (hitEntity instanceof Player) {
                FruitEnum activeFruit = PlayerManager.getInstance().getPlayer(hitEntity.getUniqueId()).getActiveFruit();
                if (activeFruit != null && activeFruit.equals(FruitEnum.PIKA)) return;
            }
            if (hitEntity != null && event.getEntity().getPersistentDataContainer().has(Pika.namespacedKeyLeft)) {
                hitEntity.getWorld().createExplosion(hitEntity.getLocation().getX(), hitEntity.getLocation().getY(), hitEntity.getLocation().getZ(), 2, false, false);
            }
        }
    }

    @EventHandler
    public void test(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equals("/test") && event.getPlayer().isOp()) {
        }
    }
}
