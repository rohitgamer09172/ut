/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.json;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.util.Codec;
import java.io.IOException;

public interface LegacyHoverEventSerializer {
    public  @NotNull HoverEvent.ShowItem deserializeShowItem(@NotNull Component var1) throws IOException;

    @NotNull
    public Component serializeShowItem( @NotNull HoverEvent.ShowItem var1) throws IOException;

    public  @NotNull HoverEvent.ShowEntity deserializeShowEntity(@NotNull Component var1, Codec.Decoder<Component, String, ? extends RuntimeException> var2) throws IOException;

    @NotNull
    public Component serializeShowEntity( @NotNull HoverEvent.ShowEntity var1, Codec.Encoder<Component, String, ? extends RuntimeException> var2) throws IOException;
}

