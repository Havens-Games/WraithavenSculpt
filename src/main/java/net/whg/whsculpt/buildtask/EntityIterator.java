package net.whg.whsculpt.buildtask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Entity;

/**
 * Iterates over a list of entities. This function will skip entities that
 * become invalid (dead, despawned, etc) while iterating.
 */
public class EntityIterator implements Iterator<Entity> {
    private final List<Entity> entities = new ArrayList<>();
    private int index;

    /**
     * Adds an entity to the end of this list.
     * 
     * @param entity - The entity to add.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Adds a collection of entities to the end of this list.
     * 
     * @param entities - The collection of entities to add.
     */
    public void addEntities(Collection<Entity> entities) {
        this.entities.addAll(entities);
    }

    @Override
    public boolean update() {
        while (true) {
            index++;

            if (index >= entities.size())
                return true;

            var e = entities.get(index);
            if (e.isValid())
                break;
        }

        return false;
    }

    @Override
    public Entity get() {
        return entities.get(index);
    }
}
