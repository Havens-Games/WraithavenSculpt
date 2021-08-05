package net.whg.whsculpt.buildtask;

import java.util.ArrayList;
import java.util.List;

/**
 * An iterator for a list of BuildTasks to preform.
 */
public class TaskList implements Iterator<BuildTask<?>> {
    private final List<BuildTask<?>> tasks = new ArrayList<>();
    private int index;

    public void addTask(BuildTask<?> task) {
        tasks.add(task);
    }

    @Override
    public boolean update() {
        index++;
        return index >= tasks.size();
    }

    @Override
    public BuildTask<?> get() {
        return tasks.get(index);
    }

    @Override
    public boolean shouldUpdate() {
        return tasks.get(index).isDone();
    }
}
