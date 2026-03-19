/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.manager.cloud;

import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.manager.CommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;

public interface CloudCommandAdapter
extends CommandAdapter {
    public ParserDescriptor<Sender, PlayerSelector> singlePlayerSelectorParser();

    public SuggestionProvider<Sender> onlinePlayerSuggestions();
}

