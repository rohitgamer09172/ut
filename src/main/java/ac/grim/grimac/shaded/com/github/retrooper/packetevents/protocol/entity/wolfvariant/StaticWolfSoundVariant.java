/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticWolfSoundVariant
extends AbstractMappedEntity
implements WolfSoundVariant {
    private final Sound ambientSound;
    private final Sound deathSound;
    private final Sound growlSound;
    private final Sound hurtSound;
    private final Sound pantSound;
    private final Sound whineSound;

    public StaticWolfSoundVariant(Sound ambientSound, Sound deathSound, Sound growlSound, Sound hurtSound, Sound pantSound, Sound whineSound) {
        this(null, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound);
    }

    @ApiStatus.Internal
    public StaticWolfSoundVariant(@Nullable TypesBuilderData data, Sound ambientSound, Sound deathSound, Sound growlSound, Sound hurtSound, Sound pantSound, Sound whineSound) {
        super(data);
        this.ambientSound = ambientSound;
        this.deathSound = deathSound;
        this.growlSound = growlSound;
        this.hurtSound = hurtSound;
        this.pantSound = pantSound;
        this.whineSound = whineSound;
    }

    @Override
    public WolfSoundVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticWolfSoundVariant(newData, this.ambientSound, this.deathSound, this.growlSound, this.hurtSound, this.pantSound, this.whineSound);
    }

    @Override
    public Sound getAmbientSound() {
        return this.ambientSound;
    }

    @Override
    public Sound getDeathSound() {
        return this.deathSound;
    }

    @Override
    public Sound getGrowlSound() {
        return this.growlSound;
    }

    @Override
    public Sound getHurtSound() {
        return this.hurtSound;
    }

    @Override
    public Sound getPantSound() {
        return this.pantSound;
    }

    @Override
    public Sound getWhineSound() {
        return this.whineSound;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticWolfSoundVariant)) {
            return false;
        }
        StaticWolfSoundVariant that = (StaticWolfSoundVariant)obj;
        if (!this.ambientSound.equals(that.ambientSound)) {
            return false;
        }
        if (!this.deathSound.equals(that.deathSound)) {
            return false;
        }
        if (!this.growlSound.equals(that.growlSound)) {
            return false;
        }
        if (!this.hurtSound.equals(that.hurtSound)) {
            return false;
        }
        if (!this.pantSound.equals(that.pantSound)) {
            return false;
        }
        return this.whineSound.equals(that.whineSound);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.ambientSound, this.deathSound, this.growlSound, this.hurtSound, this.pantSound, this.whineSound);
    }
}

