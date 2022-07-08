package greenlink.commands;

import greenlink.FruitsMain;
import greenlink.guis.TradeMenu;
import greenlink.utils.AbstractCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradeCommand extends AbstractCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player requester = (Player) sender;
            HashMap<Player, ArrayList<Player>> playerTradeRequests = FruitsMain.getInstance().playerTradeRequests;
            if (requester.toString().equals(args[1])) return true;

            if (args.length == 2) {
                if (args[0].equals("accept")) {
                    Player tradeWith = Bukkit.getPlayer(args[1]);
                    if (playerTradeRequests.containsKey(requester) && playerTradeRequests.get(requester).contains(tradeWith)) {
                        if (tradeWith == null || requester.getLocation().distance(tradeWith.getLocation()) > 30) {requester.sendMessage(ChatColor.LIGHT_PURPLE + "" + tradeWith + ChatColor.RED + "не находится рядом.");playerTradeRequests.remove(tradeWith);return true;}
                        TradeMenu.display(requester, tradeWith);
                        playerTradeRequests.remove(tradeWith);
                    }
                    else {
                        requester.sendMessage(ChatColor.GRAY + "У вас нет активного запроса на торговлю от " + ChatColor.LIGHT_PURPLE + args[1]);
                    }
                }
                if (args[0].equals("request")) {
                    Player tradeWith = Bukkit.getPlayer(args[1]);
                    if(tradeWith == null || requester.getLocation().distance(tradeWith.getLocation()) > 30) {requester.sendMessage(ChatColor.LIGHT_PURPLE + "" + args[1] + ChatColor.RED + "не находится рядом.");return true;}
                    if (playerTradeRequests.containsKey(tradeWith) && playerTradeRequests.get(tradeWith).contains(requester)) return true;

                    requester.sendMessage(ChatColor.GRAY + "Ваш торговый запрос, отправлен " + ChatColor.LIGHT_PURPLE + args[1]);
                    playerTradeRequests.put(tradeWith, new ArrayList<>());
                    playerTradeRequests.get(tradeWith).add(requester);

                    final TextComponent textComponent = Component.text("")
                            .append(
                                    Component.text(requester.getName())
                                            .color(NamedTextColor.LIGHT_PURPLE))
                            .append(Component.text(" отправил вам запрос на торговлю", NamedTextColor.GRAY))
                            .hoverEvent(Component.text("Нажмите, чтобы принять").color(NamedTextColor.GRAY))
                            .clickEvent(ClickEvent.runCommand("/trade accept " + requester.getName()));

                    tradeWith.sendMessage(textComponent);

                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FruitsMain.getInstance(), () -> playerTradeRequests.remove(tradeWith), 1200L);
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> alias = new ArrayList<>();
        alias.add("request");
        alias.add("accept");

        ArrayList<String> refundable = new ArrayList<>();

        String lastWord = args[args.length - 1];
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        for (String s : alias) {
            if (senderPlayer != null && StringUtil.startsWithIgnoreCase(s, lastWord)) {
                refundable.add(s);
            }
        }

        return refundable;
    }
}
