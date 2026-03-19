/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticInstrument
extends AbstractMappedEntity
implements Instrument {
    private final Sound sound;
    private final float useSeconds;
    private final float range;
    private final Component description;

    @Deprecated
    public StaticInstrument(Sound sound, int useDuration, float range) {
        this(sound, (float)useDuration * 20.0f, range, Component.empty());
    }

    public StaticInstrument(Sound sound, float useSeconds, float range, Component description) {
        this(null, sound, useSeconds, range, description);
    }

    @ApiStatus.Internal
    public StaticInstrument(@Nullable TypesBuilderData data, Sound sound, float useSeconds, float range, Component description) {
        super(data);
        this.sound = sound;
        this.useSeconds = useSeconds;
        this.range = range;
        this.description = description;
    }

    @Override
    public Instrument copy(@Nullable TypesBuilderData newData) {
        return new StaticInstrument(newData, this.sound, this.useSeconds, this.range, this.description);
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public float getUseSeconds() {
        return this.useSeconds;
    }

    @Override
    public float getRange() {
        return this.range;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticInstrument)) {
            return false;
        }
        StaticInstrument that = (StaticInstrument)obj;
        if (this.useSeconds != that.useSeconds) {
            return false;
        }
        if (Float.compare(that.range, this.range) != 0) {
            return false;
        }
        if (!this.sound.equals(that.sound)) {
            return false;
        }
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.sound, Float.valueOf(this.useSeconds), Float.valueOf(this.range), this.description);
    }
}

