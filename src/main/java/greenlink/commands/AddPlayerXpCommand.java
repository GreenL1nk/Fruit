package greenlink.commands;

import greenlink.FruitPlayer;
import greenlink.PlayerManager;
import greenlink.utils.AbstractCommand;
import greenlink.utils.Check;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class AddPlayerXpCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            return true;
        }

        if (Check.isInteger(args[0])) {
            FruitPlayer player = PlayerManager.getInstance().getPlayer(UUID.fromString(args[1]));
            player.addXP(Integer.parseInt(args[0]));
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
