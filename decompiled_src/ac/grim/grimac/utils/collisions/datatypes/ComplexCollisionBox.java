/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import java.util.Arrays;
import java.util.List;

public class ComplexCollisionBox
implements CollisionBox {
    public static final int DEFAULT_MAX_COLLISION_BOX_SIZE = 15;
    private final SimpleCollisionBox[] boxes;
    private int currentLength;

    public ComplexCollisionBox(SimpleCollisionBox ... boxes) {
        this(15, boxes);
    }

    public ComplexCollisionBox(int maxIndex) {
        this.boxes = new SimpleCollisionBox[maxIndex];
    }

    public ComplexCollisionBox(int maxIndex, SimpleCollisionBox ... boxes) {
        this.boxes = new SimpleCollisionBox[maxIndex];
        this.currentLength = Math.min(maxIndex, boxes.length);
        System.arraycopy(boxes, 0, this.boxes, 0, this.currentLength);
    }

    public boolean add(SimpleCollisionBox collisionBox) {
        this.boxes[this.currentLength] = collisionBox;
        ++this.currentLength;
        return this.currentLength <= this.boxes.length;
    }

    @Override
    public CollisionBox union(SimpleCollisionBox other) {
        this.add(other);
        return this;
    }

    @Override
    public boolean isCollided(SimpleCollisionBox other) {
        for (int i = 0; i < this.currentLength; ++i) {
            if (!this.boxes[i].isCollided(other)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isIntersected(SimpleCollisionBox other) {
        for (int i = 0; i < this.currentLength; ++i) {
            if (!this.boxes[i].isIntersected(other)) continue;
            return true;
        }
        return false;
    }

    @Override
    public CollisionBox copy() {
        ComplexCollisionBox copy = new ComplexCollisionBox(this.boxes.length);
        for (int i = 0; i < this.currentLength; ++i) {
            copy.boxes[i] = this.boxes[i].copy();
        }
        copy.currentLength = this.currentLength;
        return copy;
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        for (int i = 0; i < this.currentLength; ++i) {
            this.boxes[i].offset(x, y, z);
        }
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        list.addAll(Arrays.asList(this.boxes).subList(0, this.currentLength));
    }

    @Override
    public int downCast(SimpleCollisionBox[] list) {
        System.arraycopy(this.boxes, 0, list, 0, this.currentLength);
        return this.currentLength;
    }

    @Override
    public boolean isNull() {
        for (int i = 0; i < this.currentLength; ++i) {
            if (this.boxes[i].isNull()) continue;
            return false;
        }
        return true;
    }

    public int size() {
        int size = 0;
        for (SimpleCollisionBox box : this.boxes) {
            if (box == null) continue;
            ++size;
        }
        return size;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }
}

