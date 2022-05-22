package greenlink.commands;

import greenlink.FruitsMain;
import greenlink.files.DataManager;
import greenlink.utils.AbstractCommand;
import greenlink.fruits.FruitEnum;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static greenlink.fruits.FruitEnum.getRandomNotActiveFruit;

public class SetFruitSpawnCommand extends AbstractCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "У вас нету прав для этого");
            return true;
        }

        if (!(sender instanceof Player)) return true;

        if (args.length == 5) {
            try {
                Location location = new Location(((Player) sender).getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                DataManager data = FruitsMain.getInstance().data;
                data.getConfig().set("fruitsRespawn." + args[0] + ".timeMin", args[1]);
                data.getConfig().set("fruitsRespawn." + args[0] + ".location", location);
                data.saveConfig();

                long minutes = Long.parseLong(args[1]) * 1200L;


                FruitsMain.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(FruitsMain.getInstance(), new Runnable() {
                    @Override
                    public void run() {

                        try {
                            FruitEnum fruit = getRandomNotActiveFruit();
                            if (!fruit.isActive()) {
                                Item item = location.getWorld().dropItemNaturally(location, fruit.getFruitStack());
                                item.setTicksLived(4800);
                            }
                        }
                        catch (Exception e) {

                        }

                    }
                }, minutes, minutes);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Invalid input data");
            }
        }
        if (args.length == 1) {
            Location location = ((Player) sender).getLocation();
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        ArrayList<String> subCommands = new ArrayList<>();

        if (!(sender instanceof Player)) return null;
        Player player = ((Player) sender).getPlayer();
        Block block = player.getTargetBlock(4);
        Location location;
        if (block == null) location = player.getLocation();
        else location = block.getLocation();

        if (command.getName().equalsIgnoreCase("setfruitspawn")) {
            if(args.length == 1) {
                subCommands.add("<name>");
                return subCommands;
            }
            if(args.length == 2) {
                subCommands.add("<respawnTime>");
                return subCommands;
            }
            if(args.length == 3) {
                subCommands.add(String.valueOf(((int) location.getX())));
                return subCommands;
            }
            if(args.length == 4) {
                subCommands.add(String.valueOf(((int) location.getY())));
                return subCommands;
            }
            if(args.length == 5) {
                subCommands.add(String.valueOf(((int) location.getZ())));
                return subCommands;
            }
        }
        return Collections.emptyList();
    }
}
