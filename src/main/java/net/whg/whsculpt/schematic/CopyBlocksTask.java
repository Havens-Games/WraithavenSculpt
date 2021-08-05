package net.whg.whsculpt.schematic;

import org.bukkit.Location;

import net.whg.utils.math.Vec3;
import net.whg.whsculpt.buildtask.BlockBuildTask;
import net.whg.whsculpt.buildtask.RegionIterator;

/**
 * A build task that copies blocks from a schematic and pastes them at the
 * target location. This task does not update block physics for compatibility
 * with hanging objects like torches on walls, so redstone will be pasted frozen
 * in place.
 */
class CopyBlocksTask extends BlockBuildTask {
    /**
     * Creates a new region iterator that iterates over all blocks within the
     * schematic in local coordinates.
     * 
     * @param schematic - The schematic.
     * @return The region iterator.
     */
    private static RegionIterator getSchematicIterator(Schematic schematic) {
        return new RegionIterator(schematic.getMinimumPoint(), schematic.getMaximumPoint());
    }

    private final Schematic schematic;
    private final Location location;
    private final Vec3 offset;

    /**
     * Creates a new CopyBlocksTask.
     * 
     * @param schematic - The schematic to copy from.
     * @param location  - The location to paste the schematic at.
     */
    CopyBlocksTask(Schematic schematic, Location location) {
        super(getSchematicIterator(schematic));
        this.schematic = schematic;
        this.location = location;

        var loc = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        offset = loc.subtract(schematic.getOrigin());
    }

    @Override
    protected boolean update(Vec3 pos) {
        var world = location.getWorld();
        var worldPos = pos.add(offset);

        var targetData = schematic.getBlockData(pos);
        var block = world.getBlockAt(worldPos.x, worldPos.y, worldPos.z);

        if (block.getBlockData().matches(targetData))
            return false;

        block.setBlockData(targetData, false);
        return true;
    }
}
