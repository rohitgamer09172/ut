/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow.CowVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow.CowVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class CowVariantComponent {
    private CowVariant variant;

    public CowVariantComponent(CowVariant variant) {
        this.variant = variant;
    }

    public static CowVariantComponent read(PacketWrapper<?> wrapper) {
        CowVariant type = wrapper.readMappedEntity(CowVariants.getRegistry());
        return new CowVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, CowVariantComponent component) {
        wrapper.writeMappedEntity(component.variant);
    }

    public CowVariant getVariant() {
        return this.variant;
    }

    public void setVariant(CowVariant variant) {
        this.variant = variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CowVariantComponent)) {
            return false;
        }
        CowVariantComponent that = (CowVariantComponent)obj;
        return this.variant.equals(that.variant);
    }

    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}

