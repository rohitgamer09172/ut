/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public final class ComponentValue<T> {
    private final ComponentType<T> type;
    private final T value;

    public ComponentValue(ComponentType<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public static ComponentValue<?> read(PacketWrapper<?> wrapper) {
        ComponentType type = wrapper.readMappedEntity(ComponentTypes::getById);
        return ComponentValue.read0(wrapper, type);
    }

    private static <T> ComponentValue<T> read0(PacketWrapper<?> wrapper, ComponentType<T> type) {
        return new ComponentValue<T>(type, type.read(wrapper));
    }

    public static <T> void write(PacketWrapper<?> wrapper, ComponentValue<T> value) {
        wrapper.writeMappedEntity(value.type);
        value.type.write(wrapper, value.value);
    }

    public ComponentType<T> getType() {
        return this.type;
    }

    public T getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ComponentValue)) {
            return false;
        }
        ComponentValue that = (ComponentValue)obj;
        if (!this.type.equals(that.type)) {
            return false;
        }
        return this.value.equals(that.value);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.value);
    }

    public String toString() {
        return "ComponentValue{type=" + this.type + ", value=" + this.value + '}';
    }
}

