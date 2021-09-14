package net.whg.whsculpt.schematic;

import org.bukkit.Location;
import org.joml.Vector3i;

import net.whg.whsculpt.buildtask.BuildTaskList;
import net.whg.whsculpt.buildtask.EntityIterator;
import net.whg.whsculpt.buildtask.LoadChunksTask;
import net.whg.whsculpt.buildtask.RegionIterator;
import net.whg.whsculpt.buildtask.TaskList;
import net.whg.whsculpt.buildtask.UnloadChunksTask;

/**
 * A build task that pastes a schematic at the given location. This build task
 * will load the required chunks, copy over blocks, replace all entities, and
 * unload the chunks as needed.
 */
public class SchematicBuildTask extends BuildTaskList {
    /**
     * Generates a task list for all of the required steps needed to full build a
     * schematic at the given location.
     * 
     * @param schematic - The schematic to build.
     * @param location  - The location to build the schematic at.
     * @return The build task list.
     */

    private static TaskList schematicToTaskList(Schematic schematic, Location location) {
        var taskList = new TaskList();
        var world = location.getWorld();

        taskList.addTask(new LoadChunksTask(getChunkIterator(schematic, location), world));
        taskList.addTask(new ClearEntitiesTask(getEntityIterator(schematic, location)));
        taskList.addTask(new CopyBlocksTask(schematic, location));
        taskList.addTask(new CopyEntitiesTask(schematic, location));
        taskList.addTask(new TriggerFinishedEventTask(schematic, location));
        taskList.addTask(new UnloadChunksTask(getChunkIterator(schematic, location), world));

        return taskList;
    }

    /**
     * Creates a region iterator in chunk coordinates to iterate over all the chunks
     * that would be affected in world space while building the schematic at the
     * target location.
     * 
     * @param schematic - The schematic that is being built.
     * @param location  - The location that schematic is being built at.
     * @return The region iterator in chunk coordinates.
     */
    private static RegionIterator getChunkIterator(Schematic schematic, Location location) {
        var offset = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        offset.sub(schematic.getOrigin());

        var min = schematic.getMinimumPoint().add(offset, new Vector3i());
        var max = schematic.getMaximumPoint().add(offset, new Vector3i());

        blockCoordsToChunkCoords(min);
        blockCoordsToChunkCoords(max);

        var mask = new Vector3i(1, 0, 1);
        min.mul(mask);
        max.mul(mask);

        return new RegionIterator(min, max);
    }

    /**
     * Converts a set of block coordinates to chunk coordinates.
     * 
     * @param pos - The world position.
     */
    private static void blockCoordsToChunkCoords(Vector3i pos) {
        pos.x >>= 4;
        pos.y >>= 4;
        pos.z >>= 4;
    }

    /**
     * Creates an entity iterator over all entities that lie within the position
     * that would be intercepted by building the schematic at the given location.
     * 
     * @param schematic - The schematic that is being built.
     * @param location  - The location that schematic is being built at.
     * @return The entity iterator for existing entities.
     */
    private static EntityIterator getEntityIterator(Schematic schematic, Location location) {
        var iterator = new EntityIterator();

        var world = location.getWorld();
        var offset = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        offset.sub(schematic.getOrigin());

        var min = schematic.getMinimumPoint().add(offset, new Vector3i());
        var max = schematic.getMaximumPoint().add(offset, new Vector3i());

        for (var entity : world.getEntities()) {
            var eLoc = entity.getLocation();
            if (isInBounds(eLoc, min, max))
                iterator.addEntity(entity);
        }

        return iterator;
    }

    /**
     * Checks if the given location is within the axis-aligned world bounds
     * specified.
     * 
     * @param location - The location
     * @param min      - The minimum world pos.
     * @param max      - The maximum world pos.
     * @return True if the location is in the given bounds. False otherwise.
     */
    private static boolean isInBounds(Location location, Vector3i min, Vector3i max) {
        return location.getX() >= min.x && location.getX() < max.x && location.getY() >= min.y
                && location.getY() < max.y && location.getZ() >= min.z && location.getZ() < max.z;
    }

    /**
     * Creates a new SchematicBuildTask.
     * 
     * @param schematic - The schematic to build.
     * @param location  - The location to build the schematic at.
     */
    public SchematicBuildTask(Schematic schematic, Location location) {
        super(schematicToTaskList(schematic, location));
    }
}
