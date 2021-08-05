package net.whg.whsculpt.buildtask;

/**
 * Called once (or more) each frame within a build task to iterate over a region
 * or a collection. <br/>
 * <br/>
 * Update loop is get() -> update() -> get() -> update()
 */
public interface Iterator<T> {
    /**
     * Move to the next spot in the iterator.
     * 
     * @return True if the iterator has reached the end. False otherwise.
     */
    boolean update();

    /**
     * Gets the current object.
     * 
     * @return The object.
     */
    T get();

    /**
     * Checks whether or not this iterator is ready to move on to the next option.
     * 
     * @return True if the current object is finished being handled. False
     *         otherwise.
     */
    default boolean shouldUpdate() {
        return true;
    }
}
