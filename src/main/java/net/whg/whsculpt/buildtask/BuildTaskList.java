package net.whg.whsculpt.buildtask;

public class BuildTaskList extends BuildTask<BuildTask<?>> {

    protected BuildTaskList(TaskList taskList) {
        super(taskList, 1, 1);
    }

    @Override
    protected boolean update(BuildTask<?> t) {
        if (!t.isRunning())
            t.start();

        return false;
    }
}
