package greenlink.enchantments.Enchantments;

import greenlink.enchantments.Enchant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Sharpening extends Enchant {
    public Sharpening(String namespace, String name, int maxLvl) {
        super(namespace, name, maxLvl);
    }

    @Override
    public void onHit(Entity playerWhoDamaged, Player damager, EntityDamageByEntityEvent event) {
        event.setDamage(event.getDamage() + damager.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this));
    }
}
