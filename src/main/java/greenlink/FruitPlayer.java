package greenlink;

import greenlink.fruits.FruitEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FruitPlayer {

    private final UUID uuid;
    private FruitEnum activeFruit;
    private int level;
    private int xp;
    private Long leaveTime;

    public FruitPlayer(UUID uuid) {
        this.uuid = uuid;
        this.xp = 0;
        this.level = 1;
        this.activeFruit = null;
        this.leaveTime = null;
    }

    public FruitPlayer(UUID uuid, int level, int xp, FruitEnum activeFruit, Long leaveTime) {
        this.uuid = uuid;
        this.level = level;
        this.xp = xp;
        this.activeFruit = activeFruit;
        this.leaveTime = leaveTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public FruitEnum getActiveFruit() {
        return activeFruit;
    }

    public void setActiveFruit(FruitEnum activeFruit) {
        if (this.getActiveFruit() != null) this.getActiveFruit().getFruitPowers().resetCoolDowns(this.getPlayer());
        this.activeFruit = activeFruit;
    }

    public void removeActiveFruit() {
        this.getActiveFruit().getFruitPowers().resetCoolDowns(this.getPlayer());
        this.activeFruit = null;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXP(int countXP) {
        this.xp += countXP;

        if(this.xp >= this.lvlFormula()) {
            xp -= lvlFormula();
            level++;
        }
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int lvlFormula() {
        double maxXP = 100;

        for(int i = 0; i < level; i++) {
            maxXP = (maxXP * 1.05);
        }
        return (int) maxXP;
    }

    public void addLevel(int count) {
        level += count;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
