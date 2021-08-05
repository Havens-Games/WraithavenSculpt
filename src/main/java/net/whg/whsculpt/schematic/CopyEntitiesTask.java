package net.whg.whsculpt.schematic;

import org.bukkit.Location;

import net.whg.whsculpt.buildtask.InstantBuildTask;

/**
 * A simple instant build task that spawns all entities within a schematic at
 * the given relative location.
 */
class CopyEntitiesTask extends InstantBuildTask {
    private final Schematic schematic;
    private final Location location;

    /**
     * Creates a new CopyEntitiesTask.
     * 
     * @param schematic - The schematic to copy entities from.
     * @param location  - The location to spawn the entities at.
     */
    CopyEntitiesTask(Schematic schematic, Location location) {
        this.schematic = schematic;
        this.location = location;
    }

    @Override
    protected void update() {
        schematic.spawnEntities(location);
    }
}
