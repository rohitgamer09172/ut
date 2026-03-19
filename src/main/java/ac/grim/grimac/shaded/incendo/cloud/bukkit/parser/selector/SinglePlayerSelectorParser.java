/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.SinglePlayerSelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.PlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SelectorUtils;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SinglePlayerSelectorParser<C>
extends SelectorUtils.PlayerSelectorParser<C, SinglePlayerSelector> {
    @API(status=API.Status.STABLE, since="2.0.0")
    public static <C> @NonNull ParserDescriptor<C, SinglePlayerSelector> singlePlayerSelectorParser() {
        return ParserDescriptor.of(new SinglePlayerSelectorParser<C>(), SinglePlayerSelector.class);
    }

    @API(status=API.Status.STABLE, since="2.0.0")
    public static <C> @NonNull CommandComponent.Builder<C, SinglePlayerSelector> singlePlayerSelectorComponent() {
        return CommandComponent.builder().parser(SinglePlayerSelectorParser.singlePlayerSelectorParser());
    }

    public SinglePlayerSelectorParser() {
        super(true);
    }

    @Override
    @API(status=API.Status.INTERNAL, consumers={"ac.grim.grimac.shaded.incendo.cloud.*"})
    public SinglePlayerSelector mapResult(final @NonNull String input, @NonNull SelectorUtils.EntitySelectorWrapper wrapper) {
        final Player player = wrapper.singlePlayer();
        return new SinglePlayerSelector(){

            @Override
            public @NonNull Player single() {
                return player;
            }

            @Override
            public @NonNull String inputString() {
                return input;
            }
        };
    }

    @Override
    protected @NonNull CompletableFuture<ArgumentParseResult<SinglePlayerSelector>> legacyParse(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
        String input = commandInput.peekString();
        final @Nullable Player player = Bukkit.getPlayer((String)input);
        if (player == null) {
            return CompletableFuture.completedFuture(ArgumentParseResult.failure(new PlayerParser.PlayerParseException(input, commandContext)));
        }
        final String pop = commandInput.readString();
        return ArgumentParseResult.successFuture(new SinglePlayerSelector(){

            @Override
            public @NonNull Player single() {
                return player;
            }

            @Override
            public @NonNull String inputString() {
                return pop;
            }
        });
    }
}

