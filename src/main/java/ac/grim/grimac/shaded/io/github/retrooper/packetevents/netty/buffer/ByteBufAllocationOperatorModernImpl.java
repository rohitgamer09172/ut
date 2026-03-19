/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.buffer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import io.netty.buffer.Unpooled;

public class ByteBufAllocationOperatorModernImpl
implements ByteBufAllocationOperator {
    @Override
    public Object wrappedBuffer(byte[] bytes) {
        return Unpooled.wrappedBuffer((byte[])bytes);
    }

    @Override
    public Object copiedBuffer(byte[] bytes) {
        return Unpooled.copiedBuffer((byte[])bytes);
    }

    @Override
    public Object buffer() {
        return Unpooled.buffer();
    }

    @Override
    public Object buffer(int initialCapacity) {
        return Unpooled.buffer((int)initialCapacity);
    }

    @Override
    public Object directBuffer() {
        return Unpooled.directBuffer();
    }

    @Override
    public Object directBuffer(int initialCapacity) {
        return Unpooled.directBuffer((int)initialCapacity);
    }

    @Override
    public Object compositeBuffer() {
        return Unpooled.compositeBuffer();
    }

    @Override
    public Object compositeBuffer(int maxNumComponents) {
        return Unpooled.compositeBuffer((int)maxNumComponents);
    }

    @Override
    public Object emptyBuffer() {
        return Unpooled.EMPTY_BUFFER;
    }
}

