/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemLock {
    private static final String FALLBACK_LOCK_STRING = "packetevents$invalid_lock";
    @ApiStatus.Obsolete
    private String code;
    private NBTCompound predicate;

    @ApiStatus.Obsolete
    public ItemLock(String code) {
        this.code = code;
        this.predicate = new NBTCompound();
    }

    public ItemLock(NBTCompound predicate) {
        this.code = FALLBACK_LOCK_STRING;
        this.predicate = predicate;
    }

    public static ItemLock read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            return new ItemLock(wrapper.readNBT());
        }
        NBTString codeTag = (NBTString)wrapper.readNBTRaw();
        return new ItemLock(codeTag.getValue());
    }

    public static void write(PacketWrapper<?> wrapper, ItemLock lock) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeNBT(lock.predicate);
        } else {
            wrapper.writeNBTRaw(new NBTString(lock.code));
        }
    }

    @ApiStatus.Obsolete
    public String getCode() {
        return this.code;
    }

    @ApiStatus.Obsolete
    public void setCode(String code) {
        this.code = code;
    }

    public NBTCompound getPredicate() {
        return this.predicate;
    }

    public void setPredicate(NBTCompound predicate) {
        this.predicate = predicate;
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ItemLock itemLock = (ItemLock)obj;
        if (!this.code.equals(itemLock.code)) {
            return false;
        }
        return this.predicate.equals(itemLock.predicate);
    }

    public int hashCode() {
        return Objects.hash(this.code, this.predicate);
    }
}

