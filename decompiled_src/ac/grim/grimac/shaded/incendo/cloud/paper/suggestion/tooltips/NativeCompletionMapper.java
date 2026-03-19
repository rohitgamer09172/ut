/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.event.server.AsyncTabCompleteEvent$Completion
 *  com.mojang.brigadier.Message
 *  io.papermc.paper.brigadier.PaperBrigadier
 *  io.papermc.paper.command.brigadier.MessageComponentSerializer
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips.CompletionMapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import com.mojang.brigadier.Message;
import io.papermc.paper.brigadier.PaperBrigadier;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

final class NativeCompletionMapper
implements CompletionMapper {
    NativeCompletionMapper() {
    }

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
     @NonNull AsyncTabCompleteEvent.Completion map(@NonNull TooltipSuggestion suggestion) {
        if (!CraftBukkitReflection.classExists("io.papermc.paper.command.brigadier.MessageComponentSerializer")) {
            return NativeCompletionMapper.mapLegacy(suggestion);
        }
        return AsyncTabCompleteEvent.Completion.completion((String)suggestion.suggestion(), (Component)MessageComponentSerializer.message().deserializeOrNull((Object)suggestion.tooltip()));
    }

    private static // Could not load outer class - annotation placement on inner may be incorrect
     @NonNull AsyncTabCompleteEvent.Completion mapLegacy(@NotNull TooltipSuggestion suggestion) {
        Message tooltip = suggestion.tooltip();
        if (tooltip == null) {
            return AsyncTabCompleteEvent.Completion.completion((String)suggestion.suggestion());
        }
        return AsyncTabCompleteEvent.Completion.completion((String)suggestion.suggestion(), (Component)PaperBrigadier.componentFromMessage((Message)tooltip));
    }
}

