/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.flattener;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.flattener.ComponentFlattenerImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.flattener.FlattenerListener;
import ac.grim.grimac.shaded.kyori.adventure.util.Buildable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ComponentFlattener
extends Buildable<ComponentFlattener, Builder> {
    public static final int NO_NESTING_LIMIT = -1;

    @NotNull
    public static Builder builder() {
        return new ComponentFlattenerImpl.BuilderImpl();
    }

    @NotNull
    public static ComponentFlattener basic() {
        return ComponentFlattenerImpl.BASIC;
    }

    @NotNull
    public static ComponentFlattener textOnly() {
        return ComponentFlattenerImpl.TEXT_ONLY;
    }

    public void flatten(@NotNull Component var1, @NotNull FlattenerListener var2);

    public static interface Builder
    extends AbstractBuilder<ComponentFlattener>,
    Buildable.Builder<ComponentFlattener> {
        @NotNull
        public <T extends Component> Builder mapper(@NotNull Class<T> var1, @NotNull Function<T, String> var2);

        @NotNull
        public <T extends Component> Builder complexMapper(@NotNull Class<T> var1, @NotNull BiConsumer<T, Consumer<Component>> var2);

        @NotNull
        public Builder unknownMapper(@Nullable Function<Component, String> var1);

        @NotNull
        public Builder nestingLimit(@Range(from=1L, to=0x7FFFFFFFL) int var1);
    }
}

