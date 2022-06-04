package greenlink.enchantments.Enchantments;

import greenlink.enchantments.Enchant;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Vampirism extends Enchant {

    public Vampirism(String namespace, String name, int maxLvl) {
        super(namespace, name, maxLvl);
    }

    @Override
    public void onHit(Entity playerWhoDamaged, Player damager, EntityDamageByEntityEvent event) {
        if (damager != null) {
            double value = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            int enchantLevel = damager.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this);
            if (damager.getHealth() + enchantLevel >= value) return;
            if (damager.getHealth() + 0.5 >= value) damager.setHealth(value);
            else damager.setHealth(damager.getHealth() + enchantLevel);
        }
    }
}
