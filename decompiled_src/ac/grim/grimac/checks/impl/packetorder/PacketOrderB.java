/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckData(name="PacketOrderB", description="Did not swing for attack")
public class PacketOrderB
extends Check
implements PacketCheck {
    private final boolean is1_9;
    private final boolean exempt;
    private boolean sentAnimationSinceLastAttack;
    private boolean sentAttack;
    private boolean sentAnimation;
    private boolean sentSlotSwitch;

    public PacketOrderB(GrimPlayer player) {
        super(player);
        this.is1_9 = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9);
        this.exempt = this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9);
        this.sentAnimationSinceLastAttack = this.player.getClientVersion().isNewerThan(ClientVersion.V_1_8);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientInteractEntity packet;
        if (this.exempt) {
            return;
        }
        if (event.getPacketType() == PacketType.Play.Client.ANIMATION) {
            this.sentAnimation = true;
            this.sentAnimationSinceLastAttack = true;
            this.sentSlotSwitch = false;
            this.sentAttack = false;
            return;
        }
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY && (packet = new WrapperPlayClientInteractEntity(event)).getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            this.sentAttack = true;
            if (this.is1_9 ? !this.sentAnimationSinceLastAttack : !this.sentAnimation) {
                this.sentAttack = false;
                if (this.flagAndAlert("pre-attack") && this.shouldModifyPackets()) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            }
            this.sentSlotSwitch = false;
            this.sentAnimation = false;
            this.sentAnimationSinceLastAttack = false;
            return;
        }
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE && !this.is1_9 && !this.sentSlotSwitch) {
            this.sentSlotSwitch = true;
            return;
        }
        if (!PacketOrderB.isAsync(event.getPacketType())) {
            if (this.sentAttack && this.is1_9) {
                this.flagAndAlert("post-attack");
            }
            this.sentSlotSwitch = false;
            this.sentAnimation = false;
            this.sentAttack = false;
        }
    }
}

