package greenlink.commands;

import greenlink.FruitPlayer;
import greenlink.PlayerManager;
import greenlink.utils.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfoCommand extends AbstractCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;

        if (args.length == 0) {

            FruitPlayer player = PlayerManager.getInstance().getPlayer(((Player) sender).getUniqueId());
            sender.sendMessage(ChatColor.GRAY + "Уровень: " + ChatColor.LIGHT_PURPLE + player.getLevel());
            sender.sendMessage(ChatColor.GRAY + "Опыта: " + ChatColor.LIGHT_PURPLE + player.getXp());
            int needXP = player.lvlFormula() - player.getXp();
            sender.sendMessage(ChatColor.GRAY + "Опыта до след. уровня " + ChatColor.LIGHT_PURPLE + needXP + ChatColor.GRAY + " опыт (" + ChatColor.LIGHT_PURPLE  + player.getXp() + ChatColor.GRAY + "/" + ChatColor.LIGHT_PURPLE + player.lvlFormula() + ChatColor.GRAY + ")");

            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
