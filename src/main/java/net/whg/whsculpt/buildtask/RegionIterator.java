package net.whg.whsculpt.buildtask;

import net.whg.utils.math.Vec3;

/**
 * Iterates over a 3D grid region in z->x->y order.
 */
public class RegionIterator implements Iterator<Vec3> {
    private final Vec3 min;
    private final Vec3 max;
    private Vec3 current;

    /**
     * Creates a new RegionIterator.
     * 
     * @param min - The min region bounds.
     * @param max - The max region bounds.
     */
    public RegionIterator(Vec3 min, Vec3 max) {
        this.min = min;
        this.max = max;
        current = min;
    }

    @Override
    public boolean update() {
        var x = current.x;
        var y = current.y;
        var z = current.z;
        var done = false;

        z++;
        if (z > max.z) {
            z = min.z;
            x++;
            if (x > max.x) {
                x = min.x;
                y++;
                if (y > max.y) {
                    y = min.y;
                    done = true;
                }
            }
        }

        current = new Vec3(x, y, z);
        return done;
    }

    @Override
    public Vec3 get() {
        return current;
    }
}
