package greenlink.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInventoryHolder implements InventoryHolder {
    protected Inventory inventory;
    protected Player requester;
    protected Player responser;

    public AbstractInventoryHolder(Component title, int lines, Player requester, Player responser) {
        this.inventory = Bukkit.createInventory(this, lines * 9, title);
        this.requester = requester;
        if (responser == null) {
            return;
        }
        this.responser = responser;
    }

    protected abstract void init();

    public abstract void click(InventoryClickEvent event);

    public abstract void close(InventoryCloseEvent event);

    @Override
    public final @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public final void open(){
        requester.openInventory(this.inventory);
        responser.openInventory(this.inventory);
    }

    public Player getRequester() {
        return requester;
    }

    public Player getResponser() {
        return responser;
    }
}
