/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.command;

import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;

public final class CommandUtils {
    @Contract(value="_ -> new")
    @NotNull
    public static SuggestionProvider<Sender> fromStrings(String ... strings) {
        ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>(strings.length);
        for (String s : strings) {
            suggestions.add(Suggestion.suggestion(s));
        }
        return new SenderSuggestionProvider(Collections.unmodifiableList(suggestions));
    }

    @Generated
    private CommandUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static class SenderSuggestionProvider
    implements SuggestionProvider<Sender> {
        private final List<Suggestion> suggestions;

        @Override
        @NotNull
        public @NotNull CompletableFuture<? extends @NotNull Iterable<? extends @NotNull Suggestion>> suggestionsFuture(@NotNull CommandContext context, @NotNull CommandInput input) {
            return CompletableFuture.completedFuture(this.suggestions);
        }

        @Generated
        public SenderSuggestionProvider(List<Suggestion> suggestions) {
            this.suggestions = suggestions;
        }
    }
}

