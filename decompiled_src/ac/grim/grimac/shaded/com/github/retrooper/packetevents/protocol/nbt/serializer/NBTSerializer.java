/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.NBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.NBTWriter;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@ApiStatus.NonExtendable
public class NBTSerializer<IN, OUT>
implements NBTReader<NBT, IN>,
NBTWriter<NBT, OUT> {
    protected final IdReader<IN> idReader;
    protected final IdWriter<OUT> idWriter;
    protected final NameReader<IN> nameReader;
    protected final NameWriter<OUT> nameWriter;
    protected final Map<Integer, NBTType<? extends NBT>> idToType = new HashMap<Integer, NBTType<? extends NBT>>();
    protected final Map<NBTType<? extends NBT>, Integer> typeToId = new HashMap<NBTType<? extends NBT>, Integer>();
    protected final Map<NBTType<? extends NBT>, TagReader<IN, ? extends NBT>> typeReaders = new HashMap<NBTType<? extends NBT>, TagReader<IN, ? extends NBT>>();
    protected final Map<NBTType<? extends NBT>, TagWriter<OUT, ? extends NBT>> typeWriters = new HashMap<NBTType<? extends NBT>, TagWriter<OUT, ? extends NBT>>();

    public NBTSerializer(IdReader<IN> idReader, IdWriter<OUT> idWriter, NameReader<IN> nameReader, NameWriter<OUT> nameWriter) {
        this.idReader = idReader;
        this.idWriter = idWriter;
        this.nameReader = nameReader;
        this.nameWriter = nameWriter;
    }

    @Override
    public NBT deserializeTag(NBTLimiter limiter, IN from, boolean named) throws IOException {
        NBTType<?> type = this.readTagType(limiter, from);
        if (type == NBTType.END) {
            return null;
        }
        if (named) {
            this.readTagName(limiter, from);
        }
        return this.readTag(limiter, from, type);
    }

    @Override
    public void serializeTag(OUT to, NBT tag, boolean named) throws IOException {
        NBTType<?> type = tag.getType();
        this.writeTagType(to, type);
        if (tag.getType() == NBTType.END) {
            return;
        }
        if (named) {
            this.writeTagName(to, "");
        }
        this.writeTag(to, tag);
    }

    protected <T extends NBT> void registerType(NBTType<T> type, int id, TagReader<IN, T> typeReader, TagWriter<OUT, T> typeWriter) {
        if (this.typeToId.containsKey(type)) {
            throw new IllegalArgumentException(MessageFormat.format("Nbt type {0} is already registered", type));
        }
        if (this.idToType.containsKey(id)) {
            throw new IllegalArgumentException(MessageFormat.format("Nbt type id {0} is already registered", id));
        }
        this.idToType.put(id, type);
        this.typeToId.put(type, id);
        this.typeReaders.put(type, typeReader);
        this.typeWriters.put(type, typeWriter);
    }

    NBTType<?> readTagType(NBTLimiter limiter, IN from) throws IOException {
        int id = this.idReader.readId(limiter, from);
        NBTType<? extends NBT> type = this.idToType.get(id);
        if (type == null) {
            throw new IOException(MessageFormat.format("Unknown nbt type id {0}", id));
        }
        return type;
    }

    @ApiStatus.Internal
    String readTagName(NBTLimiter limiter, IN from) throws IOException {
        return this.nameReader.readName(limiter, from);
    }

    NBT readTag(NBTLimiter limiter, IN from, NBTType<?> type) throws IOException {
        TagReader<IN, NBT> f = this.typeReaders.get(type);
        if (f == null) {
            throw new IOException(MessageFormat.format("No reader registered for nbt type {0}", type));
        }
        return f.readTag(limiter, from);
    }

    void writeTagType(OUT stream, NBTType<?> type) throws IOException {
        int id = this.typeToId.getOrDefault(type, -1);
        if (id == -1) {
            throw new IOException(MessageFormat.format("Unknown nbt type {0}", type));
        }
        this.idWriter.writeId(stream, id);
    }

    void writeTagName(OUT stream, String name) throws IOException {
        this.nameWriter.writeName(stream, name);
    }

    void writeTag(OUT stream, NBT tag) throws IOException {
        TagWriter<OUT, NBT> f = this.typeWriters.get(tag.getType());
        if (f == null) {
            throw new IOException(MessageFormat.format("No writer registered for nbt type {0}", tag.getType()));
        }
        f.writeTag(stream, tag);
    }

    @FunctionalInterface
    protected static interface IdReader<T> {
        public int readId(NBTLimiter var1, T var2) throws IOException;
    }

    @FunctionalInterface
    protected static interface IdWriter<T> {
        public void writeId(T var1, int var2) throws IOException;
    }

    @FunctionalInterface
    protected static interface NameReader<T> {
        public String readName(NBTLimiter var1, T var2) throws IOException;
    }

    @FunctionalInterface
    protected static interface NameWriter<T> {
        public void writeName(T var1, String var2) throws IOException;
    }

    @FunctionalInterface
    protected static interface TagReader<IN, T extends NBT> {
        public T readTag(NBTLimiter var1, IN var2) throws IOException;
    }

    @FunctionalInterface
    public static interface TagWriter<OUT, T extends NBT> {
        public void writeTag(OUT var1, T var2) throws IOException;
    }
}

