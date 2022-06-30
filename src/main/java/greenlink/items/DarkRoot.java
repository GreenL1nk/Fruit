package greenlink.items;

import greenlink.FruitPlayer;
import greenlink.PlayerManager;
import greenlink.utils.AbstractItem;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DarkRoot extends AbstractItem {
    public DarkRoot(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem() != null) {
            FruitPlayer player = PlayerManager.getInstance().getPlayer(event.getPlayer().getUniqueId());
            if (player.getActiveFruit() != null) {
                event.getItem().subtract();
                player.removeActiveFruit();
            }
        }
    }
}
