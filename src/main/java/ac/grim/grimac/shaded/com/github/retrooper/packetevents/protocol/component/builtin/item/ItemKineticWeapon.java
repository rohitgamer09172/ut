/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemKineticWeapon {
    private int contactCooldownTicks;
    private int delayTicks;
    private @Nullable Condition dismountConditions;
    private @Nullable Condition knockbackConditions;
    private @Nullable Condition damageConditions;
    private float forwardMovement;
    private float damageMultiplier;
    private @Nullable Sound sound;
    private @Nullable Sound hitSound;

    public ItemKineticWeapon(int contactCooldownTicks, int delayTicks, @Nullable Condition dismountConditions, @Nullable Condition knockbackConditions, @Nullable Condition damageConditions, float forwardMovement, float damageMultiplier, @Nullable Sound sound, @Nullable Sound hitSound) {
        this.contactCooldownTicks = contactCooldownTicks;
        this.delayTicks = delayTicks;
        this.dismountConditions = dismountConditions;
        this.knockbackConditions = knockbackConditions;
        this.damageConditions = damageConditions;
        this.forwardMovement = forwardMovement;
        this.damageMultiplier = damageMultiplier;
        this.sound = sound;
        this.hitSound = hitSound;
    }

    public static ItemKineticWeapon read(PacketWrapper<?> wrapper) {
        int contactCooldownTicks = wrapper.readVarInt();
        int delayTicks = wrapper.readVarInt();
        Condition dismountConditions = (Condition)wrapper.readOptional(Condition::read);
        Condition knockbackConditions = (Condition)wrapper.readOptional(Condition::read);
        Condition damageConditions = (Condition)wrapper.readOptional(Condition::read);
        float forwardMovement = wrapper.readFloat();
        float damageMultiplier = wrapper.readFloat();
        Sound sound = (Sound)wrapper.readOptional(Sound::read);
        Sound hitSound = (Sound)wrapper.readOptional(Sound::read);
        return new ItemKineticWeapon(contactCooldownTicks, delayTicks, dismountConditions, knockbackConditions, damageConditions, forwardMovement, damageMultiplier, sound, hitSound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemKineticWeapon component) {
        wrapper.writeVarInt(component.contactCooldownTicks);
        wrapper.writeVarInt(component.delayTicks);
        wrapper.writeOptional(component.dismountConditions, Condition::write);
        wrapper.writeOptional(component.knockbackConditions, Condition::write);
        wrapper.writeOptional(component.damageConditions, Condition::write);
        wrapper.writeFloat(component.forwardMovement);
        wrapper.writeFloat(component.damageMultiplier);
        wrapper.writeOptional(component.sound, Sound::write);
        wrapper.writeOptional(component.hitSound, Sound::write);
    }

    public int getContactCooldownTicks() {
        return this.contactCooldownTicks;
    }

    public void setContactCooldownTicks(int contactCooldownTicks) {
        this.contactCooldownTicks = contactCooldownTicks;
    }

    public int getDelayTicks() {
        return this.delayTicks;
    }

    public void setDelayTicks(int delayTicks) {
        this.delayTicks = delayTicks;
    }

    public @Nullable Condition getDismountConditions() {
        return this.dismountConditions;
    }

    public void setDismountConditions(@Nullable Condition dismountConditions) {
        this.dismountConditions = dismountConditions;
    }

    public @Nullable Condition getKnockbackConditions() {
        return this.knockbackConditions;
    }

    public void setKnockbackConditions(@Nullable Condition knockbackConditions) {
        this.knockbackConditions = knockbackConditions;
    }

    public @Nullable Condition getDamageConditions() {
        return this.damageConditions;
    }

    public void setDamageConditions(@Nullable Condition damageConditions) {
        this.damageConditions = damageConditions;
    }

    public float getForwardMovement() {
        return this.forwardMovement;
    }

    public void setForwardMovement(float forwardMovement) {
        this.forwardMovement = forwardMovement;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public @Nullable Sound getSound() {
        return this.sound;
    }

    public void setSound(@Nullable Sound sound) {
        this.sound = sound;
    }

    public @Nullable Sound getHitSound() {
        return this.hitSound;
    }

    public void setHitSound(@Nullable Sound hitSound) {
        this.hitSound = hitSound;
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ItemKineticWeapon that = (ItemKineticWeapon)obj;
        if (this.contactCooldownTicks != that.contactCooldownTicks) {
            return false;
        }
        if (this.delayTicks != that.delayTicks) {
            return false;
        }
        if (Float.compare(that.forwardMovement, this.forwardMovement) != 0) {
            return false;
        }
        if (Float.compare(that.damageMultiplier, this.damageMultiplier) != 0) {
            return false;
        }
        if (!Objects.equals(this.dismountConditions, that.dismountConditions)) {
            return false;
        }
        if (!Objects.equals(this.knockbackConditions, that.knockbackConditions)) {
            return false;
        }
        if (!Objects.equals(this.damageConditions, that.damageConditions)) {
            return false;
        }
        if (!Objects.equals(this.sound, that.sound)) {
            return false;
        }
        return Objects.equals(this.hitSound, that.hitSound);
    }

    public int hashCode() {
        return Objects.hash(this.contactCooldownTicks, this.delayTicks, this.dismountConditions, this.knockbackConditions, this.damageConditions, Float.valueOf(this.forwardMovement), Float.valueOf(this.damageMultiplier), this.sound, this.hitSound);
    }

    public static class Condition {
        private int maxDurationTicks;
        private float minSpeed;
        private float minRelativeSpeed;

        public Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
            this.maxDurationTicks = maxDurationTicks;
            this.minSpeed = minSpeed;
            this.minRelativeSpeed = minRelativeSpeed;
        }

        public static Condition read(PacketWrapper<?> wrapper) {
            int maxDurationTicks = wrapper.readVarInt();
            float minSpeed = wrapper.readFloat();
            float minRelativeSpeed = wrapper.readFloat();
            return new Condition(maxDurationTicks, minSpeed, minRelativeSpeed);
        }

        public static void write(PacketWrapper<?> wrapper, Condition condition) {
            wrapper.writeVarInt(condition.maxDurationTicks);
            wrapper.writeFloat(condition.minSpeed);
            wrapper.writeFloat(condition.minRelativeSpeed);
        }

        public int getMaxDurationTicks() {
            return this.maxDurationTicks;
        }

        public void setMaxDurationTicks(int maxDurationTicks) {
            this.maxDurationTicks = maxDurationTicks;
        }

        public float getMinSpeed() {
            return this.minSpeed;
        }

        public void setMinSpeed(float minSpeed) {
            this.minSpeed = minSpeed;
        }

        public float getMinRelativeSpeed() {
            return this.minRelativeSpeed;
        }

        public void setMinRelativeSpeed(float minRelativeSpeed) {
            this.minRelativeSpeed = minRelativeSpeed;
        }

        public boolean equals(Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            Condition condition = (Condition)obj;
            if (this.maxDurationTicks != condition.maxDurationTicks) {
                return false;
            }
            if (Float.compare(condition.minSpeed, this.minSpeed) != 0) {
                return false;
            }
            return Float.compare(condition.minRelativeSpeed, this.minRelativeSpeed) == 0;
        }

        public int hashCode() {
            return Objects.hash(this.maxDurationTicks, Float.valueOf(this.minSpeed), Float.valueOf(this.minRelativeSpeed));
        }
    }
}

