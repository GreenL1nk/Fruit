package greenlink.utils;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommand implements TabExecutor {
    public void register(JavaPlugin plugin, String name){
        PluginCommand command = plugin.getCommand(name);
        if (command != null) {
            command.setTabCompleter(this);
            command.setExecutor(this);
        }
    }
}
