package greenlink;

import greenlink.commands.*;
import greenlink.db.DatabaseConnector;
import greenlink.enchantments.EnchantEnum;
import greenlink.files.DataManager;
import greenlink.fruits.FruitEnum;
import greenlink.guis.TradeMenu;
import greenlink.items.ItemEnum;
import greenlink.listeners.*;
import greenlink.utils.AbstractInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static greenlink.fruits.FruitEnum.getRandomNotActiveFruit;

public final class FruitsMain extends JavaPlugin {

    private static FruitsMain instance;
    public DataManager data;
    public HashMap<Player, ArrayList<Player>> playerTradeRequests = new HashMap<>();
    public static HashMap<FruitEnum, Integer> fruitDropChances = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.data = new DataManager();
        data.saveDefaultConfig();

        new FruitCommand().register(this, "fruit");
        new SetFruitSpawnCommand().register(this, "setfruitspawn");
        new TradeCommand().register(this, "trade");
        new AddPlayerXpCommand().register(this, "addxp");
        new InfoCommand().register(this, "info");
        new DarkRootCommand().register(this, "darkroot");
        new CheckerCommand().register(this, "checker");

        Bukkit.getPluginManager().registerEvents(new ActionListener(), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new FlightToggleListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);

        for (EnchantEnum value : EnchantEnum.values()) {
            EnchantEnum.registerEnchantment(value.getEnchant());
        }

        ItemEnum.init();
        FruitEnum.init();

        for (FruitEnum value : FruitEnum.values()) {
            if (!data.getConfig().getConfigurationSection("fruits_chances").contains(value.name())) {
                data.getConfig().set("fruits_chances." + value, 10);
            }
        }
        data.saveConfig();
        for (Map.Entry<String, Object> fruits_chances : data.getConfig().getConfigurationSection("fruits_chances").getValues(false).entrySet()) {
            for (FruitEnum value : FruitEnum.values()) {
                if (fruits_chances.getKey().equals(value.name())) {
                    fruitDropChances.put(value, Integer.parseInt(String.valueOf(fruits_chances.getValue())));
                }
            }
        }

        if (data.getConfig().contains("fruitsRespawn")) {
            Set<String> keys = data.getConfig().getConfigurationSection("fruitsRespawn").getKeys(false);
            for (String key : keys) {

                Location location = data.getConfig().getLocation("fruitsRespawn." + key + ".location");
                long minutes = Long.parseLong(String.valueOf(data.getConfig().get("fruitsRespawn." + key + ".timeMin"))) * 1200L;

                this.getServer().getScheduler().scheduleSyncRepeatingTask(FruitsMain.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FruitEnum fruit = getRandomNotActiveFruit();

                            Item item = location.getWorld().dropItemNaturally(location, fruit.getFruitStack());
                            item.setTicksLived(4800);

                        }
                        catch (Exception e) {

                        }
                    }
                }, minutes, minutes);
            }
        }
    }

    @Override
    public void onDisable() {
        long timeLeave = System.currentTimeMillis();

        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            PlayerManager.getInstance().savePlayer(onlinePlayer.getUniqueId(), timeLeave);

            if (onlinePlayer.getOpenInventory().getTopInventory().getHolder() instanceof AbstractInventoryHolder) {
                AbstractInventoryHolder holder = (AbstractInventoryHolder) onlinePlayer.getOpenInventory().getTopInventory().getHolder();
                if (holder.getRequester() == onlinePlayer) TradeMenu.requesterSlots.stream().filter(integer -> holder.getInventory().getItem(integer) != null).forEach(integer -> holder.getRequester().getInventory().addItem(holder.getInventory().getItem(integer)));
                if (holder.getResponser() == onlinePlayer) TradeMenu.responserSlots.stream().filter(integer -> holder.getInventory().getItem(integer) != null).forEach(integer -> holder.getResponser().getInventory().addItem(holder.getInventory().getItem(integer)));
            }
        }
        DatabaseConnector.getInstance().closeConnection();
    }

    public static FruitsMain getInstance(){
        return instance;
    }
}
