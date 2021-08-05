package net.whg.whsculpt.buildtask;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A task that is run async on the main thread over the span of many ticks in
 * order to complex a complex iterable task.
 */
public abstract class BuildTask<T> extends BukkitRunnable {
    private final Iterator<T> iterator;
    private int iterations;
    private int maxSkips;
    private boolean started;
    protected boolean done;

    /**
     * Creates a new BuildTask instance.
     * 
     * @param iterator   - The iterator for this build task.
     * @param iterations - The number of non-skipped operations to preform each
     *                   tick.
     * @param maxSkips   - The maximum number of skips for a single tick.
     */
    protected BuildTask(Iterator<T> iterator, int iterations, int maxSkips) {
        this.iterator = iterator;
        this.iterations = iterations;
        this.maxSkips = maxSkips;
    }

    @Override
    public void run() {
        var updated = 0;
        var skips = 0;
        while (updated < iterations && skips < maxSkips) {
            var t = iterator.get();

            if (update(t))
                updated++;
            else
                skips++;

            if (iterator.shouldUpdate()) {
                done = iterator.update();
                if (done) {
                    cancel();
                    return;
                }
            }
        }
    }

    /**
     * Starts this BuildTask.
     * 
     * @throws IllegalStateException If this task has already been started. (Even if
     *                               already done.)
     */
    public void start() {
        if (started)
            throw new IllegalStateException("Task has already been started!");

        started = true;

        var plugin = Bukkit.getPluginManager().getPlugin("WraithavenSculpt");
        runTaskTimer(plugin, 1, 1);
    }

    /**
     * Checks if this task has been completed.
     * 
     * @return True if this task is completed. False otherwise.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Checks if this task is currently running.
     * 
     * @return True if this task is running. False otherwise.
     */
    public boolean isRunning() {
        return started && !done;
    }

    /**
     * Called for each object supplied by the iterator.
     * 
     * @param t - THe supplied object.
     * @return True if this object should be skipped. False if this object was
     *         properly handled.
     */
    protected abstract boolean update(T t);
}
