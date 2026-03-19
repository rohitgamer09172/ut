/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.events.packets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerInitializeWorldBorder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderCenter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderSize;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayWorldBorderLerpSize;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.utils.worldborder.BorderExtent;
import ac.grim.grimac.utils.worldborder.RealTimeMovingBorderExtent;
import ac.grim.grimac.utils.worldborder.StaticBorderExtent;
import ac.grim.grimac.utils.worldborder.TickBasedMovingBorderExtent;
import lombok.Generated;

public class PacketWorldBorder
extends Check
implements PacketCheck {
    private double centerX;
    private double centerZ;
    private double absoluteMaxSize;
    private BorderExtent extent = new StaticBorderExtent(5.999997E7);
    private static final boolean SERVER_TICK_BASED = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);

    public PacketWorldBorder(GrimPlayer playerData) {
        super(playerData);
    }

    public double getCurrentDiameter() {
        return this.extent.size();
    }

    public double getMinX() {
        return this.extent.getMinX(this.centerX, this.absoluteMaxSize);
    }

    public double getMaxX() {
        return this.extent.getMaxX(this.centerX, this.absoluteMaxSize);
    }

    public double getMinZ() {
        return this.extent.getMinZ(this.centerZ, this.absoluteMaxSize);
    }

    public double getMaxZ() {
        return this.extent.getMaxZ(this.centerZ, this.absoluteMaxSize);
    }

    public void tickBorder() {
        this.extent = this.extent.tick();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        int portalTeleportBoundary;
        long speed;
        double newDiameter;
        double centerZ;
        double centerX;
        long speed2;
        double newDiameter2;
        double oldDiameter;
        PacketWrapper packet;
        if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER) {
            packet = new WrapperPlayServerWorldBorder(event);
            this.player.sendTransaction();
            if (((WrapperPlayServerWorldBorder)packet).getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.SET_SIZE) {
                double size = ((WrapperPlayServerWorldBorder)packet).getRadius();
                this.player.addRealTimeTaskNow(() -> this.setSize(size));
            } else if (((WrapperPlayServerWorldBorder)packet).getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.LERP_SIZE) {
                oldDiameter = ((WrapperPlayServerWorldBorder)packet).getOldRadius();
                newDiameter2 = ((WrapperPlayServerWorldBorder)packet).getNewRadius();
                speed2 = ((WrapperPlayServerWorldBorder)packet).getSpeed();
                this.player.addRealTimeTaskNow(() -> this.setLerp(oldDiameter, newDiameter2, speed2));
            } else if (((WrapperPlayServerWorldBorder)packet).getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.SET_CENTER) {
                centerX = ((WrapperPlayServerWorldBorder)packet).getCenterX();
                centerZ = ((WrapperPlayServerWorldBorder)packet).getCenterZ();
                this.player.addRealTimeTaskNow(() -> this.setCenter(centerX, centerZ));
            } else if (((WrapperPlayServerWorldBorder)packet).getAction() == WrapperPlayServerWorldBorder.WorldBorderAction.INITIALIZE) {
                centerX = ((WrapperPlayServerWorldBorder)packet).getCenterX();
                centerZ = ((WrapperPlayServerWorldBorder)packet).getCenterZ();
                double oldDiameter2 = ((WrapperPlayServerWorldBorder)packet).getOldRadius();
                newDiameter = ((WrapperPlayServerWorldBorder)packet).getNewRadius();
                speed = ((WrapperPlayServerWorldBorder)packet).getSpeed();
                portalTeleportBoundary = ((WrapperPlayServerWorldBorder)packet).getPortalTeleportBoundary();
                this.player.addRealTimeTaskNow(() -> {
                    this.setCenter(centerX, centerZ);
                    this.setLerp(oldDiameter2, newDiameter, speed);
                    this.absoluteMaxSize = portalTeleportBoundary;
                });
            }
        }
        if (event.getPacketType() == PacketType.Play.Server.INITIALIZE_WORLD_BORDER) {
            this.player.sendTransaction();
            packet = new WrapperPlayServerInitializeWorldBorder(event);
            centerX = ((WrapperPlayServerInitializeWorldBorder)packet).getX();
            centerZ = ((WrapperPlayServerInitializeWorldBorder)packet).getZ();
            double oldDiameter3 = ((WrapperPlayServerInitializeWorldBorder)packet).getOldDiameter();
            newDiameter = ((WrapperPlayServerInitializeWorldBorder)packet).getNewDiameter();
            speed = ((WrapperPlayServerInitializeWorldBorder)packet).getSpeed();
            portalTeleportBoundary = ((WrapperPlayServerInitializeWorldBorder)packet).getPortalTeleportBoundary();
            this.player.addRealTimeTaskNow(() -> {
                this.setCenter(centerX, centerZ);
                this.setLerp(oldDiameter3, newDiameter, speed);
                this.absoluteMaxSize = portalTeleportBoundary;
            });
        }
        if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER_CENTER) {
            this.player.sendTransaction();
            packet = new WrapperPlayServerWorldBorderCenter(event);
            centerX = ((WrapperPlayServerWorldBorderCenter)packet).getX();
            centerZ = ((WrapperPlayServerWorldBorderCenter)packet).getZ();
            this.player.addRealTimeTaskNow(() -> this.setCenter(centerX, centerZ));
        }
        if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER_SIZE) {
            this.player.sendTransaction();
            double size = new WrapperPlayServerWorldBorderSize(event).getDiameter();
            this.player.addRealTimeTaskNow(() -> this.setSize(size));
        }
        if (event.getPacketType() == PacketType.Play.Server.WORLD_BORDER_LERP_SIZE) {
            this.player.sendTransaction();
            WrapperPlayWorldBorderLerpSize packet2 = new WrapperPlayWorldBorderLerpSize(event);
            oldDiameter = packet2.getOldDiameter();
            newDiameter2 = packet2.getNewDiameter();
            speed2 = packet2.getSpeed();
            this.player.addRealTimeTaskNow(() -> this.setLerp(oldDiameter, newDiameter2, speed2));
        }
    }

    @Contract(mutates="this")
    private void setCenter(double x, double z) {
        this.centerX = x;
        this.centerZ = z;
    }

    @Contract(mutates="this")
    private void setSize(double size) {
        this.extent = new StaticBorderExtent(size);
    }

    @Contract(mutates="this")
    private void setLerp(double oldDiameter, double newDiameter, long speed) {
        this.extent = speed <= 0L || oldDiameter == newDiameter ? new StaticBorderExtent(newDiameter) : this.createMovingExtent(oldDiameter, newDiameter, speed);
    }

    private BorderExtent createMovingExtent(double from, double to, long speed) {
        if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_11)) {
            long durationTicks = SERVER_TICK_BASED ? speed : speed / 50L;
            return new TickBasedMovingBorderExtent(from, to, durationTicks);
        }
        long durationMs = SERVER_TICK_BASED ? speed * 50L : speed;
        return new RealTimeMovingBorderExtent(from, to, durationMs);
    }

    @Generated
    public double getCenterX() {
        return this.centerX;
    }

    @Generated
    public double getCenterZ() {
        return this.centerZ;
    }

    @Generated
    public double getAbsoluteMaxSize() {
        return this.absoluteMaxSize;
    }

    @Generated
    public BorderExtent getExtent() {
        return this.extent;
    }
}

