/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NBTList<T extends NBT>
extends NBT {
    protected final NBTType<T> type;
    protected final List<T> tags;

    public NBTList(NBTType<T> type) {
        this.type = type;
        this.tags = new ArrayList<T>();
    }

    public NBTList(NBTType<T> type, int size) {
        this.type = type;
        this.tags = new ArrayList<T>(size);
    }

    public NBTList(NBTType<T> type, List<T> tags) {
        this.type = type;
        this.tags = new ArrayList<T>();
        this.tags.addAll(tags);
    }

    public static NBTList<NBTCompound> createCompoundList() {
        return new NBTList<NBTCompound>(NBTType.COMPOUND);
    }

    public static NBTList<NBTString> createStringList() {
        return new NBTList<NBTString>(NBTType.STRING);
    }

    public static NBTType<?> getCommonTagType(List<? extends NBT> tags) {
        NBTType<NBTEnd> type = NBTType.END;
        for (NBT nBT : tags) {
            if (type == NBTType.END) {
                type = nBT.getType();
                continue;
            }
            if (type == nBT.getType()) continue;
            return NBTType.COMPOUND;
        }
        return type;
    }

    public NBTType<NBTList> getType() {
        return NBTType.LIST;
    }

    public NBTType<T> getTagsType() {
        return this.type;
    }

    public boolean isEmpty() {
        return this.tags.isEmpty();
    }

    public int size() {
        return this.tags.size();
    }

    public List<T> getTags() {
        return Collections.unmodifiableList(this.tags);
    }

    public T getTag(int index) {
        return (T)((NBT)this.tags.get(index));
    }

    public void setTag(int index, T tag) {
        this.validateAddTag(tag);
        this.tags.set(index, tag);
    }

    public void addTag(int index, T tag) {
        this.validateAddTag(tag);
        this.tags.add(index, tag);
    }

    public void addTag(T tag) {
        this.validateAddTag(tag);
        this.tags.add(tag);
    }

    public void addTagUnsafe(int index, NBT nbt) {
        this.addTag(index, nbt);
    }

    public void addTagUnsafe(NBT nbt) {
        this.addTag(nbt);
    }

    public void removeTag(int index) {
        this.tags.remove(index);
    }

    protected void validateAddTag(T tag) {
        if (this.type != ((NBT)tag).getType()) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid tag type. Expected {0}, got {1}.", this.type.getNBTClass(), tag.getClass()));
        }
    }

    public void addTagOrWrap(NBT tag) {
        if (this.type == tag.getType()) {
            this.tags.add(tag);
        } else if (this.type == NBTType.COMPOUND) {
            NBTCompound wrapped = new NBTCompound();
            wrapped.setTag("", tag);
            this.tags.add(wrapped);
        } else {
            throw new IllegalArgumentException("Can't add or wrap tag " + tag + " to list of type " + this.type);
        }
    }

    private static NBT tryUnwrap(NBTCompound tag) {
        NBT unwrapped;
        if (tag.tags.size() == 1 && (unwrapped = tag.getTagOrNull("")) != null) {
            return unwrapped;
        }
        return tag;
    }

    public List<? extends NBT> unwrapTags() {
        if (this.type != NBTType.COMPOUND) {
            return new ArrayList<T>(this.tags);
        }
        ArrayList<NBT> tags = new ArrayList<NBT>(this.tags.size());
        for (NBT tag : this.tags) {
            if (tag instanceof NBTCompound) {
                tags.add(NBTList.tryUnwrap((NBTCompound)tag));
                continue;
            }
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NBTList other = (NBTList)obj;
        return Objects.equals(this.type, other.type) && Objects.equals(this.tags, other.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.tags);
    }

    @Override
    public NBTList<T> copy() {
        ArrayList<NBT> newTags = new ArrayList<NBT>();
        for (NBT tag : this.tags) {
            newTags.add(tag.copy());
        }
        return new NBTList<T>(this.type, newTags);
    }

    @Override
    public String toString() {
        return "List(" + this.tags + ")";
    }
}

