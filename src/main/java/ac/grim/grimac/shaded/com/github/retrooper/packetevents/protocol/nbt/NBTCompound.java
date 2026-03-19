/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class NBTCompound
extends NBT {
    protected final Map<String, NBT> tags = new LinkedHashMap<String, NBT>();

    public NBTType<NBTCompound> getType() {
        return NBTType.COMPOUND;
    }

    public boolean isEmpty() {
        return this.tags.isEmpty();
    }

    public boolean contains(String key) {
        return this.tags.containsKey(key);
    }

    public Set<String> getTagNames() {
        return Collections.unmodifiableSet(this.tags.keySet());
    }

    public Map<String, NBT> getTags() {
        return Collections.unmodifiableMap(this.tags);
    }

    public int size() {
        return this.tags.size();
    }

    public NBT getTagOrThrow(String key) throws NbtCodecException {
        NBT tag = this.getTagOrNull(key);
        if (tag == null) {
            throw new NbtCodecException("Tag " + key + " doesn't exist");
        }
        return tag;
    }

    @Nullable
    public NBT getTagOrNull(String key) {
        return this.tags.get(key);
    }

    public <T extends NBT> T getTagOfTypeOrThrow(String key, Class<T> type) throws NbtCodecException {
        NBT tag = this.getTagOrThrow(key);
        if (type.isInstance(tag)) {
            return (T)tag;
        }
        throw new NbtCodecException(MessageFormat.format("NBT {0} has unexpected type, expected {1}, but got {2}", key, type, tag.getClass()));
    }

    @Nullable
    public <T extends NBT> T getTagOfTypeOrNull(String key, Class<T> type) {
        NBT tag = this.getTagOrNull(key);
        if (type.isInstance(tag)) {
            return (T)tag;
        }
        return null;
    }

    public <T extends NBT> NBTList<T> getTagListOfTypeOrThrow(String key, Class<T> type) throws NbtCodecException {
        NBTList list = this.getTagOfTypeOrThrow(key, NBTList.class);
        if (!type.isAssignableFrom(list.getTagsType().getNBTClass())) {
            throw new NbtCodecException(MessageFormat.format("NBTList {0} tags type has unexpected type, expected {1}, but got {2}", key, type, list.getTagsType().getNBTClass()));
        }
        return list;
    }

    @Nullable
    public <T extends NBT> NBTList<T> getTagListOfTypeOrNull(String key, Class<T> type) {
        NBTList list = this.getTagOfTypeOrNull(key, NBTList.class);
        if (list != null && type.isAssignableFrom(list.getTagsType().getNBTClass())) {
            return list;
        }
        return null;
    }

    public NBTCompound getCompoundTagOrThrow(String key) {
        return this.getTagOfTypeOrThrow(key, NBTCompound.class);
    }

    @Nullable
    public NBTCompound getCompoundTagOrNull(String key) {
        return this.getTagOfTypeOrNull(key, NBTCompound.class);
    }

    public Number getNumberTagValueOrThrow(String key) {
        return this.getNumberTagOrThrow(key).getAsNumber();
    }

    @Nullable
    public Number getNumberTagValueOrNull(String key) {
        return this.getNumberTagValueOrDefault(key, null);
    }

    @Contract(value="_, !null -> !null")
    @Nullable
    public Number getNumberTagValueOrDefault(String key, @Nullable Number number) {
        NBTNumber tag = this.getNumberTagOrNull(key);
        return tag != null ? (Number)tag.getAsNumber() : (Number)number;
    }

    public NBTNumber getNumberTagOrThrow(String key) {
        return this.getTagOfTypeOrThrow(key, NBTNumber.class);
    }

    @Nullable
    public NBTNumber getNumberTagOrNull(String key) {
        return this.getTagOfTypeOrNull(key, NBTNumber.class);
    }

    public NBTString getStringTagOrThrow(String key) {
        return this.getTagOfTypeOrThrow(key, NBTString.class);
    }

    @Nullable
    public NBTString getStringTagOrNull(String key) {
        return this.getTagOfTypeOrNull(key, NBTString.class);
    }

    public NBTList<NBTCompound> getCompoundListTagOrThrow(String key) {
        return this.getTagListOfTypeOrThrow(key, NBTCompound.class);
    }

    @Nullable
    public NBTList<NBTCompound> getCompoundListTagOrNull(String key) {
        return this.getTagListOfTypeOrNull(key, NBTCompound.class);
    }

    public NBTList<NBTNumber> getNumberTagListTagOrThrow(String key) {
        return this.getTagListOfTypeOrThrow(key, NBTNumber.class);
    }

    @Nullable
    public NBTList<NBTNumber> getNumberListTagOrNull(String key) {
        return this.getTagListOfTypeOrNull(key, NBTNumber.class);
    }

    public NBTList<NBTString> getStringListTagOrThrow(String key) {
        return this.getTagListOfTypeOrThrow(key, NBTString.class);
    }

    @Nullable
    public NBTList<NBTString> getStringListTagOrNull(String key) {
        return this.getTagListOfTypeOrNull(key, NBTString.class);
    }

    public String getStringTagValueOrThrow(String key) {
        return this.getStringTagOrThrow(key).getValue();
    }

    @Nullable
    public String getStringTagValueOrNull(String key) {
        NBT tag = this.getTagOrNull(key);
        if (tag instanceof NBTString) {
            return ((NBTString)tag).getValue();
        }
        return null;
    }

    public String getStringTagValueOrDefault(String key, String defaultValue) {
        NBT tag = this.getTagOrNull(key);
        if (tag instanceof NBTString) {
            return ((NBTString)tag).getValue();
        }
        return defaultValue;
    }

    public NBT removeTag(String key) {
        return this.tags.remove(key);
    }

    public <T extends NBT> T removeTagAndReturnIfType(String key, Class<T> type) {
        NBT tag = this.removeTag(key);
        if (type.isInstance(tag)) {
            return (T)tag;
        }
        return null;
    }

    public <T extends NBT> NBTList<T> removeTagAndReturnIfListType(String key, Class<T> type) {
        NBTList list = this.removeTagAndReturnIfType(key, NBTList.class);
        if (list != null && type.isAssignableFrom(list.getTagsType().getNBTClass())) {
            return list;
        }
        return null;
    }

    public void setTag(String key, NBT tag) {
        if (tag != null) {
            this.tags.put(key, tag);
        } else {
            this.tags.remove(key);
        }
    }

    @Override
    public NBTCompound copy() {
        NBTCompound clone = new NBTCompound();
        for (Map.Entry<String, NBT> entry : this.tags.entrySet()) {
            clone.setTag(entry.getKey(), entry.getValue().copy());
        }
        return clone;
    }

    public boolean getBoolean(String string) {
        return this.getBooleanOr(string, false);
    }

    public boolean getBooleanOr(String string, boolean defaultValue) {
        NBTNumber nbtByte = this.getTagOfTypeOrNull(string, NBTNumber.class);
        return nbtByte != null ? nbtByte.getAsByte() != 0 : defaultValue;
    }

    public boolean getBooleanOrThrow(String string) {
        return this.getTagOfTypeOrThrow(string, NBTNumber.class).getAsByte() != 0;
    }

    @Contract(value="_, _, !null, _ -> !null")
    @Nullable
    public <T> T getOr(String key, NbtDecoder<T> decoder, @Nullable T def, PacketWrapper<?> wrapper) {
        NBT tag = this.getTagOrNull(key);
        return tag != null ? decoder.decode(tag, wrapper) : def;
    }

    @Contract(value="_, _, !null, _ -> !null")
    @Nullable
    public <T> T getOrSupply(String key, NbtDecoder<T> decoder, Supplier<@Nullable T> def, PacketWrapper<?> wrapper) {
        NBT tag = this.getTagOrNull(key);
        return tag != null ? decoder.decode(tag, wrapper) : def.get();
    }

    @Nullable
    public <T> T getOrNull(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return this.getOr(key, decoder, null, wrapper);
    }

    public <T> T getOrThrow(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return decoder.decode(this.getTagOrThrow(key), wrapper);
    }

    @Contract(value="_, _, !null, _ -> !null")
    @Nullable
    public <T> List<T> getListOr(String key, NbtDecoder<T> decoder, @Nullable List<T> def, PacketWrapper<?> wrapper) {
        NBT tag = this.getTagOrNull(key);
        if (tag instanceof NBTList) {
            List tags = ((NBTList)tag).getTags();
            ArrayList<T> list = new ArrayList<T>(tags.size());
            for (NBT element : tags) {
                list.add(decoder.decode(element, wrapper));
            }
            return list;
        }
        if (tag != null) {
            ArrayList<T> list = new ArrayList<T>(1);
            list.add(decoder.decode(tag, wrapper));
            return list;
        }
        return def;
    }

    @Nullable
    public <T> List<T> getListOrNull(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return this.getListOr(key, decoder, null, wrapper);
    }

    public <T> List<T> getListOrEmpty(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        return this.getListOr(key, decoder, Collections.emptyList(), wrapper);
    }

    public <T> List<T> getListOrThrow(String key, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
        List<T> list = this.getListOrNull(key, decoder, wrapper);
        if (list == null) {
            throw new IllegalStateException(MessageFormat.format("NBT {0} does not exist", key));
        }
        return list;
    }

    public <T> void set(String key, T value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
        this.setTag(key, encoder.encode(wrapper, value));
    }

    public <T> void setList(String key, List<T> value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
        if (value.isEmpty()) {
            this.setTag(key, new NBTList<NBTEnd>(NBTType.END, 0));
        } else {
            NBT firstVal = encoder.encode(wrapper, value.get(0));
            int size = value.size();
            NBTList list = new NBTList(firstVal.getType(), size);
            list.addTagUnsafe(firstVal);
            for (int i = 1; i < size; ++i) {
                list.addTagUnsafe(encoder.encode(wrapper, value.get(i)));
            }
            this.setTag(key, list);
        }
    }

    public <T> void setCompactList(String key, List<T> value, NbtEncoder<T> encoder, PacketWrapper<?> wrapper) {
        if (value.size() == 1) {
            this.set(key, value.get(0), encoder, wrapper);
        } else {
            this.setList(key, value, encoder, wrapper);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NBTCompound) {
            if (this.isEmpty() && ((NBTCompound)other).isEmpty()) {
                return true;
            }
            return this.tags.equals(((NBTCompound)other).tags);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.tags.hashCode();
    }

    @Override
    public String toString() {
        return "Compound{" + this.tags + "}";
    }
}

