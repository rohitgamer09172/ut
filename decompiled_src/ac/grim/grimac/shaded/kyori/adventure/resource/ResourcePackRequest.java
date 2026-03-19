/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.resource;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackCallback;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackInfo;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackInfoLike;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackRequestImpl;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackRequestLike;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.List;
import java.util.Objects;

public interface ResourcePackRequest
extends Examinable,
ResourcePackRequestLike {
    @NotNull
    public static ResourcePackRequest addingRequest(@NotNull ResourcePackInfoLike first, ResourcePackInfoLike ... others) {
        return (ResourcePackRequest)ResourcePackRequest.resourcePackRequest().packs(first, others).replace(false).build();
    }

    @NotNull
    public static Builder resourcePackRequest() {
        return new ResourcePackRequestImpl.BuilderImpl();
    }

    @NotNull
    public static Builder resourcePackRequest(@NotNull ResourcePackRequest existing) {
        return new ResourcePackRequestImpl.BuilderImpl(Objects.requireNonNull(existing, "existing"));
    }

    @NotNull
    public List<ResourcePackInfo> packs();

    @NotNull
    public ResourcePackRequest packs(@NotNull Iterable<? extends ResourcePackInfoLike> var1);

    @NotNull
    public ResourcePackCallback callback();

    @NotNull
    public ResourcePackRequest callback(@NotNull ResourcePackCallback var1);

    public boolean replace();

    @NotNull
    public ResourcePackRequest replace(boolean var1);

    public boolean required();

    @Nullable
    public Component prompt();

    @Override
    @NotNull
    default public ResourcePackRequest asResourcePackRequest() {
        return this;
    }

    public static interface Builder
    extends AbstractBuilder<ResourcePackRequest>,
    ResourcePackRequestLike {
        @Contract(value="_, _ -> this")
        @NotNull
        public Builder packs(@NotNull ResourcePackInfoLike var1, ResourcePackInfoLike ... var2);

        @Contract(value="_ -> this")
        @NotNull
        public Builder packs(@NotNull Iterable<? extends ResourcePackInfoLike> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder callback(@NotNull ResourcePackCallback var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder replace(boolean var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder required(boolean var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder prompt(@Nullable Component var1);

        @Override
        @NotNull
        default public ResourcePackRequest asResourcePackRequest() {
            return (ResourcePackRequest)this.build();
        }
    }
}

