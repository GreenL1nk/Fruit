package greenlink.listeners;

import com.destroystokyo.paper.MaterialTags;
import greenlink.FruitPlayer;
import greenlink.FruitsMain;
import greenlink.PlayerManager;
import greenlink.enchantments.EnchantEnum;
import greenlink.fruits.FruitEnum;
import greenlink.fruits.powers.Pika;
import greenlink.items.ItemEnum;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ActionListener implements Listener {
    ArrayList<UUID> playerInWater = new ArrayList<>();

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
                    int total = 0;
                    for (Map.Entry<FruitEnum, Integer> chance : FruitsMain.fruitDropChances.entrySet()) {
                        total += chance.getValue();
                    }
                    int dropChance = r.nextInt(0, total);

                    int totalDummy = 0;
                    for (Map.Entry<FruitEnum, Integer> chance : FruitsMain.fruitDropChances.entrySet()) {
                        if (dropChance < chance.getValue() + totalDummy && dropChance >= totalDummy) {
                            event.getDrops().add(chance.getKey().getFruitStack());
                            return;
                        }
                        else totalDummy += chance.getValue();
                    }
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

            ItemEnum itemEnum = Arrays.stream(ItemEnum.values()).filter(item -> item.getItem().equals(event.getItem())).findFirst().orElse(null);

            if(itemEnum != null) {
                itemEnum.getItem().onUse(event);
                event.setCancelled(true);
            }

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

        if (fruitPlayer.getActiveFruit() == null && !FruitEnum.fruitInInventory(player)) return;

        if(fruitPlayer.getActiveFruit() != null) {
            fruitPlayer.getActiveFruit().getFruitPowers().onDeath(player);
            playerInWater.remove(player.getUniqueId());

            fruitPlayer.getActiveFruit().getFruitPowers().resetCoolDowns(player);
        }
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

        Entity damager = event.getDamager();

        BukkitAPIHelper mythicMobsAPI = new BukkitAPIHelper();
        if (mythicMobsAPI.isMythicMob(event.getEntity()) && damager instanceof Player) {
            ActiveMob mythicMobInstance = mythicMobsAPI.getMythicMobInstance(event.getEntity());
            FruitPlayer player = PlayerManager.getInstance().getPlayer(damager.getUniqueId());
            if (player.getLevel() < mythicMobInstance.getLevel()) {
                damager.sendMessage(ChatColor.RED + "You cannot attack a mob of a level higher than you");
                event.setCancelled(true);
            }
        }

        if (damager instanceof Player) {
            ItemStack itemInMainHand = ((Player) damager).getInventory().getItemInMainHand();
            for (EnchantEnum value : EnchantEnum.values()) {
                if (!itemInMainHand.hasItemMeta()) break;
                if (itemInMainHand.getItemMeta().hasEnchant(value.getEnchant())) {
                    value.getEnchant().onHit(event.getEntity(), (Player) damager, event);
                }
            }
        }

        if (!(event.getEntity() instanceof Player)) return;
        Player playerWhoDamaged = ((Player) event.getEntity()).getPlayer();

        if (playerWhoDamaged == null) return;

        FruitPlayer fruitPlayerWhoDamaged = PlayerManager.getInstance().getPlayer(playerWhoDamaged.getUniqueId());
        if(fruitPlayerWhoDamaged.getActiveFruit() == null) return;

        fruitPlayerWhoDamaged.getActiveFruit().getFruitPowers().passiveOnDamageTaken(playerWhoDamaged, damager, event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock();
        if(PlayerManager.getInstance().getPlayer(player.getUniqueId()).getActiveFruit() == null) return;

        if (block.getType() == Material.WATER) {

            if (!playerInWater.contains(player.getUniqueId())) {
                playerInWater.add(player.getUniqueId());
                FruitsMain.getInstance().getServer().getScheduler().runTaskLater(FruitsMain.getInstance(), () -> {
                    if (player.getLocation().getBlock().getType() == Material.WATER) {
                        player.damage(2);
                    }
                    playerInWater.remove(player.getUniqueId());
                }, 15L);
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
    public void onAnvil(PrepareAnvilEvent event) {
        ItemStack firstItem = event.getInventory().getFirstItem();
        ItemStack secondItem = event.getInventory().getSecondItem();
        if (firstItem != null && secondItem != null) {
            for (EnchantEnum value : EnchantEnum.values()) {
                if (secondItem.getItemMeta().hasEnchant(value.getEnchant()) && MaterialTags.SWORDS.isTagged(firstItem)) {
                    if (EnchantEnum.countEnchantsOnItem(firstItem) >= 3) return;
                    ItemStack itemStack = firstItem.clone();
                    int enchantmentLevel = secondItem.getEnchantmentLevel(value.getEnchant());
                    itemStack.addUnsafeEnchantment(value.getEnchant(), enchantmentLevel);
                    List<Component> lore = new ArrayList<>();
                    if (itemStack.getItemMeta().hasLore()) {
                        lore.addAll(Objects.requireNonNull(itemStack.lore()));
                    }
                    lore.add(value.getEnchant().displayName(enchantmentLevel));
                    itemStack.lore(lore);
                    event.getInventory().setRepairCost(5);
                    event.setResult(itemStack);
                }
                else if (firstItem.equals(secondItem) && firstItem.getItemMeta().hasEnchant(value.getEnchant())) {
                    ItemStack itemStack = firstItem.clone();
                    int enchantmentLevel = firstItem.getEnchantmentLevel(value.getEnchant()) + 1;
                    if (enchantmentLevel > value.getEnchant().getMaxLevel()) return;
                    itemStack.lore(Collections.singletonList(value.getEnchant().displayName(enchantmentLevel)));
                    itemStack.addUnsafeEnchantment(value.getEnchant(), enchantmentLevel);
                    event.getInventory().setRepairCost(5);
                    event.setResult(itemStack);
                }
            }
        }
    }

    @EventHandler
    public void test(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equals("/test") && event.getPlayer().isOp()) {

        }
    }
}
