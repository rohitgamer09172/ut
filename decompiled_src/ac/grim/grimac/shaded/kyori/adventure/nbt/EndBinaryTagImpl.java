/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.kyori.adventure.nbt.AbstractBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.EndBinaryTag;

final class EndBinaryTagImpl
extends AbstractBinaryTag
implements EndBinaryTag {
    static final EndBinaryTagImpl INSTANCE = new EndBinaryTagImpl();

    EndBinaryTagImpl() {
    }

    public boolean equals(Object that) {
        return this == that;
    }

    public int hashCode() {
        return 0;
    }
}

