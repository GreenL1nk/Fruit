package greenlink.items;

import greenlink.FruitPlayer;
import greenlink.FruitsMain;
import greenlink.PlayerManager;
import greenlink.utils.AbstractItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class DarkRoot extends AbstractItem {
    ArrayList<UUID> arrayList = new ArrayList<>();
    public DarkRoot(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public void onUse(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem() != null) {
            UUID uniqueId = event.getPlayer().getUniqueId();
            FruitPlayer player = PlayerManager.getInstance().getPlayer(uniqueId);
            if (player.getActiveFruit() != null) {
                if (!arrayList.contains(uniqueId)) {
                    final TextComponent textComponent = Component.text(ChatColor.DARK_GRAY + "Фрукт будет удалён через 3 секунды, для отмены уберите ")
                            .append(event.getItem().getItemMeta().displayName())
                            .append(Component.text(ChatColor.DARK_GRAY + " из рук."));

                    event.getPlayer().sendMessage(textComponent);
                    arrayList.add(uniqueId);
                    FruitsMain.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(FruitsMain.getInstance(), () -> {
                        if (event.getItem().getItemMeta().equals(event.getPlayer().getInventory().getItemInMainHand().getItemMeta())) {
                            event.getItem().subtract();
                            player.removeActiveFruit();
                        }
                        else {
                            event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Удаление фрукта отменено.");
                        }
                        arrayList.remove(uniqueId);
                    }, 60L);
                }
            }
        }
    }
}
