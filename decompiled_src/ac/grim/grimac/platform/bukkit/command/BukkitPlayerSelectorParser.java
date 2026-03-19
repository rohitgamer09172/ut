/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.bukkit.command;

import ac.grim.grimac.platform.api.command.AbstractPlayerSelectorParser;
import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.bukkit.command.BukkitPlayerSelectorAdapter;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.SinglePlayerSelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SinglePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.concurrent.CompletableFuture;

public class BukkitPlayerSelectorParser<C>
extends AbstractPlayerSelectorParser<C> {
    @Override
    public ParserDescriptor<C, PlayerSelector> descriptor() {
        return super.createDescriptor();
    }

    @Override
    protected ParserDescriptor<C, ?> getPlatformSpecificDescriptor() {
        return SinglePlayerSelectorParser.singlePlayerSelectorParser();
    }

    @Override
    protected CompletableFuture<PlayerSelector> adaptToCommonSelector(CommandContext<C> context, Object platformSpecificSelector) {
        return CompletableFuture.completedFuture(new BukkitPlayerSelectorAdapter((SinglePlayerSelector)platformSpecificSelector));
    }
}

