/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer;
import ac.grim.grimac.platform.api.player.PlatformInventory;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface PlatformPlayer
extends GrimEntity,
OfflinePlatformPlayer {
    public void kickPlayer(String var1);

    public boolean isSneaking();

    public void setSneaking(boolean var1);

    public boolean hasPermission(String var1);

    public boolean hasPermission(String var1, boolean var2);

    public void sendMessage(String var1);

    public void sendMessage(Component var1);

    public void updateInventory();

    public Vector3d getPosition();

    public PlatformInventory getInventory();

    @Nullable
    public GrimEntity getVehicle();

    public GameMode getGameMode();

    public void setGameMode(GameMode var1);

    public boolean isExternalPlayer();

    public void sendPluginMessage(String var1, byte[] var2);

    public Sender getSender();

    default public void replaceNativePlayer(Object nativePlayerObject) {
    }
}

