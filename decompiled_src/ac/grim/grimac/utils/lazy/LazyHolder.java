/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.lazy;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.lazy.SimpleLazyHolder;
import ac.grim.grimac.utils.lazy.ThreadSafeLazyHolder;
import java.util.function.Supplier;

public interface LazyHolder<T> {
    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static <T> LazyHolder<T> threadSafe(Supplier<T> supplier) {
        return new ThreadSafeLazyHolder<T>(supplier);
    }

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static <T> LazyHolder<T> simple(Supplier<T> supplier) {
        return new SimpleLazyHolder<T>(supplier);
    }

    public T get();
}

