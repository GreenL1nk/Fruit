package greenlink.commands;

import greenlink.FruitsMain;
import greenlink.enchantments.EnchantEnum;
import greenlink.files.DataManager;
import greenlink.fruits.FruitEnum;
import greenlink.items.ItemEnum;
import greenlink.utils.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FruitCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "У вас нету прав для этого");
            return true;
        }

        if (args.length == 0) {
            Player player = (Player) sender;
            for (EnchantEnum value : EnchantEnum.values()) {
                ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                itemStack.addUnsafeEnchantment(value.getEnchant(), 1);
                itemStack.lore(Collections.singletonList(value.getEnchant().displayName(1)));
                player.getInventory().addItem(itemStack);
            }
            for (FruitEnum value : FruitEnum.values()) {
                player.getInventory().addItem(value.getFruitStack());
            }
            for (ItemEnum value : ItemEnum.values()) {
                player.getInventory().addItem(value.getItem());
            }
            return true;
        }


        if (args[0].equalsIgnoreCase("reload")) {
            DataManager dataManager = FruitsMain.getInstance().data;
            dataManager.reloadConfig();
            return true;
        }

        if (args[0].equalsIgnoreCase("get")) {
            if (sender instanceof Player) {
                for (FruitEnum fruit : FruitEnum.values()) {
                    ((Player) sender).getInventory().addItem(fruit.getFruitStack());
                }
            }

            return true;
        }



        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ArrayList<String> res = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("fruit")) {
            if (!(sender instanceof Player)) return res;

            if(args.length == 1) {
                res.add("reload");
                res.add("get");
                return res;
            }

        }
        return null;
    }
}
