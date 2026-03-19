/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimIdentity;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.command.CommandUtils;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GrimList
implements BuildableCommand {
    private final SuggestionProvider<Sender> SUGGESTIONS = CommandUtils.fromStrings("players");

    @Override
    public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
        commandManager.command(commandManager.commandBuilder("grim", "grimac").literal("list", new String[0]).permission("grim.list").required("list", StringParser.stringParser(), this.SUGGESTIONS).handler(commandContext -> this.handleList((Sender)commandContext.sender(), commandContext.getOrDefault("list", "?").toLowerCase())).build());
    }

    private void handleList(Sender sender, String id) {
        switch (id) {
            case "players": {
                this.handleListPlayers(sender);
                break;
            }
            default: {
                sender.sendMessage((Component)((TextComponent.Builder)((TextComponent.Builder)Component.text().append((Component)Component.text("Invalid argument: ", (TextColor)NamedTextColor.GRAY))).append((Component)Component.text(id, (TextColor)NamedTextColor.RED))).build());
            }
        }
    }

    private Component playerComponent(String name, UUID uuid, boolean online, boolean exempt) {
        return ((TextComponent)((TextComponent)Component.text(name).color(exempt ? (online ? NamedTextColor.GRAY : NamedTextColor.DARK_GRAY) : (online ? NamedTextColor.WHITE : NamedTextColor.RED))).clickEvent(ClickEvent.copyToClipboard(name))).hoverEvent(HoverEvent.showText(this.playerHoverComponent(uuid, online, exempt, true)));
    }

    private Component playerHoverComponent(UUID uuid, boolean online, boolean exempt, boolean registered) {
        TextComponent.Builder builder = Component.text();
        builder.append((ComponentBuilder<?, ?>)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append(Component.text("UUID: ").color(NamedTextColor.GRAY))).append(Component.text(String.valueOf(uuid)).color(NamedTextColor.WHITE))).append((Component)Component.newline())).append(Component.text("Status: ").color(NamedTextColor.GRAY))).append(online ? Component.text("Online").color(NamedTextColor.GREEN) : Component.text("Offline").color(NamedTextColor.RED)));
        if (exempt) {
            builder.append((Component)Component.newline());
            builder.append(Component.text("Is Exempt").color(NamedTextColor.LIGHT_PURPLE));
        }
        if (!registered) {
            builder.append((Component)Component.newline());
            builder.append(Component.text("Not Registered").color(NamedTextColor.RED));
        }
        return builder.build();
    }

    private void handleListPlayers(Sender sender) {
        TextComponent.Builder builder = Component.text();
        Map onlinePlayers = GrimAPI.INSTANCE.getPlatformPlayerFactory().getOnlinePlayers().stream().collect(Collectors.toMap(GrimIdentity::getUniqueId, Function.identity()));
        HashSet unregisteredPlayers = new HashSet(onlinePlayers.values());
        boolean after = false;
        builder.append((Component)Component.text("Players = [", (TextColor)NamedTextColor.GRAY));
        for (GrimPlayer entry : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            if (after) {
                builder.append(Component.text(", ").color(NamedTextColor.GRAY));
            } else {
                after = true;
            }
            PlatformPlayer platformPlayer = (PlatformPlayer)onlinePlayers.get(entry.getUniqueId());
            if (platformPlayer != null) {
                unregisteredPlayers.remove(platformPlayer);
            }
            boolean online = platformPlayer != null && platformPlayer.isOnline();
            boolean exempt = !GrimAPI.INSTANCE.getPlayerDataManager().shouldCheck(entry.user);
            builder.append(this.playerComponent(entry.getName(), entry.getUniqueId(), online, exempt));
        }
        for (PlatformPlayer platformPlayer : unregisteredPlayers) {
            if (after) {
                builder.append(Component.text(", ").color(NamedTextColor.GRAY));
            } else {
                after = true;
            }
            builder.append((Component)((TextComponent)((TextComponent)Component.text(platformPlayer.getName()).color(NamedTextColor.LIGHT_PURPLE)).clickEvent(ClickEvent.suggestCommand(platformPlayer.getName()))).hoverEvent(HoverEvent.showText(this.playerHoverComponent(platformPlayer.getUniqueId(), platformPlayer.isOnline(), false, false))));
        }
        builder.append((Component)Component.text("]", (TextColor)NamedTextColor.GRAY));
        sender.sendMessage((Component)builder.build());
    }
}

