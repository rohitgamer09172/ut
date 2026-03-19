/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.GsonDataComponentValueImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.Objects;

@ApiStatus.NonExtendable
public interface GsonDataComponentValue
extends DataComponentValue {
    public static GsonDataComponentValue gsonDataComponentValue(@NotNull JsonElement data) {
        if (data instanceof JsonNull) {
            return GsonDataComponentValueImpl.RemovedGsonComponentValueImpl.INSTANCE;
        }
        return new GsonDataComponentValueImpl(Objects.requireNonNull(data, "data"));
    }

    @NotNull
    public JsonElement element();
}

