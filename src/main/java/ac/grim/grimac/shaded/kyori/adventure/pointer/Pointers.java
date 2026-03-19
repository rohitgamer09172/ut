/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.pointer;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointer;
import ac.grim.grimac.shaded.kyori.adventure.pointer.PointersImpl;
import ac.grim.grimac.shaded.kyori.adventure.util.Buildable;
import java.util.Optional;
import java.util.function.Supplier;

public interface Pointers
extends Buildable<Pointers, Builder> {
    @Contract(pure=true)
    @NotNull
    public static Pointers empty() {
        return PointersImpl.EMPTY;
    }

    @Contract(pure=true)
    @NotNull
    public static Builder builder() {
        return new PointersImpl.BuilderImpl();
    }

    @NotNull
    public <T> Optional<T> get(@NotNull Pointer<T> var1);

    @Contract(value="_, null -> _; _, !null -> !null")
    @Nullable
    default public <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
        return this.get(pointer).orElse(defaultValue);
    }

    default public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
        return this.get(pointer).orElseGet(defaultValue);
    }

    public <T> boolean supports(@NotNull Pointer<T> var1);

    public static interface Builder
    extends AbstractBuilder<Pointers>,
    Buildable.Builder<Pointers> {
        @Contract(value="_, _ -> this")
        @NotNull
        default public <T> Builder withStatic(@NotNull Pointer<T> pointer, @Nullable T value) {
            return this.withDynamic(pointer, () -> value);
        }

        @Contract(value="_, _ -> this")
        @NotNull
        public <T> Builder withDynamic(@NotNull Pointer<T> var1, @NotNull Supplier<@Nullable T> var2);
    }
}

