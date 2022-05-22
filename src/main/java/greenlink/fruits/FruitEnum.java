package greenlink.fruits;

import greenlink.FruitPlayer;
import greenlink.fruits.powers.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public enum FruitEnum {

    SUKE("Suke Suke", false, null, new Suke()),
    TON("Ton Ton", false, null, new Ton()),
    KUKU("Kuku Kuku", false, null, new Kuku()),
    CHIYU("Chiyu Chiyu", false, null, new Chiyu()),
    FUWA("Fuwa Fuwa", false, null, new Fuwa()),
    DOKU("Doku Doku", false, null, new Doku()),
    NETSU("Netsu Netsu", false, null, new Netsu()),
    KOBU("Kobu Kobu", false, null, new Kobu()),
    GOMU("Gomu Gomu", false, null, new Gomu()),
    NORO("Noro Noro", false, null, new Noro()),
    TOGE("Toge Toge", false, null, new Toge()),
    MERA("Mera Mera", false, null, new Mera()),
    GORO("Goro Goro", false, null, new Goro()),
    MAGU("Magu Magu", false, null, new Magu()),
    PIKA("Pika Pika", false, null, new Pika());

    private final String name;
    private ItemStack itemStack;
    private boolean isActive;
    private FruitPlayer holder;
    private final Fruit fruitPowers;

    private static final SecureRandom random = new SecureRandom();

    public static final HashMap<FruitEnum, UUID> fruitHolders = new HashMap<>();

    FruitEnum(String name, boolean isActive, FruitPlayer holder, Fruit fruitPowers) {
        this.name = name;
        this.isActive = isActive;
        this.holder = holder;
        this.fruitPowers = fruitPowers;
    };


    public ItemStack getFruitStack() {
        return itemStack;
    }

    public ItemStack createFruit() {
        ItemStack item = new ItemStack(Material.CHORUS_FRUIT, 1);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(name));

        meta.addEnchant(Enchantment.CHANNELING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);

        this.itemStack = item;

        return item;
    }

    public static void init() {
        for (FruitEnum fruit : FruitEnum.values()) {
            fruit.createFruit();
        }
    }

    public static FruitEnum getRandomNotActiveFruit() {
        List<FruitEnum> fruitList = new ArrayList<>();
        for (FruitEnum fruit : FruitEnum.values()) {
            if (!fruit.isActive()) {
                fruitList.add(fruit);
            }
        }
        if (fruitList.size() > 0) {
            int x = random.nextInt(fruitList.size());
            return fruitList.get(x);
        }
        return null;
    }

    public static HashMap<FruitEnum, UUID> setFruitMap() {

        for (FruitEnum fruit : FruitEnum.values()) {
            if (!fruitHolders.containsKey(fruit)) {
                fruitHolders.put(fruit, null);
            }
        }

        return fruitHolders;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public FruitPlayer getHolder() {
        return holder;
    }

    public void setHolder(FruitPlayer holder) {
        this.holder = holder;
    }

    public Fruit getFruitPowers() {
        return fruitPowers;
    }

    public static boolean playerHaveFruits(FruitPlayer fruitPlayer) {
        for (FruitEnum value : FruitEnum.values()) {
            if (value.getHolder() != null) {
                if (value.getHolder() == fruitPlayer) {
                    return true;
                }
            }
        }
        return false;
    }
}
