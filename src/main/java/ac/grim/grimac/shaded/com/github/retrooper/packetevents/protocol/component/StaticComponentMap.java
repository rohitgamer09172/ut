/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.IComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class StaticComponentMap
implements IComponentMap {
    public static final StaticComponentMap EMPTY = new StaticComponentMap(Collections.emptyMap());
    @ApiStatus.Obsolete
    public static final StaticComponentMap SHARED_ITEM_COMPONENTS = StaticComponentMap.builder().set(ComponentTypes.MAX_STACK_SIZE, 64).set(ComponentTypes.LORE, ItemLore.EMPTY).set(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY).set(ComponentTypes.REPAIR_COST, 0).set(ComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).set(ComponentTypes.RARITY, ItemRarity.COMMON).build();
    private final boolean empty;
    private final Map<ComponentType<?>, ?> delegate;

    public StaticComponentMap(Map<ComponentType<?>, ?> delegate) {
        this.empty = delegate.isEmpty();
        this.delegate = this.empty ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap(delegate));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean has(ComponentType<?> type) {
        return !this.empty && this.delegate.containsKey(type);
    }

    @Override
    @Nullable
    public <T> T get(ComponentType<T> type) {
        return this.empty ? null : (T)this.delegate.get(type);
    }

    @Override
    public <T> void set(ComponentType<T> type, Optional<T> value) {
        throw new UnsupportedOperationException();
    }

    public StaticComponentMap merge(StaticComponentMap prioritizedMap) {
        return StaticComponentMap.builder().setAll(this).setAll(prioritizedMap).build();
    }

    public Map<ComponentType<?>, ?> getDelegate() {
        return this.delegate;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticComponentMap)) {
            return false;
        }
        StaticComponentMap that = (StaticComponentMap)obj;
        return this.delegate.equals(that.delegate);
    }

    public int hashCode() {
        return Objects.hashCode(this.delegate);
    }

    public String toString() {
        return "Components" + this.delegate;
    }

    public static class Builder {
        private final Map<ComponentType<?>, Object> map = new HashMap();

        public StaticComponentMap build() {
            return new StaticComponentMap(this.map);
        }

        public Builder setAll(Builder map) {
            return this.setAll(map.map);
        }

        public Builder setAll(StaticComponentMap map) {
            return this.setAll(map.getDelegate());
        }

        public Builder setAll(Map<ComponentType<?>, ?> map) {
            for (Map.Entry<ComponentType<?>, ?> entry : map.entrySet()) {
                this.set(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public <T> Builder set(ComponentType<T> type, Optional<T> value) {
            return this.set(type, (T)value.orElse(null));
        }

        public <T> Builder set(ComponentType<T> type, @Nullable T value) {
            if (value == null) {
                this.map.remove(type);
            } else {
                this.map.put(type, value);
            }
            return this;
        }
    }
}

