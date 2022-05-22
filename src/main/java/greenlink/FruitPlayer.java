package greenlink;

import greenlink.fruits.FruitEnum;

import java.util.UUID;

public class FruitPlayer {

    private final UUID uuid;
    private FruitEnum ActiveFruit;
    private int level;
    private int xp;
    private Long leaveTime;

    public FruitPlayer(UUID uuid) {
        this.uuid = uuid;
        this.xp = 0;
        this.level = 0;
        this.ActiveFruit = null;
        this.leaveTime = null;
    }

    public FruitPlayer(UUID uuid, int level, int xp, FruitEnum ActiveFruit, Long leaveTime) {
        this.uuid = uuid;
        this.level = level;
        this.xp = xp;
        this.ActiveFruit = ActiveFruit;
        this.leaveTime = leaveTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public FruitEnum getActiveFruit() {
        return ActiveFruit;
    }

    public void setActiveFruit(FruitEnum activeFruit) {
        this.ActiveFruit = activeFruit;
        this.ActiveFruit.setHolder(this);
    }

    public void removeActiveFruit() {
        this.ActiveFruit.setHolder(null);
        this.ActiveFruit = null;
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
        int maxXP = 100;

        for(int i = 0; i < level; i++) {
            maxXP = (int) (maxXP * 1.05);
        }
        return maxXP;
    }

    public void addLevel(int count) {
        level += count;
    }
}
