/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.adventure.text.event.RemovedDataComponentValueImpl;
import ac.grim.grimac.shaded.kyori.examination.Examinable;

public interface DataComponentValue
extends Examinable {
    public static @NotNull Removed removed() {
        return RemovedDataComponentValueImpl.REMOVED;
    }

    public static interface Removed
    extends DataComponentValue {
    }

    public static interface TagSerializable
    extends DataComponentValue {
        @NotNull
        public BinaryTagHolder asBinaryTag();
    }
}

