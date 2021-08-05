package net.whg.whsculpt.buildtask;

import net.whg.utils.math.Vec3;

public abstract class BlockBuildTask extends BuildTask<Vec3> {
    protected BlockBuildTask(Iterator<Vec3> iterator) {
        super(iterator, 100, 5000);
    }
}
