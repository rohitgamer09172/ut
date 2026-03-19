/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeDisplayType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeDisplayTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AttributeDisplay {
    public static AttributeDisplay read(PacketWrapper<?> wrapper) {
        AttributeDisplayType<?> type = wrapper.readMappedEntity(AttributeDisplayTypes.getRegistry());
        return type.read(wrapper);
    }

    public static void write(PacketWrapper<?> wrapper, AttributeDisplay display) {
        wrapper.writeMappedEntity(display.getType());
        display.getType().write(wrapper, display);
    }

    public AttributeDisplayType<?> getType();
}

