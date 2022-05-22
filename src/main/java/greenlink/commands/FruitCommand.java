package greenlink.commands;

import greenlink.FruitsMain;
import greenlink.files.DataManager;
import greenlink.files.FruitManager;
import greenlink.fruits.FruitEnum;
import greenlink.utils.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FruitCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "У вас нету прав для этого");
            return true;
        }

        if (args.length == 0) {
//            Player player = (Player) sender;
//            PlayerManager.getInstance().getPlayer(player.getUniqueId()).addXP(10);
//            Bukkit.broadcastMessage(String.valueOf(PlayerManager.getInstance().getPlayer(player.getUniqueId()).getXp()));
            return true;
        }


        if (args[0].equalsIgnoreCase("reload")) {
            DataManager dataManager = FruitsMain.getInstance().data;
            FruitManager fruitManager = FruitsMain.getInstance().fruitManager;
            dataManager.reloadConfig();
            fruitManager.reloadConfig();

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
