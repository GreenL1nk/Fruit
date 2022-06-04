package greenlink.enchantments.Enchantments;

import greenlink.enchantments.Enchant;
import greenlink.enchantments.EnchantEnum;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KnockDown extends Enchant {
    public KnockDown(String namespace, String name, int maxLvl) {
        super(namespace, name, maxLvl);
    }

    @Override
    public void onHit(Entity playerWhoDamaged, Player damager, EntityDamageByEntityEvent event) {
        if (EnchantEnum.KNOCKDOWN.isLucky(5)) {
            ((LivingEntity) playerWhoDamaged).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 200, false, false, false));
            ((LivingEntity) playerWhoDamaged).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, -5, false, false, false));
        }
    }
}
