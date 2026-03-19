/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.GrimMath;

public record Vec2(float x, float y) {
    public static final Vec2 ZERO = new Vec2(0.0f, 0.0f);

    @Contract(value="_ -> new")
    @NotNull
    public Vec2 scale(float scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }

    @Contract(pure=true)
    public float dot(@NotNull Vec2 vec) {
        return this.x * vec.x + this.y * vec.y;
    }

    @Contract(value="_ -> new")
    @NotNull
    public Vec2 add(@NotNull Vec2 vec) {
        return new Vec2(this.x + vec.x, this.y + vec.y);
    }

    @Contract(value="_ -> new")
    @NotNull
    public Vec2 add(float vec) {
        return new Vec2(this.x + vec, this.y + vec);
    }

    @Contract(pure=true)
    public boolean equals(@NotNull Vec2 vec) {
        return this.x == vec.x && this.y == vec.y;
    }

    public Vec2 normalized() {
        float length = GrimMath.sqrt(this.x * this.x + this.y * this.y);
        return length < 1.0E-4f ? ZERO : new Vec2(this.x / length, this.y / length);
    }

    @Contract(pure=true)
    public float length() {
        return GrimMath.sqrt(this.x * this.x + this.y * this.y);
    }

    @Contract(pure=true)
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    @Contract(pure=true)
    public float distanceToSqr(@NotNull Vec2 vec) {
        float dx = vec.x - this.x;
        float dy = vec.y - this.y;
        return dx * dx + dy * dy;
    }

    @Contract(value=" -> new")
    @NotNull
    public Vec2 negated() {
        return new Vec2(-this.x, -this.y);
    }
}

