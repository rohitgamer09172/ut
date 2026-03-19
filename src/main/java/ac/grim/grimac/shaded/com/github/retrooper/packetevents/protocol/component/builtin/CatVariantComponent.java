/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class CatVariantComponent {
    private CatVariant variant;

    public CatVariantComponent(CatVariant variant) {
        this.variant = variant;
    }

    public static CatVariantComponent read(PacketWrapper<?> wrapper) {
        CatVariant type = wrapper.readMappedEntity(CatVariants.getRegistry());
        return new CatVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, CatVariantComponent component) {
        wrapper.writeMappedEntity(component.variant);
    }

    public CatVariant getVariant() {
        return this.variant;
    }

    public void setVariant(CatVariant variant) {
        this.variant = variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CatVariantComponent)) {
            return false;
        }
        CatVariantComponent that = (CatVariantComponent)obj;
        return this.variant.equals(that.variant);
    }

    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}

