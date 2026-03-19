/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.GrimMath;
import java.util.HashSet;

public class OffsetCollisionBox
extends SimpleCollisionBox {
    private static final HashSet<StateType> XZ_OFFSET_BLOCKSTATES = new HashSet();
    private static final HashSet<StateType> XYZ_OFFSET_BLOCKSTATES = new HashSet();
    private float maxHorizontalModelOffset = 0.25f;
    private float maxVerticalModelOffset = 0.2f;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double offsetZ = 0.0;
    private final OffsetType offsetType;

    public OffsetCollisionBox(StateType block, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        if (block.equals(StateTypes.POINTED_DRIPSTONE)) {
            this.maxHorizontalModelOffset = 0.125f;
        }
        if (XZ_OFFSET_BLOCKSTATES.contains(block)) {
            this.offsetType = OffsetType.XZ;
            return;
        }
        if (XYZ_OFFSET_BLOCKSTATES.contains(block)) {
            this.offsetType = OffsetType.XYZ;
            return;
        }
        throw new RuntimeException("Invalid State Type for OffSetCollisionBox: " + String.valueOf(block));
    }

    @Override
    public SimpleCollisionBox offset(double x, double y, double z) {
        this.resetBlockStateOffSet();
        switch (this.offsetType.ordinal()) {
            case 0: {
                return super.offset(x, y, z);
            }
            case 1: {
                long l = GrimMath.hashCode(x, 0, z);
                this.offsetX = GrimMath.clamp(((double)((float)(l & 0xFL) / 15.0f) - 0.5) * 0.5, (double)(-this.maxHorizontalModelOffset), (double)this.maxHorizontalModelOffset);
                this.offsetZ = GrimMath.clamp(((double)((float)(l >> 8 & 0xFL) / 15.0f) - 0.5) * 0.5, (double)(-this.maxHorizontalModelOffset), (double)this.maxHorizontalModelOffset);
                return super.offset(x + this.offsetX, y, z + this.offsetZ);
            }
            case 2: {
                long l = GrimMath.hashCode(x, 0, z);
                this.offsetY = ((double)((float)(l >> 4 & 0xFL) / 15.0f) - 1.0) * (double)this.maxVerticalModelOffset;
                this.offsetX = GrimMath.clamp(((double)((float)(l & 0xFL) / 15.0f) - 0.5) * 0.5, (double)(-this.maxHorizontalModelOffset), (double)this.maxHorizontalModelOffset);
                this.offsetZ = GrimMath.clamp(((double)((float)(l >> 8 & 0xFL) / 15.0f) - 0.5) * 0.5, (double)(-this.maxHorizontalModelOffset), (double)this.maxHorizontalModelOffset);
                return super.offset(x + this.offsetX, this.offsetY, z + this.offsetZ);
            }
        }
        return null;
    }

    public void resetBlockStateOffSet() {
        this.minX += this.offsetX;
        this.minY += this.offsetY;
        this.minZ += this.offsetZ;
        this.maxX += this.offsetX;
        this.maxY += this.offsetY;
        this.maxZ += this.offsetZ;
    }

    static {
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.MANGROVE_PROPAGULE);
        XZ_OFFSET_BLOCKSTATES.addAll(BlockTags.SMALL_FLOWERS.getStates());
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.BAMBOO_SAPLING);
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.BAMBOO);
        XZ_OFFSET_BLOCKSTATES.add(StateTypes.POINTED_DRIPSTONE);
    }

    public static enum OffsetType {
        NONE,
        XZ,
        XYZ;

    }
}

