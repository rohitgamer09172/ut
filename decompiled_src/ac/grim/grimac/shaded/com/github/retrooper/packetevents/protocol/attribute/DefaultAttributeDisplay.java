/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeDisplayType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.AttributeDisplayTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DefaultAttributeDisplay
implements AttributeDisplay {
    public static final DefaultAttributeDisplay INSTANCE = new DefaultAttributeDisplay();

    private DefaultAttributeDisplay() {
    }

    public static DefaultAttributeDisplay read(PacketWrapper<?> wrapper) {
        return INSTANCE;
    }

    public static void write(PacketWrapper<?> wrapper, DefaultAttributeDisplay display) {
    }

    @Override
    public AttributeDisplayType<?> getType() {
        return AttributeDisplayTypes.DEFAULT;
    }
}

