package greenlink.items;

import greenlink.utils.AbstractItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ItemEnum {

    DARK_ROOT(new DarkRoot(new ItemStack(Material.CHAIN)), "DarkRoot");

    private final AbstractItem item;
    private final String name;

    ItemEnum(AbstractItem item, String name) {
        this.item = item;
        this.name = name;
    }

    public AbstractItem getItem() {
        return this.item;
    }

    public void create() {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.addEnchant(Enchantment.CHANNELING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    public static void init() {
        for (ItemEnum itemEnum : ItemEnum.values()) {
            itemEnum.create();
        }
    }
}
