package net.whg.whsculpt.schematic;

import org.bukkit.Location;

import net.whg.utils.math.Vec3;
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
        var loc = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var offset = loc.subtract(schematic.getOrigin());
        var mask = new Vec3(1, 0, 1);

        var min = schematic.getMinimumPoint().add(offset).rBitShift(4).multiply(mask);
        var max = schematic.getMaximumPoint().add(offset).rBitShift(4).multiply(mask);

        return new RegionIterator(min, max);
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
        var loc = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var offset = loc.subtract(schematic.getOrigin());

        var min = schematic.getMinimumPoint().add(offset);
        var max = schematic.getMaximumPoint().add(offset);

        for (var entity : world.getEntities()) {
            var eLoc = entity.getLocation();
            var entityPos = new Vec3(eLoc.getBlockX(), eLoc.getBlockY(), eLoc.getBlockZ());
            if (entityPos.isInBounds(min, max))
                iterator.addEntity(entity);
        }

        return iterator;
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
