package greenlink.enchantments;

import greenlink.enchantments.Enchantments.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;


public enum EnchantEnum {

    BLINDING(new Blinding("blinding", "Blinding", 2)),
    HEAVYBLOW(new HeavyBlow("heavyblow", "HeavyBlow", 2)),
    KNOCKDOWN(new KnockDown("knockdown", "KnockDown", 1)),
    NINJA(new Ninja("ninja", "Ninja", 2)),
    POISON(new Poison("poison", "Poison", 2)),
    SHARPENING(new Sharpening("sharpening", "Sharpening", 3)),
    VAMPIRISM(new Vampirism("vampirism", "Vampirism", 2)),
    WITHER(new Wither("wither", "Wither", 2));

    private final Enchant enchant;

    EnchantEnum(Enchant enchant) {
        this.enchant = enchant;
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
        }
        if(registered){
            // It's been registered!
        }
    }

    public static int countEnchantsOnItem(ItemStack itemStack) {
        return (int) Arrays.stream(EnchantEnum.values()).filter(enchantEnum -> itemStack.getItemMeta().hasEnchant(enchantEnum.enchant)).count();
    }

    public boolean isLucky(double percent) {
        Random rand = new Random();
        return rand.nextDouble() < percent / 100;
    }

    public Enchant getEnchant() {
        return enchant;
    }
}
