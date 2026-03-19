/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Message
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 *  org.immutables.value.Value$Immutable
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestionImpl;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import com.mojang.brigadier.Message;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

@API(status=API.Status.STABLE, since="2.0.0")
@Value.Immutable
public interface TooltipSuggestion
extends Suggestion {
    public static @NonNull TooltipSuggestion suggestion(@NonNull String suggestion, @Nullable Message tooltip) {
        return TooltipSuggestionImpl.of(suggestion, tooltip);
    }

    public static @NonNull TooltipSuggestion tooltipSuggestion(@NonNull Suggestion suggestion) {
        if (suggestion instanceof TooltipSuggestion) {
            return (TooltipSuggestion)suggestion;
        }
        return TooltipSuggestion.suggestion(suggestion.suggestion(), null);
    }

    @Override
    public @NonNull String suggestion();

    public @Nullable Message tooltip();

    @Override
    public @NonNull TooltipSuggestion withSuggestion(@NonNull String var1);
}

