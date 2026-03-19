/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.utils.data.HeadRotation;
import java.util.ArrayList;
import java.util.List;

@CheckData(name="BadPacketsJ", description="Rotation in use item packet did not match tick rotation")
public class BadPacketsJ
extends Check
implements PacketCheck {
    private final List<HeadRotation> rotations = new ArrayList<HeadRotation>();

    public BadPacketsJ(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!this.player.cameraEntity.isSelf()) {
            this.rotations.clear();
            return;
        }
        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(event);
            this.rotations.add(new HeadRotation(packet.getYaw(), packet.getPitch()));
        }
        if (this.isTickPacket(event.getPacketType())) {
            boolean allowLast = this.player.canSkipTicks() && (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION);
            for (HeadRotation rotation : this.rotations) {
                if (rotation.yaw() == this.player.yaw && rotation.pitch() == this.player.pitch) {
                    allowLast = false;
                    continue;
                }
                if (rotation.yaw() == this.player.lastYaw && rotation.pitch() == this.player.lastPitch && allowLast) continue;
                this.flagAndAlert();
            }
            this.rotations.clear();
        }
    }
}

