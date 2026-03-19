/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt.api;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolderImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import ac.grim.grimac.shaded.kyori.adventure.util.Codec;

public interface BinaryTagHolder
extends DataComponentValue.TagSerializable {
    @NotNull
    public static <T, EX extends Exception> BinaryTagHolder encode(@NotNull T nbt, @NotNull Codec<? super T, String, ?, EX> codec) throws EX {
        return new BinaryTagHolderImpl(codec.encode(nbt));
    }

    @NotNull
    public static BinaryTagHolder binaryTagHolder(@NotNull String string) {
        return new BinaryTagHolderImpl(string);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static BinaryTagHolder of(@NotNull String string) {
        return new BinaryTagHolderImpl(string);
    }

    @NotNull
    public String string();

    @Override
    @NotNull
    default public BinaryTagHolder asBinaryTag() {
        return this;
    }

    @NotNull
    public <T, DX extends Exception> T get(@NotNull Codec<T, String, DX, ?> var1) throws DX;
}

