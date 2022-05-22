package greenlink.fruits;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Fruit {

    public static final Map<UUID, Long> rightCoolDown = new HashMap<>();
    public static final Map<UUID, Long> leftCoolDown = new HashMap<>();
    public static final Map<UUID, Long> spaceCoolDown = new HashMap<>();

    public abstract void onRightClick(Player player);

    public abstract void onLeftClick(Player player);

    public void passiveOnEntityDamage(Player player, EntityDamageEvent event) {}

    public void passiveOnDamageTaken(Player player, Entity damager, EntityDamageByEntityEvent event) {}

    public void passiveWalk(Player player) {}

    public void onSpacePower(Player player) {}

    public void onDeath(Player player) {}

    public void onJoin(Player player) {}

    public void onActivateFruit(Player player) {}

    public void resetCoolDowns(Player player) {
        UUID uuid = player.getUniqueId();
        leftCoolDown.remove(uuid);
        rightCoolDown.remove(uuid);
        spaceCoolDown.remove(uuid);
    }
}
