/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.RemoteConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.Permission
 *  org.bukkit.permissions.PermissionDefault
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.platform.bukkit.sender;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.platform.bukkit.BukkitAudiences;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

public class BukkitSenderFactory
extends SenderFactory<CommandSender>
implements SenderMapper<CommandSender, Sender> {
    private final BukkitAudiences audiences = BukkitAudiences.create((Plugin)GrimACBukkitLoaderPlugin.LOADER);

    @Override
    protected String getName(CommandSender sender) {
        return sender instanceof Player ? sender.getName() : "Console";
    }

    @Override
    protected UUID getUniqueId(CommandSender sender) {
        UUID uUID;
        if (sender instanceof Player) {
            Player player = (Player)sender;
            uUID = player.getUniqueId();
        } else {
            uUID = Sender.CONSOLE_UUID;
        }
        return uUID;
    }

    @Override
    protected void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    @Override
    protected void sendMessage(CommandSender sender, Component message) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
            this.audiences.sender(sender).sendMessage(message);
        } else {
            GrimAPI.INSTANCE.getScheduler().getGlobalRegionScheduler().run(GrimAPI.INSTANCE.getGrimPlugin(), () -> this.audiences.sender(sender).sendMessage(message));
        }
    }

    @Override
    protected boolean hasPermission(CommandSender sender, String node) {
        return sender.hasPermission(node);
    }

    @Override
    protected boolean hasPermission(CommandSender sender, String node, boolean defaultIfUnset) {
        return sender.hasPermission(new Permission(node, defaultIfUnset ? PermissionDefault.TRUE : PermissionDefault.FALSE));
    }

    @Override
    protected void performCommand(CommandSender sender, String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender;
    }

    @Override
    protected boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    @NotNull
    public Sender map(@NotNull CommandSender base) {
        return this.wrap(base);
    }

    @Override
    @NotNull
    public CommandSender reverse(@NotNull Sender mapped) {
        return (CommandSender)this.unwrap(mapped);
    }
}

