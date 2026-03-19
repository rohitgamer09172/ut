/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name="BadPacketsD", description="Impossible pitch")
public class BadPacketsD
extends Check
implements PacketCheck {
    public BadPacketsD(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        float pitch;
        if (this.player.packetStateData.lastPacketWasTeleport) {
            return;
        }
        if ((event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) && ((pitch = new WrapperPlayClientPlayerFlying(event).getLocation().getPitch()) > 90.0f || pitch < -90.0f) && this.flagAndAlert("pitch=" + pitch) && this.shouldModifyPackets()) {
            if (this.player.pitch > 90.0f) {
                this.player.pitch = 90.0f;
            }
            if (this.player.pitch < -90.0f) {
                this.player.pitch = -90.0f;
            }
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
    }
}

