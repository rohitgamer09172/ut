/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

final class ArgumentQueueImpl<T extends Tag.Argument>
implements ArgumentQueue {
    private final Context context;
    final List<T> args;
    private int ptr = 0;

    ArgumentQueueImpl(Context context, List<T> args) {
        this.context = context;
        this.args = args;
    }

    @NotNull
    public T pop() {
        if (!this.hasNext()) {
            throw this.context.newException("Missing argument for this tag!", this);
        }
        return (T)((Tag.Argument)this.args.get(this.ptr++));
    }

    @NotNull
    public T popOr(@NotNull String errorMessage) {
        Objects.requireNonNull(errorMessage, "errorMessage");
        if (!this.hasNext()) {
            throw this.context.newException(errorMessage, this);
        }
        return (T)((Tag.Argument)this.args.get(this.ptr++));
    }

    @NotNull
    public T popOr(@NotNull Supplier<String> errorMessage) {
        Objects.requireNonNull(errorMessage, "errorMessage");
        if (!this.hasNext()) {
            throw this.context.newException(Objects.requireNonNull(errorMessage.get(), "errorMessage.get()"), this);
        }
        return (T)((Tag.Argument)this.args.get(this.ptr++));
    }

    @Nullable
    public T peek() {
        return (T)(this.hasNext() ? (Tag.Argument)this.args.get(this.ptr) : null);
    }

    @Override
    public boolean hasNext() {
        return this.ptr < this.args.size();
    }

    @Override
    public void reset() {
        this.ptr = 0;
    }

    public String toString() {
        return this.args.toString();
    }
}

