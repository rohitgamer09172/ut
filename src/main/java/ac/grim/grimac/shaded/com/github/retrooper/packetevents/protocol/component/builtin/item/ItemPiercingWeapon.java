/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemPiercingWeapon {
    private boolean dealsKnockback;
    private boolean dismounts;
    private @Nullable Sound sound;
    private @Nullable Sound hitSound;

    public ItemPiercingWeapon(boolean dealsKnockback, boolean dismounts, @Nullable Sound sound, @Nullable Sound hitSound) {
        this.dealsKnockback = dealsKnockback;
        this.dismounts = dismounts;
        this.sound = sound;
        this.hitSound = hitSound;
    }

    public static ItemPiercingWeapon read(PacketWrapper<?> wrapper) {
        boolean dealsKnockback = wrapper.readBoolean();
        boolean dismounts = wrapper.readBoolean();
        Sound sound = (Sound)wrapper.readOptional(Sound::read);
        Sound hitSound = (Sound)wrapper.readOptional(Sound::read);
        return new ItemPiercingWeapon(dealsKnockback, dismounts, sound, hitSound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemPiercingWeapon component) {
        wrapper.writeBoolean(component.dealsKnockback);
        wrapper.writeBoolean(component.dismounts);
        wrapper.writeOptional(component.sound, Sound::write);
        wrapper.writeOptional(component.hitSound, Sound::write);
    }

    public boolean isDealsKnockback() {
        return this.dealsKnockback;
    }

    public void setDealsKnockback(boolean dealsKnockback) {
        this.dealsKnockback = dealsKnockback;
    }

    public boolean isDismounts() {
        return this.dismounts;
    }

    public void setDismounts(boolean dismounts) {
        this.dismounts = dismounts;
    }

    public @Nullable Sound getSound() {
        return this.sound;
    }

    public void setSound(@Nullable Sound sound) {
        this.sound = sound;
    }

    public @Nullable Sound getHitSound() {
        return this.hitSound;
    }

    public void setHitSound(@Nullable Sound hitSound) {
        this.hitSound = hitSound;
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ItemPiercingWeapon that = (ItemPiercingWeapon)obj;
        if (this.dealsKnockback != that.dealsKnockback) {
            return false;
        }
        if (this.dismounts != that.dismounts) {
            return false;
        }
        if (!Objects.equals(this.sound, that.sound)) {
            return false;
        }
        return Objects.equals(this.hitSound, that.hitSound);
    }

    public int hashCode() {
        return Objects.hash(this.dealsKnockback, this.dismounts, this.sound, this.hitSound);
    }
}

