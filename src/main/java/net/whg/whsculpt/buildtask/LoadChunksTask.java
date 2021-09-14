package net.whg.whsculpt.buildtask;

import org.bukkit.World;
import org.joml.Vector3i;

public class LoadChunksTask extends BuildTask<Vector3i> {
    private final World world;

    public LoadChunksTask(RegionIterator iterator, World world) {
        super(iterator, 1, 1);
        this.world = world;
    }

    @Override
    protected boolean update(Vector3i pos) {
        var chunk = world.getChunkAt(pos.x, pos.z);
        chunk.setForceLoaded(true);
        chunk.load(true);
        return false;
    }
}
