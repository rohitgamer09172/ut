/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.NBTSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

public class DefaultNBTSerializer
extends NBTSerializer<DataInput, DataOutput> {
    @ApiStatus.Internal
    public static final int OBJECT_HEADER_BYTES = 8;
    @ApiStatus.Internal
    public static final int ARRAY_HEADER_BYTES = 12;
    @ApiStatus.Internal
    public static final int OBJECT_REF_BYTES = 4;
    @ApiStatus.Internal
    public static final int STRING_SIZE_BYTES = 28;
    public static final DefaultNBTSerializer INSTANCE = new DefaultNBTSerializer();

    public DefaultNBTSerializer() {
        super((limiter, dataInput) -> dataInput.readByte(), DataOutput::writeByte, (limiter, dataInput) -> {
            dataInput.skipBytes(dataInput.readUnsignedShort());
            return "";
        }, DataOutput::writeUTF);
        this.registerType(NBTType.END, 0, (limiter, stream) -> {
            limiter.increment(8);
            return NBTEnd.INSTANCE;
        }, (stream, tag) -> {});
        this.registerType(NBTType.BYTE, 1, (limiter, stream) -> {
            limiter.increment(9);
            return new NBTByte(stream.readByte());
        }, (stream, tag) -> stream.writeByte(tag.getAsByte()));
        this.registerType(NBTType.SHORT, 2, (limiter, stream) -> {
            limiter.increment(24);
            return new NBTShort(stream.readShort());
        }, (stream, tag) -> stream.writeShort(tag.getAsShort()));
        this.registerType(NBTType.INT, 3, (limiter, stream) -> {
            limiter.increment(12);
            return new NBTInt(stream.readInt());
        }, (stream, tag) -> stream.writeInt(tag.getAsInt()));
        this.registerType(NBTType.LONG, 4, (limiter, stream) -> {
            limiter.increment(16);
            return new NBTLong(stream.readLong());
        }, (stream, tag) -> stream.writeLong(tag.getAsLong()));
        this.registerType(NBTType.FLOAT, 5, (limiter, stream) -> {
            limiter.increment(12);
            return new NBTFloat(stream.readFloat());
        }, (stream, tag) -> stream.writeFloat(tag.getAsFloat()));
        this.registerType(NBTType.DOUBLE, 6, (limiter, stream) -> {
            limiter.increment(16);
            return new NBTDouble(stream.readDouble());
        }, (stream, tag) -> stream.writeDouble(tag.getAsDouble()));
        this.registerType(NBTType.BYTE_ARRAY, 7, (limiter, stream) -> {
            limiter.increment(24);
            int length = stream.readInt();
            if (length >= 0x1000000) {
                throw new IllegalArgumentException("Byte array length is too large: " + length);
            }
            limiter.increment(1 * length);
            limiter.checkReadability(1 * length);
            byte[] array = new byte[length];
            stream.readFully(array);
            return new NBTByteArray(array);
        }, (stream, tag) -> {
            byte[] array = tag.getValue();
            stream.writeInt(array.length);
            stream.write(array);
        });
        this.registerType(NBTType.STRING, 8, (limiter, stream) -> {
            limiter.increment(36);
            String string = stream.readUTF();
            limiter.increment(string.length() * 2);
            return new NBTString(string);
        }, (stream, tag) -> stream.writeUTF(tag.getValue()));
        this.registerType(NBTType.LIST, 9, (limiter, stream) -> {
            limiter.enterDepth();
            try {
                limiter.increment(36);
                NBTType<?> valueType = this.readTagType(limiter, stream);
                int size = stream.readInt();
                if (valueType == NBTType.END && size > 0) {
                    throw new IllegalStateException("Missing nbt list values tag type");
                }
                limiter.increment(4 * size);
                NBTList list = new NBTList(valueType, size);
                for (int i = 0; i < size; ++i) {
                    list.addTag(this.readTag(limiter, stream, valueType));
                }
                NBTList nBTList = list;
                return nBTList;
            }
            finally {
                limiter.exitDepth();
            }
        }, (stream, tag) -> {
            this.writeTagType(stream, tag.getTagsType());
            stream.writeInt(tag.size());
            for (NBT value : tag.getTags()) {
                this.writeTag(stream, value);
            }
        });
        this.registerType(NBTType.COMPOUND, 10, (limiter, stream) -> {
            limiter.enterDepth();
            try {
                NBTType<?> valueType;
                limiter.increment(48);
                NBTCompound compound = new NBTCompound();
                while ((valueType = this.readTagType(limiter, stream)) != NBTType.END) {
                    String name = DefaultNBTSerializer.readString(limiter, stream);
                    NBT tag = this.readTag(limiter, stream, valueType);
                    if (!compound.getTags().containsKey(name)) {
                        limiter.increment(36);
                    }
                    compound.setTag(name, tag);
                }
                NBTCompound nBTCompound = compound;
                return nBTCompound;
            }
            finally {
                limiter.exitDepth();
            }
        }, (stream, tag) -> {
            for (Map.Entry<String, NBT> entry : tag.getTags().entrySet()) {
                NBT value = entry.getValue();
                this.writeTagType(stream, value.getType());
                this.writeTagName(stream, entry.getKey());
                this.writeTag(stream, value);
            }
            this.writeTagType(stream, NBTType.END);
        });
        this.registerType(NBTType.INT_ARRAY, 11, (limiter, stream) -> {
            limiter.increment(24);
            int length = stream.readInt();
            if (length >= 0x1000000) {
                throw new IllegalArgumentException("Int array length is too large: " + length);
            }
            limiter.increment(length * 4);
            limiter.checkReadability(length * 4);
            int[] array = new int[length];
            for (int i = 0; i < array.length; ++i) {
                array[i] = stream.readInt();
            }
            return new NBTIntArray(array);
        }, (stream, tag) -> {
            int[] array = tag.getValue();
            stream.writeInt(array.length);
            for (int i : array) {
                stream.writeInt(i);
            }
        });
        this.registerType(NBTType.LONG_ARRAY, 12, (limiter, stream) -> {
            limiter.increment(24);
            int length = stream.readInt();
            if (length >= 0x1000000) {
                throw new IllegalArgumentException("Long array length is too large: " + length);
            }
            limiter.increment(length * 8);
            limiter.checkReadability(length * 8);
            long[] array = new long[length];
            for (int i = 0; i < array.length; ++i) {
                array[i] = stream.readLong();
            }
            return new NBTLongArray(array);
        }, (stream, tag) -> {
            long[] array = tag.getValue();
            stream.writeInt(array.length);
            for (long i : array) {
                stream.writeLong(i);
            }
        });
    }

    @ApiStatus.Internal
    public static String readString(NBTLimiter limiter, DataInput input) throws IOException {
        String string = input.readUTF();
        limiter.increment(28 + 2 * string.length());
        return string;
    }
}

