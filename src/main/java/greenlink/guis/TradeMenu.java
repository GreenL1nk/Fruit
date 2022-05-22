package greenlink.guis;

import greenlink.FruitsMain;
import greenlink.utils.AbstractInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class TradeMenu extends AbstractInventoryHolder {

    private static final Component NAME = Component.text("             Меню обмена");

    public static final List<Integer> requesterSlots = Arrays.asList(0, 9, 18, 27, 36, 1, 10, 19, 28, 37, 2, 11, 20, 29, 38, 3, 12, 21, 30, 39);
    public static final List<Integer> responserSlots = Arrays.asList(5, 14, 23, 32, 41, 6, 15, 24, 33, 42, 7, 16, 25, 34, 43, 8, 17, 26, 35, 44);

    private boolean responserReady = false;
    private boolean requesterReady = false;

    public TradeMenu(Player requester, Player responser) {
        super(NAME, 6, requester, responser);
        this.init();
    }

    @Override
    protected void init() {

        ItemStack iron_bars = new ItemStack(Material.IRON_BARS);
        ItemStack red_glass_pane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack red_concrete = new ItemStack(Material.RED_CONCRETE);
        ItemStack barrier = new ItemStack(Material.BARRIER);

        for (int i = 0; i < 6; i++) {
            ItemMeta itemMeta = iron_bars.getItemMeta();
            itemMeta.displayName(Component.text(""));
            iron_bars.setItemMeta(itemMeta);

            this.inventory.setItem(i * 9 + 4, iron_bars);
        }
        this.inventory.setItem(4, iron_bars);

        for (int i = 0; i < 3; i++) {
            ItemMeta itemMeta = red_glass_pane.getItemMeta();
            itemMeta.displayName(Component.text(ChatColor.RED + "NOT READY"));
            red_glass_pane.setItemMeta(itemMeta);

            this.inventory.setItem(45 + i, red_glass_pane);
            this.inventory.setItem(51 + i, red_glass_pane);
        }
        ItemMeta red_concreteItemMeta = red_concrete.getItemMeta();
        red_concreteItemMeta.displayName(Component.text(ChatColor.YELLOW + "CLICK TO READY"));
        red_concrete.setItemMeta(red_concreteItemMeta);

        ItemMeta barrierItemMeta = barrier.getItemMeta();
        barrierItemMeta.displayName(Component.text(ChatColor.RED + "CANCEL"));
        barrier.setItemMeta(barrierItemMeta);

        this.inventory.setItem(48, red_concrete);
        this.inventory.setItem(50, red_concrete);
        this.inventory.setItem(49, barrier);
    }

    @Override
    public void click(InventoryClickEvent event) {

        boolean notRequesterSlot = event.getWhoClicked() == requester && requesterSlots.stream().noneMatch(i -> i == event.getRawSlot());
        boolean notResponserSlot = event.getWhoClicked() == responser && responserSlots.stream().noneMatch(i -> i == event.getRawSlot());
        if (notRequesterSlot) event.setCancelled(true);
        if (notResponserSlot) event.setCancelled(true);

        if (event.getAction() != InventoryAction.NOTHING && (event.getWhoClicked() == requester && requesterSlots.stream().anyMatch(integer -> integer == event.getRawSlot())) || event.getWhoClicked() == responser && responserSlots.stream().anyMatch(integer -> integer == event.getRawSlot())) {
            responserReady = false;
            requesterReady = false;

            ItemStack red_glass_pane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemStack red_concrete = new ItemStack(Material.RED_CONCRETE);
            for (int i = 0; i < 3; i++) {
                ItemMeta itemMeta = red_glass_pane.getItemMeta();
                itemMeta.displayName(Component.text(ChatColor.RED + "NOT READY"));
                red_glass_pane.setItemMeta(itemMeta);

                this.inventory.setItem(45 + i, red_glass_pane);
                this.inventory.setItem(51 + i, red_glass_pane);
            }
            ItemMeta red_concreteItemMeta = red_concrete.getItemMeta();
            red_concreteItemMeta.displayName(Component.text(ChatColor.YELLOW + "CLICK TO READY"));
            red_concrete.setItemMeta(red_concreteItemMeta);
            this.inventory.setItem(48, red_concrete);
            this.inventory.setItem(50, red_concrete);
        }

        ItemStack green_glass_pane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        if (event.getRawSlot() == 48 && event.getWhoClicked() == requester && !requesterReady) {
            ItemStack greenConcrete = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta itemMeta = greenConcrete.getItemMeta();
            itemMeta.displayName(Component.text(ChatColor.GREEN + requester.getName() + " IS READY"));
            greenConcrete.setItemMeta(itemMeta);
            this.inventory.setItem(48, greenConcrete);

            for (int i = 0; i < 3; i++) {
                ItemMeta glass_paneItemMeta = green_glass_pane.getItemMeta();
                glass_paneItemMeta.displayName(Component.text(ChatColor.GREEN + requester.getName() + " IS READY"));
                green_glass_pane.setItemMeta(glass_paneItemMeta);
                this.inventory.setItem(45 + i, green_glass_pane);
            }

            responser.playSound(responser, Sound.BLOCK_LEVER_CLICK, 1, 1);
            requester.playSound(requester, Sound.BLOCK_LEVER_CLICK, 1, 1);
            requesterReady = true;
        }
        if (event.getRawSlot() == 50 && event.getWhoClicked() == responser && !responserReady) {
            ItemStack greenConcrete = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta itemMeta = greenConcrete.getItemMeta();
            itemMeta.displayName(Component.text(ChatColor.GREEN + responser.getName() + " IS READY"));
            greenConcrete.setItemMeta(itemMeta);
            this.inventory.setItem(50, greenConcrete);

            for (int i = 0; i < 3; i++) {
                ItemMeta glass_paneItemMeta = green_glass_pane.getItemMeta();
                glass_paneItemMeta.displayName(Component.text(ChatColor.GREEN + responser.getName() + " IS READY"));
                green_glass_pane.setItemMeta(glass_paneItemMeta);
                this.inventory.setItem(51 + i, green_glass_pane);
            }

            responser.playSound(responser, Sound.BLOCK_LEVER_CLICK, 1, 1);
            requester.playSound(requester, Sound.BLOCK_LEVER_CLICK, 1, 1);
            responserReady = true;
        }

        if (requesterReady && responserReady) {
            requester.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
            requester.playSound(requester, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
            responser.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
            responser.playSound(responser, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0);
        }

        if (event.getRawSlot() == 49) {
            requester.closeInventory();
            requester.playSound(requester, Sound.ENTITY_VILLAGER_NO, 1, 0);
            responser.closeInventory();
            responser.playSound(responser, Sound.ENTITY_VILLAGER_NO, 1, 0);
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {

        if (event.getReason() == InventoryCloseEvent.Reason.CANT_USE) {
            if (event.getPlayer() == requester) responserSlots.stream().filter(integer -> event.getInventory().getItem(integer) != null).forEach(integer -> requester.getInventory().addItem(event.getInventory().getItem(integer)));
            if (event.getPlayer() == responser) requesterSlots.stream().filter(integer -> event.getInventory().getItem(integer) != null).forEach(integer -> responser.getInventory().addItem(event.getInventory().getItem(integer)));
        }
        else if (event.getReason() == InventoryCloseEvent.Reason.DISCONNECT || event.getReason() == InventoryCloseEvent.Reason.DEATH) {
            if (event.getPlayer() == requester) {
                requesterSlots.stream().filter(integer -> event.getInventory().getItem(integer) != null).forEach(integer -> requester.getInventory().addItem(event.getInventory().getItem(integer)));
                responser.closeInventory();
                return;
            }
            if (event.getPlayer() == responser) {
                responserSlots.stream().filter(integer -> event.getInventory().getItem(integer) != null).forEach(integer -> responser.getInventory().addItem(event.getInventory().getItem(integer)));
                requester.closeInventory();
                return;
            }
        }

        else {
            Bukkit.getServer().getScheduler().runTaskLater(FruitsMain.getInstance(), () -> {
                if (event.getPlayer() == requester) {
                    requesterSlots.stream().filter(integer -> event.getInventory().getItem(integer) != null).forEach(integer -> requester.getInventory().addItem(event.getInventory().getItem(integer)));
                    responser.closeInventory();
                }
                if (event.getPlayer() == responser) {
                    responserSlots.stream().filter(integer -> event.getInventory().getItem(integer) != null).forEach(integer -> responser.getInventory().addItem(event.getInventory().getItem(integer)));
                    requester.closeInventory();
                }
            }, 1);
        }
    }


    public static void display(Player requester, Player responser) {
        TradeMenu tradeMenu = new TradeMenu(requester, responser);
        Bukkit.getServer().getScheduler().runTaskLater(FruitsMain.getInstance(), tradeMenu::open, 1);
    }
}
