/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

import java.nio.charset.Charset;

public interface ByteBufOperator {
    public int capacity(Object var1);

    public Object capacity(Object var1, int var2);

    public int readerIndex(Object var1);

    public Object readerIndex(Object var1, int var2);

    public int writerIndex(Object var1);

    public Object writerIndex(Object var1, int var2);

    public int readableBytes(Object var1);

    public int writableBytes(Object var1);

    public Object clear(Object var1);

    public byte readByte(Object var1);

    public short readShort(Object var1);

    public int readMedium(Object var1);

    public int readInt(Object var1);

    public long readUnsignedInt(Object var1);

    public long readLong(Object var1);

    public void writeByte(Object var1, int var2);

    public void writeShort(Object var1, int var2);

    public void writeShortLE(Object var1, int var2);

    public void writeMedium(Object var1, int var2);

    public void writeInt(Object var1, int var2);

    public void writeLong(Object var1, long var2);

    public Object getBytes(Object var1, int var2, byte[] var3);

    public short getUnsignedByte(Object var1, int var2);

    public boolean isReadable(Object var1);

    public Object copy(Object var1);

    public Object duplicate(Object var1);

    public boolean hasArray(Object var1);

    public byte[] array(Object var1);

    public Object retain(Object var1);

    public Object retainedDuplicate(Object var1);

    public Object readSlice(Object var1, int var2);

    public Object readBytes(Object var1, byte[] var2, int var3, int var4);

    public Object readBytes(Object var1, int var2);

    public void readBytes(Object var1, byte[] var2);

    public Object writeBytes(Object var1, Object var2);

    public Object writeBytes(Object var1, byte[] var2);

    public Object writeBytes(Object var1, byte[] var2, int var3, int var4);

    public boolean release(Object var1);

    public int refCnt(Object var1);

    public Object skipBytes(Object var1, int var2);

    public String toString(Object var1, int var2, int var3, Charset var4);

    public Object markReaderIndex(Object var1);

    public Object resetReaderIndex(Object var1);

    public Object markWriterIndex(Object var1);

    public Object resetWriterIndex(Object var1);

    public Object allocateNewBuffer(Object var1);

    default public float readFloat(Object buffer) {
        return Float.intBitsToFloat(this.readInt(buffer));
    }

    default public void writeFloat(Object buffer, float value) {
        this.writeInt(buffer, Float.floatToIntBits(value));
    }

    default public double readDouble(Object buffer) {
        return Double.longBitsToDouble(this.readLong(buffer));
    }

    default public void writeDouble(Object buffer, double value) {
        this.writeLong(buffer, Double.doubleToLongBits(value));
    }

    default public char readChar(Object buffer) {
        return (char)this.readShort(buffer);
    }

    default public void writeChar(Object buffer, int value) {
        this.writeShort(buffer, value);
    }

    default public int readUnsignedShort(Object buffer) {
        return this.readShort(buffer) & 0xFFFF;
    }

    default public short readUnsignedByte(Object buffer) {
        return (short)(this.readByte(buffer) & 0xFF);
    }

    default public boolean readBoolean(Object buffer) {
        return this.readByte(buffer) != 0;
    }

    default public void writeBoolean(Object buffer, boolean value) {
        this.writeByte(buffer, value ? 1 : 0);
    }
}

