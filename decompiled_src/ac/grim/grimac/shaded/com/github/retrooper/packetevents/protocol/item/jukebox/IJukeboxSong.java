/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSongs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IJukeboxSong
extends MappedEntity,
CopyableEntity<IJukeboxSong>,
DeepComparableEntity {
    public Sound getSound();

    public Component getDescription();

    public float getLengthInSeconds();

    public int getComparatorOutput();

    @Deprecated
    public static IJukeboxSong decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return IJukeboxSong.decode(nbt, PacketWrapper.createDummyWrapper(version), data);
    }

    public static IJukeboxSong decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        Sound sound = compound.getOrThrow("sound_event", Sound.CODEC, wrapper);
        Component description = compound.getOrThrow("description", wrapper.getSerializers(), wrapper);
        float length = compound.getNumberTagOrThrow("length_in_seconds").getAsFloat();
        int comparator_output = compound.getNumberTagOrThrow("comparator_output").getAsInt();
        return new JukeboxSong(data, sound, description, length, comparator_output);
    }

    @Deprecated
    public static NBT encode(IJukeboxSong jukeboxSong, ClientVersion version) {
        return IJukeboxSong.encode(PacketWrapper.createDummyWrapper(version), jukeboxSong);
    }

    public static NBT encode(PacketWrapper<?> wrapper, IJukeboxSong song) {
        NBTCompound compound = new NBTCompound();
        compound.set("sound_event", song.getSound(), Sound.CODEC, wrapper);
        compound.set("description", song.getDescription(), wrapper.getSerializers(), wrapper);
        compound.setTag("length_in_seconds", new NBTFloat(song.getLengthInSeconds()));
        compound.setTag("comparator_output", new NBTInt(song.getComparatorOutput()));
        return compound;
    }

    public static IJukeboxSong read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(JukeboxSongs.getRegistry(), IJukeboxSong::readDirect);
    }

    public static IJukeboxSong readDirect(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        Component description = wrapper.readComponent();
        float lengthInSeconds = wrapper.readFloat();
        int comparatorOutput = wrapper.readVarInt();
        return new JukeboxSong(null, sound, description, lengthInSeconds, comparatorOutput);
    }

    public static void write(PacketWrapper<?> wrapper, IJukeboxSong song) {
        wrapper.writeMappedEntityOrDirect(song, IJukeboxSong::writeDirect);
    }

    public static void writeDirect(PacketWrapper<?> wrapper, IJukeboxSong song) {
        Sound.write(wrapper, song.getSound());
        wrapper.writeComponent(song.getDescription());
        wrapper.writeFloat(song.getLengthInSeconds());
        wrapper.writeVarInt(song.getComparatorOutput());
    }
}

