/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.IJukeboxSong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSongs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

public class ItemJukeboxPlayable {
    private MaybeMappedEntity<IJukeboxSong> song;
    @ApiStatus.Obsolete
    private boolean showInTooltip;

    public ItemJukeboxPlayable(MaybeMappedEntity<IJukeboxSong> song) {
        this(song, true);
    }

    @ApiStatus.Obsolete
    public ItemJukeboxPlayable(@Nullable JukeboxSong song, @Nullable ResourceLocation songKey, boolean showInTooltip) {
        this((IJukeboxSong)song, songKey, showInTooltip);
    }

    @Deprecated
    public ItemJukeboxPlayable(@Nullable IJukeboxSong song, @Nullable ResourceLocation songKey, boolean showInTooltip) {
        this(new MaybeMappedEntity<IJukeboxSong>(song, songKey), showInTooltip);
    }

    @ApiStatus.Obsolete
    public ItemJukeboxPlayable(MaybeMappedEntity<IJukeboxSong> song, boolean showInTooltip) {
        this.song = song;
        this.showInTooltip = showInTooltip;
    }

    public static ItemJukeboxPlayable read(PacketWrapper<?> wrapper) {
        MaybeMappedEntity<IJukeboxSong> song = MaybeMappedEntity.read(wrapper, JukeboxSongs.getRegistry(), IJukeboxSong::read);
        boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
        return new ItemJukeboxPlayable(song, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemJukeboxPlayable jukeboxPlayable) {
        MaybeMappedEntity.write(wrapper, jukeboxPlayable.song, IJukeboxSong::write);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeBoolean(jukeboxPlayable.showInTooltip);
        }
    }

    public MaybeMappedEntity<IJukeboxSong> getSongHolder() {
        return this.song;
    }

    public void setSongHolder(MaybeMappedEntity<IJukeboxSong> songHolder) {
        this.song = songHolder;
    }

    @Nullable
    public IJukeboxSong getJukeboxSong() {
        return this.song.getValue();
    }

    public void setJukeboxSong(@Nullable IJukeboxSong song) {
        this.song = new MaybeMappedEntity<IJukeboxSong>(song);
    }

    public void setJukeboxSong(@Nullable JukeboxSong song) {
        this.setJukeboxSong((IJukeboxSong)song);
    }

    @Deprecated
    @Nullable
    public JukeboxSong getSong() {
        IJukeboxSong song = this.getJukeboxSong();
        if (song == null) {
            return null;
        }
        if (song instanceof JukeboxSong) {
            return (JukeboxSong)song;
        }
        return (JukeboxSong)song.copy(null);
    }

    @Deprecated
    public void setSong(JukeboxSong song) {
        this.setJukeboxSong((IJukeboxSong)song);
    }

    @Nullable
    public ResourceLocation getSongKey() {
        return this.song.getName();
    }

    public void setSongKey(ResourceLocation songKey) {
        this.song = new MaybeMappedEntity(songKey);
    }

    @ApiStatus.Obsolete
    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    @ApiStatus.Obsolete
    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemJukeboxPlayable)) {
            return false;
        }
        ItemJukeboxPlayable that = (ItemJukeboxPlayable)obj;
        if (this.showInTooltip != that.showInTooltip) {
            return false;
        }
        if (!Objects.equals(this.song, that.song)) {
            return false;
        }
        return Objects.equals(this.song, that.song);
    }

    public int hashCode() {
        return Objects.hash(this.song, this.song, this.showInTooltip);
    }
}

