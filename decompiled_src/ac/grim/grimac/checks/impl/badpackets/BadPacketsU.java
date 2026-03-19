/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;

@CheckData(name="BadPacketsU", description="Sent impossible use item packet")
public class BadPacketsU
extends Check
implements PacketCheck {
    public BadPacketsU(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientPlayerBlockPlacement packet;
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && (packet = new WrapperPlayClientPlayerBlockPlacement(event)).getFace() == BlockFace.OTHER) {
            String verbose;
            int expectedY = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? 4095 : 255;
            boolean failedItemCheck = packet.getItemStack().isPresent() && this.isEmpty(packet.getItemStack().get()) && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9);
            Vector3i pos = packet.getBlockPosition();
            Vector3f cursor = packet.getCursorPosition();
            if ((failedItemCheck || pos.x != -1 || pos.y != expectedY || pos.z != -1 || cursor.x != 0.0f || cursor.y != 0.0f || cursor.z != 0.0f || packet.getSequence() != 0) && this.flagAndAlert(verbose = String.format("xyz=%s, %s, %s, cursor=%s, %s, %s, item=%s, sequence=%s", pos.x, pos.y, pos.z, Float.valueOf(cursor.x), Float.valueOf(cursor.y), Float.valueOf(cursor.z), !failedItemCheck, packet.getSequence())) && this.shouldModifyPackets()) {
                this.player.onPacketCancel();
                event.setCancelled(true);
            }
        }
    }

    private boolean isEmpty(ItemStack itemStack) {
        return itemStack.getType() == null || itemStack.getType() == ItemTypes.AIR;
    }
}

