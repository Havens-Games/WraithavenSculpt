package net.whg.whsculpt.buildtask;

import org.joml.Vector3i;

/**
 * Iterates over a 3D grid region in z->x->y order.
 */
public class RegionIterator implements Iterator<Vector3i> {
    private final Vector3i min;
    private final Vector3i max;
    private Vector3i current;

    /**
     * Creates a new RegionIterator.
     * 
     * @param min - The min region bounds.
     * @param max - The max region bounds.
     */
    public RegionIterator(Vector3i min, Vector3i max) {
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

        current.set(x, y, z);
        return done;
    }

    @Override
    public Vector3i get() {
        return current;
    }
}
