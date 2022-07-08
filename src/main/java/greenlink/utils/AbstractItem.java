package greenlink.utils;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem extends ItemStack {
    public AbstractItem(ItemStack itemStack) {
        super(itemStack);
    }
    public abstract void onUse(PlayerInteractEvent event);
}
