package greenlink.fruits;

import greenlink.FruitsMain;
import greenlink.fruits.powers.*;
import greenlink.items.ItemEnum;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.security.SecureRandom;
import java.util.*;

public enum FruitEnum {

    CHIYU("Chiyu Chiyu", new Chiyu()),
    DOKU("Doku Doku", new Doku()),
    FUWA("Fuwa Fuwa", new Fuwa()),
    GOMU("Gomu Gomu", new Gomu()),
    GORO("Goro Goro", new Goro()),
    KOBU("Kobu Kobu", new Kobu()),
    KUKU("Kuku Kuku", new Kuku()),
    MAGU("Magu Magu", new Magu()),
    MERA("Mera Mera", new Mera()),
    NETSU("Netsu Netsu", new Netsu()),
    NORO("Noro Noro", new Noro()),
    PIKA("Pika Pika", new Pika()),
    SUKE("Suke Suke", new Suke()),
    TOGE("Toge Toge", new Toge()),
    TON("Ton Ton", new Ton());

    private final String name;

    private ItemStack itemStack;
    private final Fruit fruitPowers;
    public static final NamespacedKey namespacedKey = new NamespacedKey(FruitsMain.getInstance(), "fruit");
    private static final SecureRandom random = new SecureRandom();
    public static final HashMap<FruitEnum, UUID> fruitHolders = new HashMap<>();
    public static ShapelessRecipe shapelessRecipe;

    FruitEnum(String name, Fruit fruitPowers) {
        this.name = name;
        this.fruitPowers = fruitPowers;
    }

    public ItemStack getFruitStack() {
        return itemStack;
    }

    public void createFruit() {
        ItemStack item = new ItemStack(Material.CHORUS_FRUIT);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, name);
        meta.displayName(Component.text("unidentified fruit").decorate(TextDecoration.OBFUSCATED));
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        this.itemStack = item;
    }

    public static void init() {
        for (FruitEnum fruit : FruitEnum.values()) {
            fruit.createFruit();

            NamespacedKey identifier = new NamespacedKey(FruitsMain.getInstance(), fruit.name().toLowerCase());

            ShapelessRecipe recipe = new ShapelessRecipe(identifier, fruit.itemStack);
            recipe.addIngredient(ItemEnum.FRUIT_CHECKER.getItem());
            recipe.addIngredient(fruit.getFruitStack());
            FruitsMain.getInstance().getServer().addRecipe(recipe);
            shapelessRecipe = recipe;
        }
    }

    public static FruitEnum getRandomNotActiveFruit() {
        List<FruitEnum> fruitList = new ArrayList<>();
        Collections.addAll(fruitList, FruitEnum.values());
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

    public Fruit getFruitPowers() {
        return fruitPowers;
    }

    public String getName() {
        return name;
    }
}
