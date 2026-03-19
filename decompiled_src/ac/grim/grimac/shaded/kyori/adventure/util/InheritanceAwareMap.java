/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.CheckReturnValue;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.util.InheritanceAwareMapImpl;

public interface InheritanceAwareMap<C, V> {
    @NotNull
    public static <K, E> InheritanceAwareMap<K, E> empty() {
        return InheritanceAwareMapImpl.EMPTY;
    }

    public static <K, E> @NotNull Builder<K, E> builder() {
        return new InheritanceAwareMapImpl.BuilderImpl();
    }

    public static <K, E> @NotNull Builder<K, E> builder(InheritanceAwareMap<? extends K, ? extends E> existing) {
        return new InheritanceAwareMapImpl.BuilderImpl<K, E>().putAll(existing);
    }

    public boolean containsKey(@NotNull Class<? extends C> var1);

    @Nullable
    public V get(@NotNull Class<? extends C> var1);

    @CheckReturnValue
    @NotNull
    public InheritanceAwareMap<C, V> with(@NotNull Class<? extends C> var1, @NotNull V var2);

    @CheckReturnValue
    @NotNull
    public InheritanceAwareMap<C, V> without(@NotNull Class<? extends C> var1);

    public static interface Builder<C, V>
    extends AbstractBuilder<InheritanceAwareMap<C, V>> {
        @NotNull
        public Builder<C, V> strict(boolean var1);

        @NotNull
        public Builder<C, V> put(@NotNull Class<? extends C> var1, @NotNull V var2);

        @NotNull
        public Builder<C, V> remove(@NotNull Class<? extends C> var1);

        @NotNull
        public Builder<C, V> putAll(@NotNull InheritanceAwareMap<? extends C, ? extends V> var1);
    }
}

