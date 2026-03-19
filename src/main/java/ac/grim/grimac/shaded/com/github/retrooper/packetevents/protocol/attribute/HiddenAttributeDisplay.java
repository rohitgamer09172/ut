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
public class HiddenAttributeDisplay
implements AttributeDisplay {
    public static final HiddenAttributeDisplay INSTANCE = new HiddenAttributeDisplay();

    private HiddenAttributeDisplay() {
    }

    public static HiddenAttributeDisplay read(PacketWrapper<?> wrapper) {
        return INSTANCE;
    }

    public static void write(PacketWrapper<?> wrapper, HiddenAttributeDisplay display) {
    }

    @Override
    public AttributeDisplayType<?> getType() {
        return AttributeDisplayTypes.HIDDEN;
    }
}

