/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.IComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ComponentPredicate
implements Predicate<IComponentMap> {
    private List<ComponentValue<?>> requiredComponents;

    public ComponentPredicate() {
        this(new ArrayList());
    }

    public ComponentPredicate(List<ComponentValue<?>> requiredComponents) {
        this.requiredComponents = requiredComponents;
    }

    public static ComponentPredicate read(PacketWrapper<?> wrapper) {
        List<ComponentValue<?>> components = wrapper.readList(ComponentValue::read);
        return new ComponentPredicate(components);
    }

    public static void write(PacketWrapper<?> wrapper, ComponentPredicate predicate) {
        wrapper.writeList(predicate.requiredComponents, ComponentValue::write);
    }

    public static ComponentPredicate emptyPredicate() {
        return new ComponentPredicate(new ArrayList());
    }

    public static ComponentPredicate fromPatches(PatchableComponentMap components) {
        Map<ComponentType<?>, Optional<?>> patches = components.getPatches();
        ArrayList values = new ArrayList(patches.size());
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : patches.entrySet()) {
            if (!patch.getValue().isPresent()) continue;
            values.add(new ComponentValue(patch.getKey(), patch.getValue().get()));
        }
        return new ComponentPredicate(values);
    }

    public PatchableComponentMap asPatches(StaticComponentMap base) {
        PatchableComponentMap patched = new PatchableComponentMap(base);
        for (ComponentValue<?> component : this.requiredComponents) {
            patched.set(component);
        }
        return patched;
    }

    @Override
    public boolean test(IComponentMap components) {
        for (ComponentValue<?> component : this.requiredComponents) {
            Optional<?> value = components.getOptional(component.getType());
            if (value.isPresent() && component.getValue().equals(value.get())) continue;
            return false;
        }
        return true;
    }

    public List<ComponentValue<?>> getRequiredComponents() {
        return this.requiredComponents;
    }

    public void setRequiredComponents(List<ComponentValue<?>> requiredComponents) {
        this.requiredComponents = requiredComponents;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ComponentPredicate)) {
            return false;
        }
        ComponentPredicate that = (ComponentPredicate)obj;
        return this.requiredComponents.equals(that.requiredComponents);
    }

    public int hashCode() {
        return Objects.hashCode(this.requiredComponents);
    }

    public String toString() {
        return "ComponentPredicate{requiredComponents=" + this.requiredComponents + '}';
    }
}

