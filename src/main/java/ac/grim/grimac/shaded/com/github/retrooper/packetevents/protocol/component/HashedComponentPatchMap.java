/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class HashedComponentPatchMap {
    private final Map<ComponentType<?>, Integer> addedComponents;
    private final Set<ComponentType<?>> removedComponents;

    public HashedComponentPatchMap(Map<ComponentType<?>, Integer> addedComponents, Set<ComponentType<?>> removedComponents) {
        this.addedComponents = addedComponents;
        this.removedComponents = removedComponents;
    }

    public static HashedComponentPatchMap read(PacketWrapper<?> wrapper) {
        Map<ComponentType<?>, Integer> addedComponents = wrapper.readMap(ew -> ew.readMappedEntity(ComponentTypes.getRegistry()), PacketWrapper::readInt);
        Set removedComponents = wrapper.readCollection(HashSet::new, ew -> ew.readMappedEntity(ComponentTypes.getRegistry()));
        return new HashedComponentPatchMap(addedComponents, removedComponents);
    }

    public static void write(PacketWrapper<?> wrapper, HashedComponentPatchMap map) {
        wrapper.writeMap(map.addedComponents, PacketWrapper::writeMappedEntity, PacketWrapper::writeInt);
        wrapper.writeCollection(map.removedComponents, PacketWrapper::writeMappedEntity);
    }

    public Map<ComponentType<?>, Integer> getAddedComponents() {
        return this.addedComponents;
    }

    public Set<ComponentType<?>> getRemovedComponents() {
        return this.removedComponents;
    }
}

