/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteBufOutputStream
extends OutputStream
implements DataOutput {
    private final Object buffer;
    private final int startIndex;
    private final DataOutputStream utf8out = new DataOutputStream(this);

    public ByteBufOutputStream(Object buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        this.buffer = buffer;
        this.startIndex = ByteBufHelper.writerIndex(buffer);
    }

    public int writtenBytes() {
        return ByteBufHelper.writerIndex(this.buffer) - this.startIndex;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (len != 0) {
            ByteBufHelper.writeBytes(this.buffer, b, off, len);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        ByteBufHelper.writeBytes(this.buffer, b);
    }

    @Override
    public void write(int b) throws IOException {
        ByteBufHelper.writeByte(this.buffer, b);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        ByteBufHelper.writeBoolean(this.buffer, v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        ByteBufHelper.writeByte(this.buffer, v);
    }

    @Override
    @Deprecated
    public void writeBytes(String s) throws IOException {
        throw new IllegalStateException("This operation is not supported!");
    }

    @Override
    public void writeChar(int v) throws IOException {
        ByteBufHelper.writeChar(this.buffer, v);
    }

    @Override
    public void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            ByteBufHelper.writeChar(this.buffer, s.charAt(i));
        }
    }

    @Override
    public void writeDouble(double v) throws IOException {
        ByteBufHelper.writeDouble(this.buffer, v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        ByteBufHelper.writeFloat(this.buffer, v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        ByteBufHelper.writeInt(this.buffer, v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        ByteBufHelper.writeLong(this.buffer, v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        ByteBufHelper.writeShort(this.buffer, v);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        this.utf8out.writeUTF(s);
    }

    public Object buffer() {
        return this.buffer;
    }
}

