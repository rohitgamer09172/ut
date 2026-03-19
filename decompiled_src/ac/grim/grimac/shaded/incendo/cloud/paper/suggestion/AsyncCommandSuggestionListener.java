/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.event.server.AsyncTabCompleteEvent
 *  org.bukkit.event.EventHandler
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitPluginRegistrationHandler;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.SuggestionListener;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.util.StringUtils;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.event.EventHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

class AsyncCommandSuggestionListener<C>
implements SuggestionListener<C> {
    private final LegacyPaperCommandManager<C> paperCommandManager;

    AsyncCommandSuggestionListener(@NonNull LegacyPaperCommandManager<C> paperCommandManager) {
        this.paperCommandManager = paperCommandManager;
    }

    @EventHandler
    void onTabCompletion(@NonNull AsyncTabCompleteEvent event) {
        String commandLabel;
        String strippedBuffer;
        String string = strippedBuffer = event.getBuffer().startsWith("/") ? event.getBuffer().substring(1) : event.getBuffer();
        if (strippedBuffer.trim().isEmpty()) {
            return;
        }
        BukkitPluginRegistrationHandler bukkitPluginRegistrationHandler = (BukkitPluginRegistrationHandler)this.paperCommandManager.commandRegistrationHandler();
        if (!bukkitPluginRegistrationHandler.isRecognized(commandLabel = strippedBuffer.split(" ")[0])) {
            return;
        }
        String input = event.getBuffer();
        if (input.charAt(0) == '/') {
            input = input.substring(1);
        }
        this.setSuggestions(event, this.paperCommandManager.senderMapper().map(event.getSender()), BukkitHelper.stripNamespace(this.paperCommandManager, input));
        event.setHandled(true);
    }

    protected Suggestions<C, ?> querySuggestions(@NonNull C commandSender, @NonNull String input) {
        return this.paperCommandManager.suggestionFactory().suggestImmediately(commandSender, input);
    }

    protected void setSuggestions(@NonNull AsyncTabCompleteEvent event, @NonNull C commandSender, @NonNull String input) {
        Suggestions suggestions = this.querySuggestions(commandSender, input);
        event.setCompletions(suggestions.list().stream().map(Suggestion::suggestion).map(suggestion -> StringUtils.trimBeforeLastSpace(suggestion, suggestions.commandInput())).filter(Objects::nonNull).collect(Collectors.toList()));
    }
}

