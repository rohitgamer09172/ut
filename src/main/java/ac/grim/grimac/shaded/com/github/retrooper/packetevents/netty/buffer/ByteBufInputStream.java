/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.PacketProcessException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ByteBufInputStream
extends InputStream
implements DataInput {
    private final Object buffer;
    private final int startIndex;
    private final int endIndex;
    private final boolean releaseOnClose;
    private final StringBuilder lineBuf = new StringBuilder();
    private boolean closed;

    public ByteBufInputStream(Object buffer) {
        this(buffer, ByteBufHelper.readableBytes(buffer));
    }

    public ByteBufInputStream(Object buffer, int length) {
        this(buffer, length, false);
    }

    public ByteBufInputStream(Object buffer, boolean releaseOnClose) {
        this(buffer, ByteBufHelper.readableBytes(buffer), releaseOnClose);
    }

    public ByteBufInputStream(Object buffer, int maxLength, boolean releaseOnClose) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        if (maxLength < 0) {
            if (releaseOnClose) {
                ByteBufHelper.release(buffer);
            }
            throw new IllegalArgumentException("maxLength: " + maxLength);
        }
        if (ByteBufHelper.readableBytes(buffer) > maxLength) {
            if (releaseOnClose) {
                ByteBufHelper.release(buffer);
            }
            throw new IndexOutOfBoundsException("Too many bytes to be read - Found " + ByteBufHelper.readableBytes(buffer) + ", maximum is " + maxLength);
        }
        this.releaseOnClose = releaseOnClose;
        this.buffer = buffer;
        this.startIndex = ByteBufHelper.readerIndex(buffer);
        this.endIndex = this.startIndex + ByteBufHelper.readableBytes(buffer);
        ByteBufHelper.markReaderIndex(buffer);
    }

    public int readBytes() {
        return ByteBufHelper.readerIndex(this.buffer) - this.startIndex;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        }
        finally {
            if (this.releaseOnClose && !this.closed) {
                this.closed = true;
                ByteBufHelper.release(this.buffer);
            }
        }
    }

    @Override
    public int available() throws IOException {
        return this.endIndex - ByteBufHelper.readerIndex(this.buffer);
    }

    @Override
    public void mark(int readlimit) {
        ByteBufHelper.markReaderIndex(this.buffer);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        return !ByteBufHelper.isReadable(this.buffer) ? -1 : ByteBufHelper.readByte(this.buffer) & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int available = this.available();
        if (available == 0) {
            return -1;
        }
        len = Math.min(available, len);
        ByteBufHelper.readBytes(this.buffer, b, off, len);
        return len;
    }

    @Override
    public void reset() throws IOException {
        ByteBufHelper.resetReaderIndex(this.buffer);
    }

    @Override
    public long skip(long n) throws IOException {
        return n > Integer.MAX_VALUE ? (long)this.skipBytes(Integer.MAX_VALUE) : (long)this.skipBytes((int)n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        this.checkAvailable(1);
        return this.read() != 0;
    }

    public byte[] readBytes(int len) {
        byte[] bytes = new byte[len];
        ByteBufHelper.readBytes(this.buffer, bytes);
        return bytes;
    }

    @Override
    public byte readByte() throws IOException {
        if (!ByteBufHelper.isReadable(this.buffer)) {
            throw new EOFException();
        }
        return ByteBufHelper.readByte(this.buffer);
    }

    @Override
    public char readChar() throws IOException {
        return (char)this.readShort();
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        this.readFully(b, 0, b.length);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        this.checkAvailable(len);
        ByteBufHelper.readBytes(this.buffer, b, off, len);
    }

    @Override
    public int readInt() throws IOException {
        this.checkAvailable(4);
        return ByteBufHelper.readInt(this.buffer);
    }

    @Override
    public String readLine() throws IOException {
        this.lineBuf.setLength(0);
        while (ByteBufHelper.isReadable(this.buffer)) {
            short c = ByteBufHelper.readUnsignedByte(this.buffer);
            switch (c) {
                case 13: {
                    if (ByteBufHelper.isReadable(this.buffer) && (char)ByteBufHelper.getUnsignedByte(this.buffer, ByteBufHelper.readerIndex(this.buffer)) == '\n') {
                        ByteBufHelper.skipBytes(this.buffer, 1);
                    }
                }
                case 10: {
                    return this.lineBuf.toString();
                }
            }
            this.lineBuf.append((char)c);
        }
        return this.lineBuf.length() > 0 ? this.lineBuf.toString() : null;
    }

    @Override
    public long readLong() throws IOException {
        this.checkAvailable(8);
        return ByteBufHelper.readLong(this.buffer);
    }

    public long[] readLongs(int size) throws IOException {
        long[] array = new long[size];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.readLong();
        }
        return array;
    }

    @Override
    public short readShort() throws IOException {
        this.checkAvailable(2);
        return ByteBufHelper.readShort(this.buffer);
    }

    @Override
    public String readUTF() throws IOException {
        String text = DataInputStream.readUTF(this);
        return text;
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return this.readByte() & 0xFF;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return this.readShort() & 0xFFFF;
    }

    @Override
    public int skipBytes(int n) throws IOException {
        int nBytes = Math.min(this.available(), n);
        ByteBufHelper.skipBytes(this.buffer, nBytes);
        return nBytes;
    }

    private void checkAvailable(int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        }
        if (fieldSize > this.available()) {
            int value = this.available();
            String msg = "fieldSize is too long! Length is " + fieldSize + ", but maximum is " + value;
            if (value == 0) {
                throw new PacketProcessException(msg);
            }
            throw new EOFException(msg);
        }
    }
}

