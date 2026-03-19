/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.lazy;

import ac.grim.grimac.utils.lazy.LazyHolder;
import java.util.function.Supplier;

final class ThreadSafeLazyHolder<T>
implements LazyHolder<T> {
    private final Supplier<T> supplier;
    private volatile T value;

    ThreadSafeLazyHolder(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public T get() {
        T result = this.value;
        if (result == null) {
            ThreadSafeLazyHolder threadSafeLazyHolder = this;
            synchronized (threadSafeLazyHolder) {
                result = this.value;
                if (result == null) {
                    this.value = result = this.supplier.get();
                }
            }
        }
        return result;
    }
}

