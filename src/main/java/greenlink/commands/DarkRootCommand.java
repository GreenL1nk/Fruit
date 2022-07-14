package greenlink.commands;

import greenlink.items.ItemEnum;
import greenlink.utils.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkRootCommand extends AbstractCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.isOp() || args.length != 1) return true;
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return true;

        player.getInventory().addItem(ItemEnum.DARK_ROOT.getItem());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
