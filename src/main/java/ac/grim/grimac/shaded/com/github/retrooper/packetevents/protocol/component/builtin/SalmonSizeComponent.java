/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.salmon.SalmonVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.salmon.SalmonVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class SalmonSizeComponent {
    private SalmonVariant variant;

    public SalmonSizeComponent(SalmonVariant variant) {
        this.variant = variant;
    }

    public static SalmonSizeComponent read(PacketWrapper<?> wrapper) {
        SalmonVariant type = wrapper.readMappedEntity(SalmonVariants.getRegistry());
        return new SalmonSizeComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, SalmonSizeComponent component) {
        wrapper.writeMappedEntity(component.variant);
    }

    public SalmonVariant getVariant() {
        return this.variant;
    }

    public void setVariant(SalmonVariant variant) {
        this.variant = variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SalmonSizeComponent)) {
            return false;
        }
        SalmonSizeComponent that = (SalmonSizeComponent)obj;
        return this.variant.equals(that.variant);
    }

    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}

