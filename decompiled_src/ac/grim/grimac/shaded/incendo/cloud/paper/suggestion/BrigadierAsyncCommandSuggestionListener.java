/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
 *  org.bukkit.event.EventHandler
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.AsyncCommandSuggestionListener;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips.CompletionMapper;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips.CompletionMapperFactory;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.util.StringUtils;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.event.EventHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

class BrigadierAsyncCommandSuggestionListener<C>
extends AsyncCommandSuggestionListener<C> {
    private final CompletionMapperFactory completionMapperFactory = CompletionMapperFactory.detectingRelocation();
    private final SuggestionFactory<C, ? extends TooltipSuggestion> suggestionFactory;

    BrigadierAsyncCommandSuggestionListener(@NonNull LegacyPaperCommandManager<C> paperCommandManager) {
        super(paperCommandManager);
        this.suggestionFactory = paperCommandManager.suggestionFactory().mapped(TooltipSuggestion::tooltipSuggestion);
    }

    @Override
    @EventHandler
    void onTabCompletion(@NonNull AsyncTabCompleteEvent event) {
        super.onTabCompletion(event);
    }

    @Override
    protected Suggestions<C, ? extends TooltipSuggestion> querySuggestions(@NonNull C commandSender, @NonNull String input) {
        return this.suggestionFactory.suggestImmediately(commandSender, input);
    }

    @Override
    protected void setSuggestions(@NonNull AsyncTabCompleteEvent event, @NonNull C commandSender, @NonNull String input) {
        CompletionMapper completionMapper = this.completionMapperFactory.createMapper();
        Suggestions suggestions = this.querySuggestions(commandSender, input);
        event.completions(suggestions.list().stream().map(suggestion -> {
            @Nullable String trim = StringUtils.trimBeforeLastSpace(suggestion.suggestion(), suggestions.commandInput());
            if (trim == null) {
                return null;
            }
            return suggestion.withSuggestion(trim);
        }).filter(Objects::nonNull).map(completionMapper::map).collect(Collectors.toList()));
    }
}

