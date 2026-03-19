/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class BannerLayers {
    private List<Layer> layers;

    public BannerLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public static BannerLayers read(PacketWrapper<?> wrapper) {
        List<Layer> layers = wrapper.readList(Layer::read);
        return new BannerLayers(layers);
    }

    public static void write(PacketWrapper<?> wrapper, BannerLayers patterns) {
        wrapper.writeList(patterns.layers, Layer::write);
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public List<Layer> getLayers() {
        return this.layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BannerLayers)) {
            return false;
        }
        BannerLayers that = (BannerLayers)obj;
        return this.layers.equals(that.layers);
    }

    public int hashCode() {
        return Objects.hashCode(this.layers);
    }

    public static class Layer {
        private BannerPattern pattern;
        private DyeColor color;

        public Layer(BannerPattern pattern, DyeColor color) {
            this.pattern = pattern;
            this.color = color;
        }

        public static Layer read(PacketWrapper<?> wrapper) {
            BannerPattern pattern = wrapper.readMappedEntityOrDirect(BannerPatterns.getRegistry(), BannerPattern::readDirect);
            DyeColor color = DyeColor.read(wrapper);
            return new Layer(pattern, color);
        }

        public static void write(PacketWrapper<?> wrapper, Layer layer) {
            wrapper.writeMappedEntityOrDirect(layer.pattern, BannerPattern::writeDirect);
            DyeColor.write(wrapper, layer.color);
        }

        public BannerPattern getPattern() {
            return this.pattern;
        }

        public void setPattern(BannerPattern pattern) {
            this.pattern = pattern;
        }

        public DyeColor getColor() {
            return this.color;
        }

        public void setColor(DyeColor color) {
            this.color = color;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Layer)) {
                return false;
            }
            Layer layer = (Layer)obj;
            if (!this.pattern.equals(layer.pattern)) {
                return false;
            }
            return this.color == layer.color;
        }

        public int hashCode() {
            return Objects.hash(this.pattern, this.color);
        }
    }
}

