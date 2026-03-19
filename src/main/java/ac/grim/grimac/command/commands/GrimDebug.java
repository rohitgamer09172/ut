/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.description.Description;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.utils.anticheat.MessageUtil;

public class GrimDebug
implements BuildableCommand {
    @Override
    public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
        Command.Builder grimCommand = commandManager.commandBuilder("grim", "grimac");
        Command.Builder<Sender> debugCommand = grimCommand.literal("debug", Description.of("Toggle debug output for a player"), new String[0]).permission("grim.debug").optional("target", adapter.singlePlayerSelectorParser()).handler(this::handleDebug);
        Command.Builder<Sender> consoleDebugCommand = grimCommand.literal("consoledebug", Description.of("Toggle console debug output for a player"), new String[0]).permission("grim.consoledebug").required("target", adapter.singlePlayerSelectorParser()).handler(this::handleConsoleDebug);
        commandManager.command(debugCommand);
        commandManager.command(consoleDebugCommand);
    }

    private void handleDebug(@NotNull CommandContext<Sender> context) {
        PlayerSelector playerSelector;
        Sender sender = context.sender();
        GrimPlayer targetGrimPlayer = this.parseTarget(sender, (playerSelector = (PlayerSelector)context.getOrDefault("target", null)) == null ? sender : playerSelector.getSinglePlayer());
        if (targetGrimPlayer == null) {
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "player-not-found", "%prefix% &cPlayer is exempt or offline!"));
            return;
        }
        if (sender.isConsole()) {
            targetGrimPlayer.checkManager.getDebugHandler().toggleConsoleOutput();
        } else if (sender.isPlayer()) {
            GrimPlayer senderGrimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(sender.getUniqueId());
            if (senderGrimPlayer == null) {
                sender.sendMessage(MessageUtil.getParsedComponent(sender, "sender-not-found", "%prefix% &cYou cannot be exempt to use this command!"));
                return;
            }
            targetGrimPlayer.checkManager.getDebugHandler().toggleListener(senderGrimPlayer);
        } else {
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "run-as-player-or-console", "%prefix% &cThis command can only be used by players or the console!"));
        }
    }

    private void handleConsoleDebug(@NotNull CommandContext<Sender> context) {
        PlayerSelector targetName;
        Sender sender = context.sender();
        GrimPlayer grimPlayer = this.parseTarget(sender, (targetName = (PlayerSelector)context.getOrDefault("target", null)).getSinglePlayer());
        if (grimPlayer == null) {
            return;
        }
        boolean isOutput = grimPlayer.checkManager.getDebugHandler().toggleConsoleOutput();
        String playerName = grimPlayer.user.getProfile().getName();
        Object message = ((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append((Component)Component.text("Console output for ", (TextColor)NamedTextColor.GRAY))).append((Component)Component.text(playerName, (TextColor)NamedTextColor.WHITE))).append((Component)Component.text(" is now ", (TextColor)NamedTextColor.GRAY))).append((Component)Component.text(isOutput ? "enabled" : "disabled", (TextColor)NamedTextColor.WHITE))).build();
        sender.sendMessage((Component)message);
    }

    @Nullable
    private GrimPlayer parseTarget(@NotNull Sender sender, @Nullable Sender t) {
        if (sender.isConsole() && t == null) {
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "console-specify-target", "%prefix% &cYou must specify a target as the console!"));
            return null;
        }
        Sender target = t == null ? sender : t;
        GrimPlayer grimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(target.getUniqueId());
        if (grimPlayer == null) {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(sender.getPlatformPlayer().getNative());
            sender.sendMessage(MessageUtil.getParsedComponent(sender, "player-not-found", "%prefix% &cPlayer is exempt or offline!"));
            if (user == null) {
                sender.sendMessage(Component.text("Unknown PacketEvents user", (TextColor)NamedTextColor.RED));
            } else {
                boolean isExempt = GrimAPI.INSTANCE.getPlayerDataManager().shouldCheck(user);
                if (!isExempt) {
                    sender.sendMessage(Component.text("User connection state: " + String.valueOf((Object)user.getConnectionState()), (TextColor)NamedTextColor.RED));
                }
            }
        }
        return grimPlayer;
    }
}

