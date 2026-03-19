/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom.MooshroomVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom.MooshroomVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class MooshroomVariantComponent {
    private MooshroomVariant variant;

    public MooshroomVariantComponent(MooshroomVariant variant) {
        this.variant = variant;
    }

    public static MooshroomVariantComponent read(PacketWrapper<?> wrapper) {
        MooshroomVariant type = wrapper.readMappedEntity(MooshroomVariants.getRegistry());
        return new MooshroomVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, MooshroomVariantComponent component) {
        wrapper.writeMappedEntity(component.variant);
    }

    public MooshroomVariant getVariant() {
        return this.variant;
    }

    public void setVariant(MooshroomVariant variant) {
        this.variant = variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MooshroomVariantComponent)) {
            return false;
        }
        MooshroomVariantComponent that = (MooshroomVariantComponent)obj;
        return this.variant.equals(that.variant);
    }

    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}

