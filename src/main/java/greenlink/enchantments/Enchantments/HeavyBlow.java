package greenlink.enchantments.Enchantments;

import greenlink.enchantments.Enchant;
import greenlink.enchantments.EnchantEnum;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HeavyBlow extends Enchant {
    public HeavyBlow(String namespace, String name, int maxLvl) {
        super(namespace, name, maxLvl);
    }

    @Override
    public void onHit(Entity playerWhoDamaged, Player damager, EntityDamageByEntityEvent event) {
        int enchantLevel = damager.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this);
        if (enchantLevel == 1 && EnchantEnum.HEAVYBLOW.isLucky(10)) {
            ((LivingEntity) playerWhoDamaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
        }
        if (enchantLevel == 2 && EnchantEnum.HEAVYBLOW.isLucky(15)) {
            ((LivingEntity) playerWhoDamaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
        }
    }
}
