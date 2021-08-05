package net.whg.whsculpt.events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.whsculpt.schematic.Schematic;

/**
 * Called when a build task for building a schematic object has been completed.
 * This event is called before the chunks are unloaded in case any additional
 * processing needs to be preformed within the region during this event.
 */
public class FinishedBuildingSchematicEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Schematic schematic;
    private final Location location;

    /**
     * Creates a new FinishedBuildingSchematicEvent.
     * 
     * @param schematic - The schematic that was built.
     * @param location  - The location that the schematic was built at.
     */
    public FinishedBuildingSchematicEvent(Schematic schematic, Location location) {
        this.schematic = schematic;
        this.location = location;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * Gets the schematic that was built.
     * 
     * @return The schematic.
     */
    public Schematic getSchematic() {
        return schematic;
    }

    /**
     * Gets the location the schematic was built at.
     * 
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }
}
