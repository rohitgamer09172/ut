/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;

public interface Attribute
extends MappedEntity {
    @Override
    default public ResourceLocation getName() {
        return this.getName(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    public ResourceLocation getName(ClientVersion var1);

    default public double sanitizeValue(double value) {
        return this.sanitizeValue(value, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    default public double sanitizeValue(double value, ClientVersion version) {
        if (!Double.isNaN(value)) {
            return MathUtil.clamp(value, this.getMinValue(), this.getMaxValue());
        }
        return this.getMinValue();
    }

    public double getDefaultValue();

    public double getMinValue();

    public double getMaxValue();
}

