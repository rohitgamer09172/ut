/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticAttribute
extends AbstractMappedEntity
implements Attribute {
    @Nullable
    private final ResourceLocation legacyName;
    private final double defaultValue;
    private final double minValue;
    private final double maxValue;

    @ApiStatus.Internal
    public StaticAttribute(@Nullable TypesBuilderData data, String legacyPrefix, double defaultValue, double minValue, double maxValue) {
        super(data);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.legacyName = legacyPrefix == null || data == null ? null : new ResourceLocation(data.getName().getNamespace(), legacyPrefix + "." + data.getName().getKey());
    }

    @Override
    public ResourceLocation getName(ClientVersion version) {
        if (this.data == null) {
            throw new UnsupportedOperationException();
        }
        return version.isNewerThanOrEquals(ClientVersion.V_1_21_2) || this.legacyName == null ? this.data.getName() : this.legacyName;
    }

    @Override
    public double getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public double getMinValue() {
        return this.minValue;
    }

    @Override
    public double getMaxValue() {
        return this.maxValue;
    }
}

