/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.llama.LlamaVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.llama.LlamaVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class LlamaVariantComponent {
    private LlamaVariant variant;

    public LlamaVariantComponent(LlamaVariant variant) {
        this.variant = variant;
    }

    public static LlamaVariantComponent read(PacketWrapper<?> wrapper) {
        LlamaVariant type = wrapper.readMappedEntity(LlamaVariants.getRegistry());
        return new LlamaVariantComponent(type);
    }

    public static void write(PacketWrapper<?> wrapper, LlamaVariantComponent component) {
        wrapper.writeMappedEntity(component.variant);
    }

    public LlamaVariant getVariant() {
        return this.variant;
    }

    public void setVariant(LlamaVariant variant) {
        this.variant = variant;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LlamaVariantComponent)) {
            return false;
        }
        LlamaVariantComponent that = (LlamaVariantComponent)obj;
        return this.variant.equals(that.variant);
    }

    public int hashCode() {
        return Objects.hashCode(this.variant);
    }
}

