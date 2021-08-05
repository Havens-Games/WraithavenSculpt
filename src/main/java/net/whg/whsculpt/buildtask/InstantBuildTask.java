package net.whg.whsculpt.buildtask;

/**
 * A special kind of build task that finished in one tick. It does not use an
 * iterator an instead is primarily targeted towards usage in build task lists.
 */
public abstract class InstantBuildTask extends BuildTask<Object> {

    /**
     * Creates a new InstanceBuildTask.
     */
    protected InstantBuildTask() {
        super(null, 1, 1);
    }

    @Override
    public void run() {
        update();
        cancel();
        done = true;
    }

    @Override
    protected boolean update(Object t) {
        return false;
    }

    /**
     * Called once for when this build task is run and never again.
     */
    protected abstract void update();

}
