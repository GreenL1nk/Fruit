package greenlink.enchantments.Enchantments;

import greenlink.enchantments.Enchant;
import greenlink.enchantments.EnchantEnum;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ninja extends Enchant {
    public Ninja(String namespace, String name, int maxLvl) {
        super(namespace, name, maxLvl);
    }

    @Override
    public void onHit(Entity playerWhoDamaged, Player damager, EntityDamageByEntityEvent event) {
        int enchantLevel = damager.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this);
        if (enchantLevel == 1 && EnchantEnum.NINJA.isLucky(5)) {
            damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0));
        }
        if (enchantLevel == 2 && EnchantEnum.NINJA.isLucky(10)) {
            damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0));
        }
    }
}
