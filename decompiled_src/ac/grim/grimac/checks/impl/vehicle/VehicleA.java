/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.vehicle;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;

@CheckData(name="VehicleA", description="Impossible input values")
public class VehicleA
extends Check
implements PacketCheck {
    public VehicleA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientSteerVehicle packet;
        if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE && (Math.abs((packet = new WrapperPlayClientSteerVehicle(event)).getForward()) > 0.98f || Math.abs(packet.getSideways()) > 0.98f) && this.flagAndAlert("forwards=" + packet.getForward() + ", sideways=" + packet.getSideways()) && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
    }
}

