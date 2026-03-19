/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCooldown;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Optional;

public class ItemUseCooldown {
    private float seconds;
    private Optional<ResourceLocation> cooldownGroup;

    public ItemUseCooldown(float seconds, @Nullable ResourceLocation cooldownGroup) {
        this(seconds, Optional.ofNullable(cooldownGroup));
    }

    public ItemUseCooldown(float seconds, Optional<ResourceLocation> cooldownGroup) {
        this.seconds = seconds;
        this.cooldownGroup = cooldownGroup;
    }

    public static ItemUseCooldown read(PacketWrapper<?> wrapper) {
        float seconds = wrapper.readFloat();
        ResourceLocation cooldownGroup = (ResourceLocation)wrapper.readOptional(PacketWrapper::readIdentifier);
        return new ItemUseCooldown(seconds, cooldownGroup);
    }

    public static void write(PacketWrapper<?> wrapper, ItemUseCooldown cooldown) {
        wrapper.writeFloat(cooldown.seconds);
        wrapper.writeOptional(cooldown.cooldownGroup.orElse(null), PacketWrapper::writeIdentifier);
    }

    public WrapperPlayServerSetCooldown buildWrapper(ItemStack fallbackStack) {
        return this.buildWrapper(fallbackStack.getType());
    }

    public WrapperPlayServerSetCooldown buildWrapper(ItemType fallbackItem) {
        int ticks = (int)(this.seconds * 20.0f);
        return this.cooldownGroup.map(resourceLocation -> new WrapperPlayServerSetCooldown((ResourceLocation)resourceLocation, ticks)).orElseGet(() -> new WrapperPlayServerSetCooldown(fallbackItem, ticks));
    }

    public float getSeconds() {
        return this.seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    public Optional<ResourceLocation> getCooldownGroup() {
        return this.cooldownGroup;
    }

    public void setCooldownGroup(Optional<ResourceLocation> cooldownGroup) {
        this.cooldownGroup = cooldownGroup;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemUseCooldown)) {
            return false;
        }
        ItemUseCooldown that = (ItemUseCooldown)obj;
        if (Float.compare(that.seconds, this.seconds) != 0) {
            return false;
        }
        return this.cooldownGroup.equals(that.cooldownGroup);
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.seconds), this.cooldownGroup);
    }

    public String toString() {
        return "ItemUseCooldown{seconds=" + this.seconds + ", cooldownGroup=" + this.cooldownGroup + '}';
    }
}

