/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.IJukeboxSong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collection;

public final class JukeboxSongs {
    private static final VersionedRegistry<IJukeboxSong> REGISTRY = new VersionedRegistry("jukebox_song");
    public static final IJukeboxSong THIRTEEN = JukeboxSongs.define("13", Sounds.MUSIC_DISC_13, 178.0f, 1);
    public static final IJukeboxSong CAT = JukeboxSongs.define("cat", Sounds.MUSIC_DISC_CAT, 185.0f, 2);
    public static final IJukeboxSong BLOCKS = JukeboxSongs.define("blocks", Sounds.MUSIC_DISC_BLOCKS, 345.0f, 3);
    public static final IJukeboxSong CHIRP = JukeboxSongs.define("chirp", Sounds.MUSIC_DISC_CHIRP, 185.0f, 4);
    public static final IJukeboxSong FAR = JukeboxSongs.define("far", Sounds.MUSIC_DISC_FAR, 174.0f, 5);
    public static final IJukeboxSong MALL = JukeboxSongs.define("mall", Sounds.MUSIC_DISC_MALL, 197.0f, 6);
    public static final IJukeboxSong MELLOHI = JukeboxSongs.define("mellohi", Sounds.MUSIC_DISC_MELLOHI, 96.0f, 7);
    public static final IJukeboxSong STAL = JukeboxSongs.define("stal", Sounds.MUSIC_DISC_STAL, 150.0f, 8);
    public static final IJukeboxSong STRAD = JukeboxSongs.define("strad", Sounds.MUSIC_DISC_STRAD, 188.0f, 9);
    public static final IJukeboxSong WARD = JukeboxSongs.define("ward", Sounds.MUSIC_DISC_WARD, 251.0f, 10);
    public static final IJukeboxSong ELEVEN = JukeboxSongs.define("11", Sounds.MUSIC_DISC_11, 71.0f, 11);
    public static final IJukeboxSong WAIT = JukeboxSongs.define("wait", Sounds.MUSIC_DISC_WAIT, 238.0f, 12);
    public static final IJukeboxSong PIGSTEP = JukeboxSongs.define("pigstep", Sounds.MUSIC_DISC_PIGSTEP, 149.0f, 13);
    public static final IJukeboxSong OTHERSIDE = JukeboxSongs.define("otherside", Sounds.MUSIC_DISC_OTHERSIDE, 195.0f, 14);
    public static final IJukeboxSong FIVE = JukeboxSongs.define("5", Sounds.MUSIC_DISC_5, 178.0f, 15);
    public static final IJukeboxSong RELIC = JukeboxSongs.define("relic", Sounds.MUSIC_DISC_RELIC, 218.0f, 14);
    public static final IJukeboxSong PRECIPICE = JukeboxSongs.define("precipice", Sounds.MUSIC_DISC_PRECIPICE, 299.0f, 13);
    public static final IJukeboxSong CREATOR = JukeboxSongs.define("creator", Sounds.MUSIC_DISC_CREATOR, 176.0f, 12);
    public static final IJukeboxSong CREATOR_MUSIC_BOX = JukeboxSongs.define("creator_music_box", Sounds.MUSIC_DISC_CREATOR_MUSIC_BOX, 73.0f, 11);
    public static final IJukeboxSong TEARS = JukeboxSongs.define("tears", Sounds.MUSIC_DISC_TEARS, 175.0f, 10);
    public static final IJukeboxSong LAVA_CHICKEN = JukeboxSongs.define("lava_chicken", Sounds.MUSIC_DISC_LAVA_CHICKEN, 134.0f, 9);

    private JukeboxSongs() {
    }

    private static String makeDescriptionId(String var0, @Nullable ResourceLocation var1) {
        return var1 == null ? var0 + ".unregistered_sadface" : var0 + "." + var1.getNamespace() + "." + var1.getKey().replace('/', '.');
    }

    @ApiStatus.Internal
    public static IJukeboxSong define(String key, Sound sound, float lengthInSeconds, int comparatorOutput) {
        return REGISTRY.define(key, data -> new JukeboxSong((TypesBuilderData)data, sound, Component.translatable(JukeboxSongs.makeDescriptionId("jukebox_song", data.getName())), lengthInSeconds, comparatorOutput));
    }

    public static VersionedRegistry<IJukeboxSong> getRegistry() {
        return REGISTRY;
    }

    public static IJukeboxSong getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static IJukeboxSong getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static Collection<IJukeboxSong> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

