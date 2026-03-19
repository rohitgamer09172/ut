/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.Permission
 *  org.bukkit.permissions.PermissionDefault
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.platform.bukkit.player;

import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.player.PlatformInventory;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.platform.bukkit.entity.BukkitGrimEntity;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformInventory;
import ac.grim.grimac.platform.bukkit.utils.anticheat.MultiLibUtil;
import ac.grim.grimac.platform.bukkit.utils.convert.BukkitConversionUtils;
import ac.grim.grimac.platform.bukkit.utils.reflection.PaperUtils;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotConversionUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.platform.bukkit.BukkitAudiences;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import ac.grim.grimac.utils.math.Location;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

public class BukkitPlatformPlayer
extends BukkitGrimEntity
implements PlatformPlayer {
    private static final BukkitAudiences audiences = BukkitAudiences.create((Plugin)GrimACBukkitLoaderPlugin.LOADER);
    private final Player bukkitPlayer;
    private final PlatformInventory inventory;
    @Nullable
    private final User user;

    public BukkitPlatformPlayer(Player bukkitPlayer) {
        super((Entity)bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
        this.inventory = new BukkitPlatformInventory(bukkitPlayer);
        if (CommonGrimArguments.USE_CHAT_FAST_BYPASS.value().booleanValue()) {
            Object channel = PacketEvents.getAPI().getProtocolManager().getChannel(bukkitPlayer.getUniqueId());
            this.user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
        } else {
            this.user = null;
        }
    }

    @Override
    public void kickPlayer(String textReason) {
        this.bukkitPlayer.kickPlayer(textReason);
    }

    @Override
    public boolean hasPermission(String s) {
        return this.bukkitPlayer.hasPermission(s);
    }

    @Override
    public boolean hasPermission(String s, boolean defaultIfUnset) {
        return this.bukkitPlayer.hasPermission(new Permission(s, defaultIfUnset ? PermissionDefault.TRUE : PermissionDefault.FALSE));
    }

    @Override
    public boolean isSneaking() {
        return this.bukkitPlayer.isSneaking();
    }

    @Override
    public void setSneaking(boolean isSneaking) {
        this.bukkitPlayer.setSneaking(isSneaking);
    }

    @Override
    public void sendMessage(String message) {
        if (CommonGrimArguments.USE_CHAT_FAST_BYPASS.value().booleanValue() && this.user != null) {
            this.user.sendMessage(message);
        } else {
            this.bukkitPlayer.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Component message) {
        if (CommonGrimArguments.USE_CHAT_FAST_BYPASS.value().booleanValue() && this.user != null) {
            this.user.sendMessage(message);
        } else {
            audiences.player(this.bukkitPlayer).sendMessage(message);
        }
    }

    @Override
    public boolean isOnline() {
        return this.bukkitPlayer.isOnline();
    }

    @Override
    public String getName() {
        return this.bukkitPlayer.getName();
    }

    @Override
    public void updateInventory() {
        this.bukkitPlayer.updateInventory();
    }

    @Override
    public Vector3d getPosition() {
        if (CAN_USE_DIRECT_GETTERS) {
            return new Vector3d(this.bukkitPlayer.getX(), this.bukkitPlayer.getY(), this.bukkitPlayer.getZ());
        }
        org.bukkit.Location location = this.bukkitPlayer.getLocation();
        return new Vector3d(location.getX(), location.getY(), location.getZ());
    }

    @Override
    @Nullable
    public GrimEntity getVehicle() {
        return this.bukkitPlayer.getVehicle() == null ? null : new BukkitGrimEntity(this.bukkitPlayer.getVehicle());
    }

    @Override
    public GameMode getGameMode() {
        return SpigotConversionUtil.fromBukkitGameMode(this.bukkitPlayer.getGameMode());
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.bukkitPlayer.setGameMode(SpigotConversionUtil.toBukkitGameMode(gameMode));
    }

    public World getBukkitWorld() {
        return this.bukkitPlayer.getWorld();
    }

    @Override
    public UUID getUniqueId() {
        return this.bukkitPlayer.getUniqueId();
    }

    @Override
    public boolean eject() {
        return this.bukkitPlayer.eject();
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(Location location) {
        org.bukkit.Location bLoc = BukkitConversionUtils.toBukkitLocation(location);
        return PaperUtils.teleportAsync((Entity)this.bukkitPlayer, bLoc);
    }

    @Override
    public boolean isExternalPlayer() {
        return MultiLibUtil.isExternalPlayer(this.bukkitPlayer);
    }

    @Override
    public void sendPluginMessage(String channelName, byte[] byteArray) {
        this.bukkitPlayer.sendPluginMessage((Plugin)GrimACBukkitLoaderPlugin.LOADER, channelName, byteArray);
    }

    @Override
    public Sender getSender() {
        return GrimACBukkitLoaderPlugin.LOADER.getBukkitSenderFactory().map((CommandSender)this.bukkitPlayer);
    }

    @NotNull
    public Player getNative() {
        return this.bukkitPlayer;
    }

    @Generated
    public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    @Override
    @Generated
    public PlatformInventory getInventory() {
        return this.inventory;
    }
}

