/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import lombok.Generated;

public final class WatchableIndexUtil {
    @Nullable
    public static EntityData<?> getIndex(@NotNull List<EntityData<?>> objects, int index) {
        for (EntityData<?> object : objects) {
            if (object.getIndex() != index) continue;
            return object;
        }
        return null;
    }

    @Generated
    private WatchableIndexUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

