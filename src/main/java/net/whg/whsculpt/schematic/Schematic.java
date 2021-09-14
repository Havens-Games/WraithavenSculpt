package net.whg.whsculpt.schematic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.entity.ExtentEntityCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.EntityVisitor;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.Identity;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.joml.Vector3i;

/**
 * Represents a loaded Minecraft schematic file.
 */
public class Schematic {
    /**
     * Loads a schematic from a file.
     * 
     * @param file - The schematic file to load.
     * @return The schematic.
     * @throws IOException If the file could not be loaded.
     */
    public static Schematic loadSchematic(File file) throws IOException {
        var format = ClipboardFormats.findByFile(file);
        try (var reader = format.getReader(new FileInputStream(file))) {
            return new Schematic(reader.read());
        }
    }

    private final Clipboard s;
    private final Vector3i origin;
    private final Vector3i minimumPoint;
    private final Vector3i maximumPoint;

    private Schematic(Clipboard schematic) {
        this.s = schematic;

        var clipboardOrigin = s.getOrigin();
        origin = new Vector3i(clipboardOrigin.getX(), clipboardOrigin.getY(), clipboardOrigin.getZ());

        var min = s.getMinimumPoint();
        minimumPoint = new Vector3i(min.getX(), min.getY(), min.getZ());

        var max = s.getMaximumPoint();
        maximumPoint = new Vector3i(max.getX(), max.getY(), max.getZ());
    }

    /**
     * Gets the origin point of this schematic.
     * 
     * @return The origin point.
     */
    public Vector3i getOrigin() {
        return origin;
    }

    /**
     * Gets the minimum bounds of this schematic.
     * 
     * @return The minimum bounds location.
     */
    public Vector3i getMinimumPoint() {
        return minimumPoint;
    }

    /**
     * Gets the maximum bounds of this schematic.
     * 
     * @return The maximum bounds location.
     */
    public Vector3i getMaximumPoint() {
        return maximumPoint;
    }

    /**
     * Gets the block data at the specified location within the schematic.
     * 
     * @param pos - The block position.
     * @return The block data.
     */
    public BlockData getBlockData(Vector3i pos) {
        var targetBlock = s.getFullBlock(BlockVector3.at(pos.x, pos.y, pos.z));
        return BukkitAdapter.adapt(targetBlock);
    }

    /**
     * Spawns all entities within this schematic at the given location. Entity
     * positions within the schematic relative to the origin are maintained as well
     * as any NBT data.
     * 
     * @param location - The location to paste all entities at.
     */
    public void spawnEntities(Location location) {
        var world = BukkitAdapter.adapt(location.getWorld());
        var clipboardOrigin = s.getOrigin().toVector3();
        var target = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        ExtentEntityCopy entityCopy = new ExtentEntityCopy(clipboardOrigin, world, target.toVector3(), new Identity());
        List<? extends Entity> entities = Lists.newArrayList(s.getEntities());
        EntityVisitor entityVisitor = new EntityVisitor(entities.iterator(), entityCopy);

        try {
            Operations.complete(entityVisitor);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}
