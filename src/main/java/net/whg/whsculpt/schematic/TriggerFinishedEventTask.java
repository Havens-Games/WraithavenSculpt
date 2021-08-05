package net.whg.whsculpt.schematic;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.whg.whsculpt.buildtask.InstantBuildTask;
import net.whg.whsculpt.events.FinishedBuildingSchematicEvent;

/**
 * A simply instant build task that triggers a FinishedBuildingSchematicEvent.
 */
class TriggerFinishedEventTask extends InstantBuildTask {
    private final Schematic schematic;
    private final Location location;

    /**
     * Creates a new TriggeredFinishedEventTask.
     * 
     * @param schematic - The schematic that was built.
     * @param location  - The location that the schematic was built at.
     */
    TriggerFinishedEventTask(Schematic schematic, Location location) {
        this.schematic = schematic;
        this.location = location;
    }

    @Override
    protected void update() {
        var event = new FinishedBuildingSchematicEvent(schematic, location);
        Bukkit.getPluginManager().callEvent(event);
    }
}
