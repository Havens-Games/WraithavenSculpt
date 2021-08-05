package net.whg.whsculpt.schematic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.whg.whsculpt.buildtask.BuildTask;
import net.whg.whsculpt.buildtask.EntityIterator;

/**
 * A build task that deleted all provided entities. This function ignores player
 * entities.
 */
class ClearEntitiesTask extends BuildTask<Entity> {
    /**
     * Creates a new ClearEntitiesTask.
     * 
     * @param iterator - The entity iterator.
     */

    ClearEntitiesTask(EntityIterator iterator) {
        super(iterator, 5000, 5000);
    }

    @Override
    protected boolean update(Entity e) {
        if (e instanceof Player)
            return true;

        e.remove();
        return false;
    }
}
