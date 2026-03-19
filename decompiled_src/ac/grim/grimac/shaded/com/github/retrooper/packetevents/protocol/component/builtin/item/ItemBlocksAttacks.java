/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemBlocksAttacks {
    private float blockDelaySeconds;
    private float disableCooldownScale;
    private List<DamageReduction> damageReductions;
    private ItemDamageFunction itemDamage;
    @Nullable
    private ResourceLocation bypassedBy;
    @Nullable
    private Sound blockSound;
    @Nullable
    private Sound disableSound;

    public ItemBlocksAttacks(float blockDelaySeconds, float disableCooldownScale, List<DamageReduction> damageReductions, ItemDamageFunction itemDamage, @Nullable ResourceLocation bypassedBy, @Nullable Sound blockSound, @Nullable Sound disableSound) {
        this.blockDelaySeconds = blockDelaySeconds;
        this.disableCooldownScale = disableCooldownScale;
        this.damageReductions = damageReductions;
        this.itemDamage = itemDamage;
        this.bypassedBy = bypassedBy;
        this.blockSound = blockSound;
        this.disableSound = disableSound;
    }

    public static ItemBlocksAttacks read(PacketWrapper<?> wrapper) {
        float blockDelaySeconds = wrapper.readFloat();
        float disableCooldownScale = wrapper.readFloat();
        List<DamageReduction> damageReductions = wrapper.readList(DamageReduction::read);
        ItemDamageFunction itemDamage = ItemDamageFunction.read(wrapper);
        ResourceLocation bypassedBy = (ResourceLocation)wrapper.readOptional(PacketWrapper::readIdentifier);
        Sound blockSound = (Sound)wrapper.readOptional(Sound::read);
        Sound disableSound = (Sound)wrapper.readOptional(Sound::read);
        return new ItemBlocksAttacks(blockDelaySeconds, disableCooldownScale, damageReductions, itemDamage, bypassedBy, blockSound, disableSound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBlocksAttacks attack) {
        wrapper.writeFloat(attack.blockDelaySeconds);
        wrapper.writeFloat(attack.disableCooldownScale);
        wrapper.writeList(attack.damageReductions, DamageReduction::write);
        ItemDamageFunction.write(wrapper, attack.itemDamage);
        wrapper.writeOptional(attack.bypassedBy, PacketWrapper::writeIdentifier);
        wrapper.writeOptional(attack.blockSound, Sound::write);
        wrapper.writeOptional(attack.disableSound, Sound::write);
    }

    public float getBlockDelaySeconds() {
        return this.blockDelaySeconds;
    }

    public void setBlockDelaySeconds(float blockDelaySeconds) {
        this.blockDelaySeconds = blockDelaySeconds;
    }

    public float getDisableCooldownScale() {
        return this.disableCooldownScale;
    }

    public void setDisableCooldownScale(float disableCooldownScale) {
        this.disableCooldownScale = disableCooldownScale;
    }

    public List<DamageReduction> getDamageReductions() {
        return this.damageReductions;
    }

    public void setDamageReductions(List<DamageReduction> damageReductions) {
        this.damageReductions = damageReductions;
    }

    public ItemDamageFunction getItemDamage() {
        return this.itemDamage;
    }

    public void setItemDamage(ItemDamageFunction itemDamage) {
        this.itemDamage = itemDamage;
    }

    @Nullable
    public ResourceLocation getBypassedBy() {
        return this.bypassedBy;
    }

    public void setBypassedBy(@Nullable ResourceLocation bypassedBy) {
        this.bypassedBy = bypassedBy;
    }

    @Nullable
    public Sound getBlockSound() {
        return this.blockSound;
    }

    public void setBlockSound(@Nullable Sound blockSound) {
        this.blockSound = blockSound;
    }

    @Nullable
    public Sound getDisableSound() {
        return this.disableSound;
    }

    public void setDisableSound(@Nullable Sound disableSound) {
        this.disableSound = disableSound;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ItemBlocksAttacks)) {
            return false;
        }
        ItemBlocksAttacks that = (ItemBlocksAttacks)obj;
        if (Float.compare(that.blockDelaySeconds, this.blockDelaySeconds) != 0) {
            return false;
        }
        if (Float.compare(that.disableCooldownScale, this.disableCooldownScale) != 0) {
            return false;
        }
        if (!this.damageReductions.equals(that.damageReductions)) {
            return false;
        }
        if (!this.itemDamage.equals(that.itemDamage)) {
            return false;
        }
        if (!Objects.equals(this.bypassedBy, that.bypassedBy)) {
            return false;
        }
        if (!Objects.equals(this.blockSound, that.blockSound)) {
            return false;
        }
        return Objects.equals(this.disableSound, that.disableSound);
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.blockDelaySeconds), Float.valueOf(this.disableCooldownScale), this.damageReductions, this.itemDamage, this.bypassedBy, this.blockSound, this.disableSound);
    }

    public static final class ItemDamageFunction {
        private float threshold;
        private float base;
        private float factor;

        public ItemDamageFunction(float threshold, float base, float factor) {
            this.threshold = threshold;
            this.base = base;
            this.factor = factor;
        }

        public static ItemDamageFunction read(PacketWrapper<?> wrapper) {
            float threshold = wrapper.readFloat();
            float base = wrapper.readFloat();
            float factor = wrapper.readFloat();
            return new ItemDamageFunction(threshold, base, factor);
        }

        public static void write(PacketWrapper<?> wrapper, ItemDamageFunction function) {
            wrapper.writeFloat(function.threshold);
            wrapper.writeFloat(function.base);
            wrapper.writeFloat(function.factor);
        }

        public float getThreshold() {
            return this.threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public float getBase() {
            return this.base;
        }

        public void setBase(float base) {
            this.base = base;
        }

        public float getFactor() {
            return this.factor;
        }

        public void setFactor(float factor) {
            this.factor = factor;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ItemDamageFunction)) {
                return false;
            }
            ItemDamageFunction that = (ItemDamageFunction)obj;
            if (Float.compare(that.threshold, this.threshold) != 0) {
                return false;
            }
            if (Float.compare(that.base, this.base) != 0) {
                return false;
            }
            return Float.compare(that.factor, this.factor) == 0;
        }

        public int hashCode() {
            return Objects.hash(Float.valueOf(this.threshold), Float.valueOf(this.base), Float.valueOf(this.factor));
        }
    }

    public static final class DamageReduction {
        private float horizontalBlockingAngle;
        @Nullable
        private MappedEntitySet<DamageType> type;
        private float base;
        private float factor;

        public DamageReduction(float horizontalBlockingAngle, @Nullable MappedEntitySet<DamageType> type, float base, float factor) {
            this.horizontalBlockingAngle = horizontalBlockingAngle;
            this.type = type;
            this.base = base;
            this.factor = factor;
        }

        public static DamageReduction read(PacketWrapper<?> wrapper) {
            float horizontalBlockingAngle = wrapper.readFloat();
            MappedEntitySet type = (MappedEntitySet)wrapper.readOptional(ew -> MappedEntitySet.read(ew, DamageTypes.getRegistry()));
            float base = wrapper.readFloat();
            float factor = wrapper.readFloat();
            return new DamageReduction(horizontalBlockingAngle, type, base, factor);
        }

        public static void write(PacketWrapper<?> wrapper, DamageReduction reduction) {
            wrapper.writeFloat(reduction.horizontalBlockingAngle);
            wrapper.writeOptional(reduction.type, MappedEntitySet::write);
            wrapper.writeFloat(reduction.base);
            wrapper.writeFloat(reduction.factor);
        }

        public float getHorizontalBlockingAngle() {
            return this.horizontalBlockingAngle;
        }

        public void setHorizontalBlockingAngle(float horizontalBlockingAngle) {
            this.horizontalBlockingAngle = horizontalBlockingAngle;
        }

        @Nullable
        public MappedEntitySet<DamageType> getType() {
            return this.type;
        }

        public void setType(@Nullable MappedEntitySet<DamageType> type) {
            this.type = type;
        }

        public float getBase() {
            return this.base;
        }

        public void setBase(float base) {
            this.base = base;
        }

        public float getFactor() {
            return this.factor;
        }

        public void setFactor(float factor) {
            this.factor = factor;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof DamageReduction)) {
                return false;
            }
            DamageReduction that = (DamageReduction)obj;
            if (Float.compare(that.horizontalBlockingAngle, this.horizontalBlockingAngle) != 0) {
                return false;
            }
            if (Float.compare(that.base, this.base) != 0) {
                return false;
            }
            if (Float.compare(that.factor, this.factor) != 0) {
                return false;
            }
            return Objects.equals(this.type, that.type);
        }

        public int hashCode() {
            return Objects.hash(Float.valueOf(this.horizontalBlockingAngle), this.type, Float.valueOf(this.base), Float.valueOf(this.factor));
        }
    }
}

