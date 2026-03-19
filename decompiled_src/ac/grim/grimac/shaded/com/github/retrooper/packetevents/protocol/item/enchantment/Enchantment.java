/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;

public class Enchantment {
    private EnchantmentType type;
    private int level;

    public Enchantment(EnchantmentType type, int level) {
        this.type = type;
        this.level = level;
    }

    public EnchantmentType getType() {
        return this.type;
    }

    public void setType(EnchantmentType type) {
        this.type = type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private EnchantmentType type;
        private int level;

        public Builder type(EnchantmentType type) {
            this.type = type;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Enchantment build() {
            return new Enchantment(this.type, this.level);
        }
    }
}

