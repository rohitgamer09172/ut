/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentPredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.TypedComponentPredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComponentMatchers {
    private ComponentPredicate components;
    private List<TypedComponentPredicate<?>> predicates;

    public ComponentMatchers() {
        this(new ComponentPredicate(), new ArrayList());
    }

    public ComponentMatchers(ComponentPredicate components, List<TypedComponentPredicate<?>> predicates) {
        this.components = components;
        this.predicates = predicates;
    }

    public static ComponentMatchers read(PacketWrapper<?> wrapper) {
        ComponentPredicate components = ComponentPredicate.read(wrapper);
        List<TypedComponentPredicate<?>> predicates = wrapper.readList(TypedComponentPredicate::read);
        return new ComponentMatchers(components, predicates);
    }

    public static void write(PacketWrapper<?> wrapper, ComponentMatchers matchers) {
        ComponentPredicate.write(wrapper, matchers.components);
        wrapper.writeList(matchers.predicates, TypedComponentPredicate::write);
    }

    public ComponentPredicate getComponents() {
        return this.components;
    }

    public void setComponents(ComponentPredicate components) {
        this.components = components;
    }

    public List<TypedComponentPredicate<?>> getPredicates() {
        return this.predicates;
    }

    public void setPredicates(List<TypedComponentPredicate<?>> predicates) {
        this.predicates = predicates;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ComponentMatchers)) {
            return false;
        }
        ComponentMatchers that = (ComponentMatchers)obj;
        if (!this.components.equals(that.components)) {
            return false;
        }
        return this.predicates.equals(that.predicates);
    }

    public int hashCode() {
        return Objects.hash(this.components, this.predicates);
    }
}

