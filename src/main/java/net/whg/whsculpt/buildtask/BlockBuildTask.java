package net.whg.whsculpt.buildtask;

import org.joml.Vector3i;

public abstract class BlockBuildTask extends BuildTask<Vector3i> {
    protected BlockBuildTask(Iterator<Vector3i> iterator) {
        super(iterator, 100, 5000);
    }
}
