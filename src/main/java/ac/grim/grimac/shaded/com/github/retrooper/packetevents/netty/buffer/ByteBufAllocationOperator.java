/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

public interface ByteBufAllocationOperator {
    public Object wrappedBuffer(byte[] var1);

    public Object copiedBuffer(byte[] var1);

    public Object buffer();

    public Object buffer(int var1);

    public Object directBuffer();

    public Object directBuffer(int var1);

    public Object compositeBuffer();

    public Object compositeBuffer(int var1);

    public Object emptyBuffer();
}

