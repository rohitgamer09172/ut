/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.GsonComponentSerializerImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.kyori.adventure.util.Buildable;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface GsonComponentSerializer
extends JSONComponentSerializer,
Buildable<GsonComponentSerializer, Builder> {
    @NotNull
    public static GsonComponentSerializer gson() {
        return GsonComponentSerializerImpl.Instances.INSTANCE;
    }

    @NotNull
    public static GsonComponentSerializer colorDownsamplingGson() {
        return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
    }

    public static Builder builder() {
        return new GsonComponentSerializerImpl.BuilderImpl();
    }

    @NotNull
    public Gson serializer();

    @NotNull
    public UnaryOperator<GsonBuilder> populator();

    @NotNull
    public Component deserializeFromTree(@NotNull JsonElement var1);

    @NotNull
    public JsonElement serializeToTree(@NotNull Component var1);

    @PlatformAPI
    @ApiStatus.Internal
    public static interface Provider {
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        public GsonComponentSerializer gson();

        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        public GsonComponentSerializer gsonLegacy();

        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        public Consumer<Builder> builder();
    }

    public static interface Builder
    extends AbstractBuilder<GsonComponentSerializer>,
    Buildable.Builder<GsonComponentSerializer>,
    JSONComponentSerializer.Builder {
        @Override
        @NotNull
        public Builder options(@NotNull OptionState var1);

        @Override
        @NotNull
        public Builder editOptions(@NotNull Consumer<OptionState.Builder> var1);

        @Override
        @NotNull
        default public Builder downsampleColors() {
            return this.editOptions(features -> features.value(JSONOptions.EMIT_RGB, false));
        }

        @Deprecated
        @NotNull
        default public Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer serializer) {
            return this.legacyHoverEventSerializer((ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)serializer);
        }

        @Override
        @NotNull
        public Builder legacyHoverEventSerializer(@Nullable ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer var1);

        @Override
        @Deprecated
        @NotNull
        default public Builder emitLegacyHoverEvent() {
            return this.editOptions(b -> b.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.ALL));
        }

        @Override
        @NotNull
        public GsonComponentSerializer build();
    }
}

