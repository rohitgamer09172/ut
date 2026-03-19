/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 */
package ac.grim.grimac.utils.data.json;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface JsonSerializable {
    @NotNull
    public JsonElement toJson();

    @NotNull
    public static JsonArray serializeArray(@Nullable JsonSerializable @NotNull [] serializableArray) {
        JsonArray array = new JsonArray();
        for (JsonSerializable serializable : serializableArray) {
            array.add((JsonElement)(serializable == null ? JsonNull.INSTANCE : serializable.toJson()));
        }
        return array;
    }

    public static <T extends JsonSerializable> T @NotNull [] deserializeArray(JsonArray jsonArray, IntFunction<T[]> newArray, Function<JsonElement, T> constructor) {
        JsonSerializable[] array = (JsonSerializable[])newArray.apply(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); ++i) {
            array[i] = (JsonSerializable)constructor.apply(jsonArray.get(i));
        }
        return array;
    }
}

