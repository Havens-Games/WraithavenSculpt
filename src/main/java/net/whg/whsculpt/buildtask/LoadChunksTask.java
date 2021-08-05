package net.whg.whsculpt.buildtask;

import org.bukkit.World;

import net.whg.utils.math.Vec3;

public class LoadChunksTask extends BuildTask<Vec3> {
    private final World world;

    public LoadChunksTask(RegionIterator iterator, World world) {
        super(iterator, 1, 1);
        this.world = world;
    }

    @Override
    protected boolean update(Vec3 pos) {
        var chunk = world.getChunkAt(pos.x, pos.z);
        chunk.setForceLoaded(true);
        chunk.load(true);
        return false;
    }
}
