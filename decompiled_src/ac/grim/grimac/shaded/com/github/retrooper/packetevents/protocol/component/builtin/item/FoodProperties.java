/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FoodProperties {
    private int nutrition;
    private float saturation;
    private boolean canAlwaysEat;
    private float eatSeconds;
    private List<PossibleEffect> effects;
    @Nullable
    private ItemStack usingConvertsTo;

    public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat) {
        this(nutrition, saturation, canAlwaysEat, 1.6f, Collections.emptyList());
    }

    @ApiStatus.Obsolete
    public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat, float eatSeconds, List<PossibleEffect> effects) {
        this(nutrition, saturation, canAlwaysEat, eatSeconds, effects, null);
    }

    @ApiStatus.Obsolete
    public FoodProperties(int nutrition, float saturation, boolean canAlwaysEat, float eatSeconds, List<PossibleEffect> effects, @Nullable ItemStack usingConvertsTo) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.canAlwaysEat = canAlwaysEat;
        this.eatSeconds = eatSeconds;
        this.effects = effects;
        this.usingConvertsTo = usingConvertsTo;
    }

    public static FoodProperties read(PacketWrapper<?> wrapper) {
        int nutrition = wrapper.readVarInt();
        float saturation = wrapper.readFloat();
        boolean canAlwaysEat = wrapper.readBoolean();
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            return new FoodProperties(nutrition, saturation, canAlwaysEat);
        }
        float eatSeconds = wrapper.readFloat();
        ItemStack usingConvertsTo = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21) ? (ItemStack)wrapper.readOptional(PacketWrapper::readItemStack) : null;
        List<PossibleEffect> effects = wrapper.readList(PossibleEffect::read);
        return new FoodProperties(nutrition, saturation, canAlwaysEat, eatSeconds, effects, usingConvertsTo);
    }

    public static void write(PacketWrapper<?> wrapper, FoodProperties props) {
        wrapper.writeVarInt(props.nutrition);
        wrapper.writeFloat(props.saturation);
        wrapper.writeBoolean(props.canAlwaysEat);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_2)) {
            wrapper.writeFloat(props.eatSeconds);
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
                wrapper.writeOptional(props.usingConvertsTo, PacketWrapper::writeItemStack);
            }
            wrapper.writeList(props.effects, PossibleEffect::write);
        }
    }

    public int getNutrition() {
        return this.nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public boolean isCanAlwaysEat() {
        return this.canAlwaysEat;
    }

    public void setCanAlwaysEat(boolean canAlwaysEat) {
        this.canAlwaysEat = canAlwaysEat;
    }

    @ApiStatus.Obsolete
    public float getEatSeconds() {
        return this.eatSeconds;
    }

    @ApiStatus.Obsolete
    public void setEatSeconds(float eatSeconds) {
        this.eatSeconds = eatSeconds;
    }

    @ApiStatus.Obsolete
    public void addEffect(PossibleEffect effect) {
        this.effects.add(effect);
    }

    @ApiStatus.Obsolete
    public List<PossibleEffect> getEffects() {
        return this.effects;
    }

    @ApiStatus.Obsolete
    public void setEffects(List<PossibleEffect> effects) {
        this.effects = effects;
    }

    @ApiStatus.Obsolete
    @Nullable
    public ItemStack getUsingConvertsTo() {
        return this.usingConvertsTo;
    }

    @ApiStatus.Obsolete
    public void setUsingConvertsTo(@Nullable ItemStack usingConvertsTo) {
        this.usingConvertsTo = usingConvertsTo;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FoodProperties)) {
            return false;
        }
        FoodProperties that = (FoodProperties)obj;
        if (this.nutrition != that.nutrition) {
            return false;
        }
        if (Float.compare(that.saturation, this.saturation) != 0) {
            return false;
        }
        if (this.canAlwaysEat != that.canAlwaysEat) {
            return false;
        }
        if (Float.compare(that.eatSeconds, this.eatSeconds) != 0) {
            return false;
        }
        if (!this.effects.equals(that.effects)) {
            return false;
        }
        return Objects.equals(this.usingConvertsTo, that.usingConvertsTo);
    }

    public int hashCode() {
        return Objects.hash(this.nutrition, Float.valueOf(this.saturation), this.canAlwaysEat, Float.valueOf(this.eatSeconds), this.effects, this.usingConvertsTo);
    }

    @ApiStatus.Obsolete
    public static class PossibleEffect {
        private PotionEffect effect;
        private float probability;

        public PossibleEffect(PotionEffect effect, float probability) {
            this.effect = effect;
            this.probability = probability;
        }

        public static PossibleEffect read(PacketWrapper<?> wrapper) {
            PotionEffect effect = PotionEffect.read(wrapper);
            float probability = wrapper.readFloat();
            return new PossibleEffect(effect, probability);
        }

        public static void write(PacketWrapper<?> wrapper, PossibleEffect effect) {
            PotionEffect.write(wrapper, effect.effect);
            wrapper.writeFloat(effect.probability);
        }

        public PotionEffect getEffect() {
            return this.effect;
        }

        public void setEffect(PotionEffect effect) {
            this.effect = effect;
        }

        public float getProbability() {
            return this.probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PossibleEffect)) {
                return false;
            }
            PossibleEffect that = (PossibleEffect)obj;
            if (Float.compare(that.probability, this.probability) != 0) {
                return false;
            }
            return this.effect.equals(that.effect);
        }

        public int hashCode() {
            return Objects.hash(this.effect, Float.valueOf(this.probability));
        }
    }
}

