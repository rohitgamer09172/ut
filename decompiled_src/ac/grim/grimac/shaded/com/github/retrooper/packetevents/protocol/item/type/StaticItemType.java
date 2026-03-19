/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class StaticItemType
extends AbstractMappedEntity
implements ItemType {
    private final int maxAmount;
    private final int maxDurability;
    private final ItemType craftRemainder;
    @Nullable
    private final StateType placedType;
    private final Set<ItemTypes.ItemAttribute> attributes;
    private final Map<ClientVersion, StaticComponentMap> components;

    @ApiStatus.Internal
    public StaticItemType(@Nullable TypesBuilderData data, int maxAmount, int maxDurability, ItemType craftRemainder, @Nullable StateType placedType, Set<ItemTypes.ItemAttribute> attributes) {
        super(data);
        this.maxAmount = maxAmount;
        this.maxDurability = maxDurability;
        this.craftRemainder = craftRemainder;
        this.placedType = placedType;
        this.attributes = attributes;
        this.components = new EnumMap<ClientVersion, StaticComponentMap>(ClientVersion.class);
    }

    @Override
    public int getMaxAmount() {
        return this.maxAmount;
    }

    @Override
    public int getMaxDurability() {
        return this.maxDurability;
    }

    @Override
    public ItemType getCraftRemainder() {
        return this.craftRemainder;
    }

    @Override
    @Nullable
    public StateType getPlacedType() {
        return this.placedType;
    }

    @Override
    public Set<ItemTypes.ItemAttribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public StaticComponentMap getComponents(ClientVersion version) {
        if (!version.isRelease()) {
            throw new IllegalArgumentException("Unsupported version for getting components of " + this.getName() + ": " + (Object)((Object)version));
        }
        return this.components.getOrDefault((Object)version, StaticComponentMap.SHARED_ITEM_COMPONENTS);
    }

    void setComponents(ClientVersion version, StaticComponentMap components) {
        if (this.components.containsKey((Object)version)) {
            throw new IllegalStateException("Components are already defined for " + this.getName() + " in version " + (Object)((Object)version));
        }
        if (!version.isRelease()) {
            throw new IllegalArgumentException("Unsupported version for setting components of " + this.getName() + ": " + (Object)((Object)version));
        }
        this.components.put(version, components);
    }

    boolean hasComponents(ClientVersion version) {
        return this.components.containsKey((Object)version);
    }

    void fillComponents() {
        StaticComponentMap lastComponents = null;
        for (ClientVersion version : ClientVersion.values()) {
            if (!version.isRelease()) continue;
            StaticComponentMap components = this.components.get((Object)version);
            if (components == null) {
                if (lastComponents == null) continue;
                this.components.put(version, lastComponents);
                continue;
            }
            if (lastComponents == null) {
                for (ClientVersion beforeVersion : ClientVersion.values()) {
                    if (beforeVersion == version) break;
                    this.components.put(beforeVersion, components);
                }
            }
            lastComponents = components;
        }
    }
}

