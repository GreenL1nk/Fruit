package greenlink.listeners;

import greenlink.utils.AbstractInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getClickedInventory() != null) {
            if (event.getInventory().getHolder() instanceof AbstractInventoryHolder && event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) event.setCancelled(true);
            if (event.getClickedInventory().getHolder() instanceof AbstractInventoryHolder) {
                ((AbstractInventoryHolder) event.getClickedInventory().getHolder()).click(event);
            }
            if (event.getInventory().getHolder() instanceof AbstractInventoryHolder && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event){
        if ( event.getInventory().getHolder() instanceof AbstractInventoryHolder) event.setCancelled(true);
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if (event.getInventory().getHolder() instanceof AbstractInventoryHolder) {
            ((AbstractInventoryHolder) event.getInventory().getHolder()).close(event);
        }
    }
}
