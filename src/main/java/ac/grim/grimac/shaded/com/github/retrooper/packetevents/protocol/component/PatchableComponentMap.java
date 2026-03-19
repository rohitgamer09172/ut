/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.IComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PatchableComponentMap
implements IComponentMap {
    public static final PatchableComponentMap EMPTY = new PatchableComponentMap(Collections.emptyMap(), Collections.emptyMap());
    private final Map<ComponentType<?>, ?> base;
    private final Map<ComponentType<?>, Optional<?>> patches;

    public PatchableComponentMap(StaticComponentMap base) {
        this(base.getDelegate(), new HashMap());
    }

    public PatchableComponentMap(Map<ComponentType<?>, ?> base) {
        this(base, new HashMap());
    }

    public PatchableComponentMap(StaticComponentMap base, Map<ComponentType<?>, Optional<?>> patches) {
        this(base.getDelegate(), patches);
    }

    public PatchableComponentMap(Map<ComponentType<?>, ?> base, Map<ComponentType<?>, Optional<?>> patches) {
        this.base = Collections.unmodifiableMap(new HashMap(base));
        this.patches = patches;
    }

    @Override
    @Nullable
    public <T> T get(ComponentType<T> type) {
        Optional<?> patched = this.patches.get(type);
        if (patched != null) {
            return patched.orElse(null);
        }
        return (T)this.base.get(type);
    }

    @Override
    public <T> void set(ComponentType<T> type, Optional<T> value) {
        Object newVal;
        Object baseVal = this.base.get(type);
        if (Objects.equals(baseVal, newVal = value.orElse(null))) {
            this.patches.remove(type);
        } else {
            this.patches.put(type, value);
        }
    }

    @Override
    public boolean has(ComponentType<?> type) {
        Optional<?> patched = this.patches.get(type);
        return patched != null ? patched.isPresent() : this.base.containsKey(type);
    }

    public PatchableComponentMap copy() {
        return new PatchableComponentMap(this.base, new HashMap(this.patches));
    }

    public Map<ComponentType<?>, ?> getBase() {
        return this.base;
    }

    public Map<ComponentType<?>, Optional<?>> getPatches() {
        return this.patches;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PatchableComponentMap)) {
            return false;
        }
        PatchableComponentMap that = (PatchableComponentMap)obj;
        if (!this.base.equals(that.base)) {
            return false;
        }
        return this.patches.equals(that.patches);
    }

    public int hashCode() {
        return Objects.hash(this.base, this.patches);
    }

    public String toString() {
        return "PatchableComponentMap{base=" + this.base + ", patches=" + this.patches + '}';
    }
}

