package greenlink.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class GetBlocks {
    public static ArrayList<Block> getBlocksInRadius(Block start, int radius){
        ArrayList<Block> blocks = new ArrayList<>();
        for(double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++){
            for(double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++){
                Location loc = new Location(start.getWorld(), x, start.getLocation().getY(), z);
                blocks.add(loc.getBlock());
            }
        }
        return blocks;
    }
}
