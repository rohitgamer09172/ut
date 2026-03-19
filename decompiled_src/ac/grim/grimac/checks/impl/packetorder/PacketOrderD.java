/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(name="PacketOrderD", experimental=true)
public class PacketOrderD
extends Check
implements PacketCheck {
    private boolean sentMainhand;
    private int requiredEntity;
    private boolean requiredSneaking;

    public PacketOrderD(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientInteractEntity packet;
        WrapperPlayClientInteractEntity.InteractAction action;
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && (action = (packet = new WrapperPlayClientInteractEntity(event)).getAction()) != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            boolean sneaking = packet.isSneaking().orElse(false);
            int entity = packet.getEntityId();
            if (packet.getHand() == InteractionHand.OFF_HAND) {
                String verbose;
                if (action == WrapperPlayClientInteractEntity.InteractAction.INTERACT) {
                    if (!this.sentMainhand && this.flagAndAlert("Skipped Mainhand") && this.shouldModifyPackets()) {
                        event.setCancelled(true);
                        this.player.onPacketCancel();
                    }
                    this.sentMainhand = false;
                } else if ((sneaking != this.requiredSneaking || entity != this.requiredEntity) && this.flagAndAlert(verbose = "requiredEntity=" + this.requiredEntity + ", entity=" + entity + ", requiredSneaking=" + this.requiredSneaking + ", sneaking=" + sneaking) && this.shouldModifyPackets()) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            } else {
                this.requiredEntity = entity;
                this.requiredSneaking = sneaking;
                this.sentMainhand = true;
            }
        }
        if (this.isTickPacket(event.getPacketType())) {
            this.sentMainhand = false;
        }
    }
}

