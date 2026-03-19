/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.json;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.ComponentSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONComponentSerializerAccessor;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JSONComponentSerializer
extends ComponentSerializer<Component, Component, String> {
    @NotNull
    public static JSONComponentSerializer json() {
        return JSONComponentSerializerAccessor.Instances.INSTANCE;
    }

    public static @NotNull Builder builder() {
        return JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
    }

    public static interface Builder {
        @NotNull
        public Builder options(@NotNull OptionState var1);

        @NotNull
        public Builder editOptions(@NotNull Consumer<OptionState.Builder> var1);

        @Deprecated
        @NotNull
        public Builder downsampleColors();

        @NotNull
        public Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer var1);

        @Deprecated
        @NotNull
        public Builder emitLegacyHoverEvent();

        @NotNull
        public JSONComponentSerializer build();
    }

    @PlatformAPI
    @ApiStatus.Internal
    public static interface Provider {
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        public JSONComponentSerializer instance();

        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        public @NotNull Supplier<@NotNull Builder> builder();
    }
}

