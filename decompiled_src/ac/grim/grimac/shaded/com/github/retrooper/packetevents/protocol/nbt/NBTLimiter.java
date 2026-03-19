/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public interface NBTLimiter {
    public static final int DEFAULT_MAX_SIZE = Integer.getInteger("packetevents.nbt.default-max-size", 0x200000);
    public static final int DEFAULT_MAX_DEPTH = Integer.getInteger("packetevents.nbt.default-max-depth", 512);

    public static NBTLimiter noop() {
        return new NBTLimiter(){

            @Override
            public void increment(int amount) {
            }

            @Override
            public void checkReadability(int length) {
            }

            @Override
            public void enterDepth() {
            }

            @Override
            public void exitDepth() {
            }
        };
    }

    public static NBTLimiter forBuffer(Object byteBuf) {
        return NBTLimiter.forBuffer(byteBuf, DEFAULT_MAX_SIZE);
    }

    public static NBTLimiter forBuffer(Object byteBuf, int maxBytes) {
        return NBTLimiter.forBuffer(byteBuf, maxBytes, DEFAULT_MAX_DEPTH);
    }

    public static NBTLimiter forBuffer(final Object byteBuf, final int maxBytes, final int maxDepth) {
        return new NBTLimiter(){
            private int bytes;
            private int depth;

            @Override
            public void increment(int amount) {
                if (amount < 0) {
                    throw new IllegalArgumentException("Can't increment NBT limiter by negative amount: " + amount);
                }
                if (this.bytes + amount > maxBytes) {
                    throw new IllegalArgumentException("NBT size limit reached (" + this.bytes + "/" + maxBytes + ")");
                }
                this.bytes += amount;
            }

            @Override
            public void checkReadability(int length) {
                int readableBytes = ByteBufHelper.readableBytes(byteBuf);
                if (length > readableBytes) {
                    throw new IllegalArgumentException("Can't read more than possible: " + length + " > " + readableBytes);
                }
            }

            @Override
            public void enterDepth() {
                if (this.depth >= maxDepth) {
                    throw new IllegalArgumentException("NBT depth limit reached (" + this.depth + "/" + maxDepth + ")");
                }
                ++this.depth;
            }

            @Override
            public void exitDepth() {
                if (this.depth <= 0) {
                    throw new IllegalArgumentException("Can't exit top-level depth");
                }
                --this.depth;
            }
        };
    }

    public void increment(int var1);

    public void checkReadability(int var1);

    public void enterDepth();

    public void exitDepth();
}

