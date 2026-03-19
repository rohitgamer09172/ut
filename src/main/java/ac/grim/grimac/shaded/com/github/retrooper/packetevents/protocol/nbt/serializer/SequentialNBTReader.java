/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.NBTReader;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public final class SequentialNBTReader
implements NBTReader<NBT, DataInputStream> {
    public static final SequentialNBTReader INSTANCE = new SequentialNBTReader();
    private static final Map<NBTType<?>, TagSkip> TAG_SKIPS = new HashMap(16);
    private static final Map<NBTType<?>, TagBinaryReader> TAG_BINARY_READERS = new HashMap(16);

    @Override
    public NBT deserializeTag(NBTLimiter limiter, DataInputStream from, boolean named) throws IOException {
        NBTType<?> type = DefaultNBTSerializer.INSTANCE.readTagType(limiter, from);
        if (named) {
            int len = from.readUnsignedShort();
            from.skipBytes(len);
        }
        NBT nbt = type == NBTType.COMPOUND ? new Compound(from, limiter, () -> {}) : (type == NBTType.LIST ? new List(from, limiter, () -> {}) : DefaultNBTSerializer.INSTANCE.readTag(limiter, from, type));
        return nbt;
    }

    private static void checkReadable(NBT lastRead) {
        if (lastRead == null) {
            return;
        }
        if (lastRead instanceof Iterator && ((Iterator)((Object)lastRead)).hasNext()) {
            throw new IllegalStateException("Previous nbt has not been read completely");
        }
    }

    public static void intToBytes(byte[] array, int val, int offset) {
        array[offset] = (byte)(val >>> 24 & 0xFF);
        array[offset + 1] = (byte)(val >>> 16 & 0xFF);
        array[offset + 2] = (byte)(val >>> 8 & 0xFF);
        array[offset + 3] = (byte)(val & 0xFF);
    }

    static {
        TAG_SKIPS.put(NBTType.BYTE, (limiter, in) -> {
            limiter.increment(9);
            in.skipBytes(1);
        });
        TAG_SKIPS.put(NBTType.SHORT, (limiter, in) -> {
            limiter.increment(10);
            in.skipBytes(2);
        });
        TAG_SKIPS.put(NBTType.INT, (limiter, in) -> {
            limiter.increment(12);
            in.skipBytes(4);
        });
        TAG_SKIPS.put(NBTType.LONG, (limiter, in) -> {
            limiter.increment(16);
            in.skipBytes(8);
        });
        TAG_SKIPS.put(NBTType.FLOAT, (limiter, in) -> {
            limiter.increment(12);
            in.skipBytes(4);
        });
        TAG_SKIPS.put(NBTType.DOUBLE, (limiter, in) -> {
            limiter.increment(16);
            in.skipBytes(8);
        });
        TAG_SKIPS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int length = in.readInt();
            limiter.increment(length * 1);
            limiter.checkReadability(length * 1);
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.STRING, (limiter, in) -> {
            limiter.increment(36);
            int length = in.readUnsignedShort();
            limiter.increment(length * 2);
            in.skipBytes(length);
        });
        TAG_SKIPS.put(NBTType.LIST, (limiter, in) -> {
            limiter.enterDepth();
            try {
                limiter.increment(36);
                NBTType<?> listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in);
                int length = in.readInt();
                limiter.increment(length * 4);
                for (int i = 0; i < length; ++i) {
                    TAG_SKIPS.get(listType).skip(limiter, in);
                }
            }
            finally {
                limiter.exitDepth();
            }
        });
        TAG_SKIPS.put(NBTType.COMPOUND, (limiter, in) -> {
            limiter.enterDepth();
            try {
                NBTType<?> valueType;
                limiter.increment(48);
                HashSet<String> names = new HashSet<String>();
                while ((valueType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in)) != NBTType.END) {
                    String name = DefaultNBTSerializer.readString(limiter, in);
                    if (names.add(name)) {
                        limiter.increment(36);
                    }
                    TAG_SKIPS.get(valueType).skip(limiter, in);
                }
            }
            finally {
                limiter.exitDepth();
            }
        });
        TAG_SKIPS.put(NBTType.INT_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int length = in.readInt();
            limiter.increment(length * 4);
            limiter.checkReadability(length * 4);
            in.skipBytes(length * 4);
        });
        TAG_SKIPS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int length = in.readInt();
            limiter.increment(length * 8);
            limiter.checkReadability(length * 8);
            in.skipBytes(length * 8);
        });
        TAG_BINARY_READERS.put(NBTType.BYTE, (limiter, in) -> {
            limiter.increment(9);
            return new byte[]{in.readByte()};
        });
        TAG_BINARY_READERS.put(NBTType.SHORT, (limiter, in) -> {
            limiter.increment(10);
            return new byte[]{in.readByte(), in.readByte()};
        });
        TAG_BINARY_READERS.put(NBTType.INT, (limiter, in) -> {
            limiter.increment(12);
            byte[] bytes = new byte[4];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.LONG, (limiter, in) -> {
            limiter.increment(16);
            byte[] bytes = new byte[8];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.FLOAT, (limiter, in) -> {
            limiter.increment(12);
            byte[] bytes = new byte[4];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.DOUBLE, (limiter, in) -> {
            limiter.increment(16);
            byte[] bytes = new byte[8];
            in.readFully(bytes);
            return bytes;
        });
        TAG_BINARY_READERS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int len = in.readInt();
            if (len >= 0x1000000) {
                throw new IllegalArgumentException("Byte array length is too large: " + len);
            }
            limiter.increment(len * 1);
            limiter.checkReadability(len * 1);
            byte[] array = new byte[4 + len];
            SequentialNBTReader.intToBytes(array, len, 0);
            in.readFully(array, 4, len);
            return array;
        });
        TAG_BINARY_READERS.put(NBTType.STRING, (limiter, in) -> {
            limiter.increment(36);
            String string = in.readUTF();
            limiter.increment(string.length() * 2);
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();){
                byte[] byArray;
                try (DataOutputStream out = new DataOutputStream(bytes);){
                    out.writeUTF(string);
                    byArray = bytes.toByteArray();
                }
                return byArray;
            }
        });
        TAG_BINARY_READERS.put(NBTType.LIST, (limiter, in) -> {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1050)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        });
        TAG_BINARY_READERS.put(NBTType.COMPOUND, (limiter, in) -> {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1050)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        });
        TAG_BINARY_READERS.put(NBTType.INT_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int len = in.readInt();
            limiter.increment(len * 4);
            limiter.checkReadability(len * 4);
            byte[] array = new byte[4 + len * 4];
            SequentialNBTReader.intToBytes(array, len, 0);
            in.readFully(array, 4, len * 4);
            return array;
        });
        TAG_BINARY_READERS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
            limiter.increment(24);
            int len = in.readInt();
            limiter.increment(len * 8);
            limiter.checkReadability(len * 8);
            byte[] array = new byte[4 + len * 8];
            SequentialNBTReader.intToBytes(array, len, 0);
            in.readFully(array, 4, len * 8);
            return array;
        });
    }

    public static class Compound
    extends NBT
    implements Iterator<Map.Entry<String, NBT>>,
    Iterable<Map.Entry<String, NBT>>,
    Skippable,
    Closeable {
        private final DataInputStream stream;
        private final NBTLimiter limiter;
        private final Runnable onComplete;
        private NBTType<?> nextType;
        private NBT lastRead;
        private boolean hasReadType;

        private Compound(DataInputStream stream, NBTLimiter limiter, Runnable onComplete) {
            this.stream = stream;
            this.limiter = limiter;
            this.onComplete = onComplete;
            limiter.increment(48);
            this.runCompleted();
        }

        @Override
        public NBTType<?> getType() {
            return NBTType.COMPOUND;
        }

        @Override
        public boolean equals(Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public NBT copy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            SequentialNBTReader.checkReadable(this.lastRead);
            if (!this.hasReadType) {
                try {
                    this.nextType = DefaultNBTSerializer.INSTANCE.readTagType(this.limiter, this.stream);
                    this.hasReadType = true;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return this.nextType != NBTType.END;
        }

        @Override
        public Map.Entry<String, NBT> next() {
            if (!this.hasNext()) {
                throw new IllegalStateException("No more elements in compound");
            }
            try {
                this.hasReadType = false;
                String name = DefaultNBTSerializer.readString(this.limiter, this.stream);
                if (this.nextType == NBTType.COMPOUND) {
                    this.lastRead = new Compound(this.stream, this.limiter, this::runCompleted);
                } else if (this.nextType == NBTType.LIST) {
                    this.lastRead = new List(this.stream, this.limiter, this::runCompleted);
                } else {
                    this.lastRead = DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.nextType);
                    this.runCompleted();
                }
                this.limiter.increment(36);
                return new AbstractMap.SimpleEntry<String, NBT>(name, this.lastRead);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void runCompleted() {
            if (!this.hasNext()) {
                this.onComplete.run();
            }
        }

        @Override
        @NotNull
        public Iterator<Map.Entry<String, NBT>> iterator() {
            return this;
        }

        @Override
        public void skip() {
            if (this.lastRead instanceof Skippable) {
                ((Skippable)((Object)this.lastRead)).skip();
            }
            if (!this.hasNext()) {
                return;
            }
            try {
                int len = this.stream.readUnsignedShort();
                this.stream.skipBytes(len);
                ((TagSkip)TAG_SKIPS.get(this.nextType)).skip(this.limiter, this.stream);
                this.limiter.increment(36);
                ((TagSkip)TAG_SKIPS.get(NBTType.COMPOUND)).skip(this.limiter, this.stream);
                this.hasReadType = true;
                this.nextType = NBTType.END;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.runCompleted();
        }

        @Override
        public void skipOne() {
            SequentialNBTReader.checkReadable(this.lastRead);
            if (!this.hasNext()) {
                return;
            }
            try {
                int len = this.stream.readUnsignedShort();
                this.stream.skipBytes(len);
                ((TagSkip)TAG_SKIPS.get(this.nextType)).skip(this.limiter, this.stream);
                this.limiter.increment(36);
                this.hasReadType = false;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.runCompleted();
        }

        public NBTCompound readFully() {
            try {
                if (this.lastRead instanceof Skippable) {
                    ((Skippable)((Object)this.lastRead)).skip();
                }
                if (!this.hasNext()) {
                    return new NBTCompound();
                }
                NBTCompound compound = new NBTCompound();
                do {
                    String name = DefaultNBTSerializer.readString(this.limiter, this.stream);
                    NBT nbt = DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.nextType);
                    this.limiter.increment(36);
                    compound.setTag(name, nbt);
                } while ((this.nextType = DefaultNBTSerializer.INSTANCE.readTagType(this.limiter, this.stream)) != NBTType.END);
                this.hasReadType = true;
                this.runCompleted();
                return compound;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*
         * Enabled aggressive exception aggregation
         */
        public byte[] readFullyAsBytes() {
            try {
                if (this.lastRead instanceof Skippable) {
                    ((Skippable)((Object)this.lastRead)).skip();
                }
                if (!this.hasNext()) {
                    return new byte[]{10, 0};
                }
                try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();){
                    byte[] byArray;
                    try (DataOutputStream out = new DataOutputStream(bytes);){
                        out.write(10);
                        do {
                            out.write((Integer)DefaultNBTSerializer.INSTANCE.typeToId.get(this.nextType));
                            byte[] name = ((TagBinaryReader)TAG_BINARY_READERS.get(NBTType.STRING)).read(NBTLimiter.noop(), this.stream);
                            this.limiter.increment(name.length * 2 + 28);
                            out.write(name);
                            byte[] nbt = ((TagBinaryReader)TAG_BINARY_READERS.get(this.nextType)).read(this.limiter, this.stream);
                            this.limiter.increment(36);
                            out.write(nbt);
                        } while ((this.nextType = DefaultNBTSerializer.INSTANCE.readTagType(this.limiter, this.stream)) != NBTType.END);
                        out.write(0);
                        this.hasReadType = true;
                        this.runCompleted();
                        byArray = bytes.toByteArray();
                    }
                    return byArray;
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {
            this.stream.close();
        }
    }

    public static class List
    extends NBT
    implements Iterator<NBT>,
    Iterable<NBT>,
    Skippable,
    Closeable {
        private final DataInputStream stream;
        private final NBTLimiter limiter;
        private final Runnable onComplete;
        private final NBTType<?> listType;
        private NBT lastRead;
        public int remaining;

        private List(DataInputStream stream, NBTLimiter limiter, Runnable onComplete) {
            this.stream = stream;
            this.limiter = limiter;
            this.onComplete = onComplete;
            limiter.increment(37);
            try {
                this.listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, stream);
                this.remaining = stream.readInt();
                limiter.increment(this.remaining * 4);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.runCompleted();
        }

        @Override
        public NBTType<?> getType() {
            return NBTType.LIST;
        }

        @Override
        public boolean equals(Object other) {
            return this == other;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public NBT copy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return this.remaining > 0;
        }

        @Override
        public NBT next() {
            SequentialNBTReader.checkReadable(this.lastRead);
            if (!this.hasNext()) {
                throw new IllegalStateException("No more elements in list");
            }
            try {
                --this.remaining;
                if (this.listType == NBTType.COMPOUND) {
                    this.lastRead = new Compound(this.stream, this.limiter, this::runCompleted);
                } else if (this.listType == NBTType.LIST) {
                    this.lastRead = new List(this.stream, this.limiter, this::runCompleted);
                } else {
                    this.lastRead = DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.listType);
                    this.runCompleted();
                }
                return this.lastRead;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void runCompleted() {
            if (!this.hasNext()) {
                this.onComplete.run();
            }
        }

        @Override
        @NotNull
        public Iterator<NBT> iterator() {
            return this;
        }

        @Override
        public void skip() {
            if (this.lastRead instanceof Skippable) {
                ((Skippable)((Object)this.lastRead)).skip();
            }
            if (!this.hasNext()) {
                return;
            }
            try {
                TagSkip typeSkip = (TagSkip)TAG_SKIPS.get(this.listType);
                for (int i = 0; i < this.remaining; ++i) {
                    typeSkip.skip(this.limiter, this.stream);
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.remaining = 0;
            this.runCompleted();
        }

        @Override
        public void skipOne() {
            SequentialNBTReader.checkReadable(this.lastRead);
            if (!this.hasNext()) {
                return;
            }
            try {
                ((TagSkip)TAG_SKIPS.get(this.listType)).skip(this.limiter, this.stream);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            --this.remaining;
            this.runCompleted();
        }

        public NBTList<NBT> readFully() {
            try {
                if (this.lastRead instanceof Skippable) {
                    ((Skippable)((Object)this.lastRead)).skip();
                }
                if (!this.hasNext()) {
                    return new NBTList<NBT>(this.listType, 0);
                }
                NBTList<NBT> list = new NBTList<NBT>(this.listType, this.remaining);
                for (int i = 0; i < this.remaining; ++i) {
                    list.addTag(DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.listType));
                }
                this.remaining = 0;
                this.runCompleted();
                return list;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public byte[] readFullyAsBinary() {
            try {
                if (this.lastRead instanceof Skippable) {
                    ((Skippable)((Object)this.lastRead)).skip();
                }
                if (!this.hasNext()) {
                    return new byte[]{9};
                }
                byte[] array = null;
                for (int i = 0; i < this.remaining; ++i) {
                    byte[] element = ((TagBinaryReader)TAG_BINARY_READERS.get(this.listType)).read(this.limiter, this.stream);
                    if (array == null) {
                        array = new byte[6 + this.remaining * element.length];
                        array[0] = 9;
                        array[1] = ((Integer)DefaultNBTSerializer.INSTANCE.typeToId.get(this.listType)).byteValue();
                        array[2] = (byte)(this.remaining >>> 24);
                        array[3] = (byte)(this.remaining >>> 16);
                        array[4] = (byte)(this.remaining >>> 8);
                        array[5] = (byte)this.remaining;
                    }
                    System.arraycopy(element, 0, array, 5 + i * element.length, element.length);
                }
                this.remaining = 0;
                this.runCompleted();
                return array;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {
            this.stream.close();
        }
    }

    @FunctionalInterface
    private static interface TagBinaryReader {
        public byte[] read(NBTLimiter var1, DataInput var2) throws IOException;
    }

    @FunctionalInterface
    private static interface TagSkip {
        public void skip(NBTLimiter var1, DataInput var2) throws IOException;
    }

    static interface Skippable {
        public void skip();

        public void skipOne();
    }
}

