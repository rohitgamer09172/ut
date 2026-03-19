/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTag;
import ac.grim.grimac.shaded.kyori.examination.string.StringExaminer;

abstract class AbstractBinaryTag
implements BinaryTag {
    AbstractBinaryTag() {
    }

    @Override
    @NotNull
    public final String examinableName() {
        return this.type().toString();
    }

    public final String toString() {
        return this.examine(StringExaminer.simpleEscaping());
    }
}

