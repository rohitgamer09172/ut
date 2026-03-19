/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.groundspoof;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.GhostBlockDetector;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.ArrayList;

@CheckData(name="NoFall", setback=10.0)
public class NoFall
extends Check
implements PacketCheck {
    public boolean flipPlayerGroundStatus = false;

    public NoFall(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientPlayerFlying wrapper;
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING || event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) {
            if (this.player.getSetbackTeleportUtil().insideUnloadedChunk()) {
                return;
            }
            if (this.player.getSetbackTeleportUtil().blockOffsets) {
                return;
            }
            wrapper = new WrapperPlayClientPlayerFlying(event);
            if (wrapper.isOnGround() && !wrapper.hasPositionChanged() && !this.isNearGround(wrapper.isOnGround())) {
                if (!GhostBlockDetector.isGhostBlock(this.player)) {
                    this.flagAndAlertWithSetback();
                }
                if (this.shouldModifyPackets()) {
                    wrapper.setOnGround(false);
                    event.markForReEncode(true);
                }
            }
        }
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            wrapper = new WrapperPlayClientPlayerFlying(event);
            if (this.flipPlayerGroundStatus) {
                this.flipPlayerGroundStatus = false;
                if (this.shouldModifyPackets()) {
                    wrapper.setOnGround(!wrapper.isOnGround());
                    event.markForReEncode(true);
                }
            }
            if (this.player.packetStateData.lastPacketWasTeleport && this.shouldModifyPackets()) {
                wrapper.setOnGround(false);
                event.markForReEncode(true);
            }
        }
    }

    private boolean isNearGround(boolean onGround) {
        if (onGround) {
            SimpleCollisionBox feetBB = GetBoundingBox.getBoundingBoxFromPosAndSize(this.player, this.player.x, this.player.y, this.player.z, 0.6f, 0.001f);
            feetBB.expand(this.player.getMovementThreshold());
            return this.checkForBoxes(feetBB);
        }
        return true;
    }

    private boolean checkForBoxes(SimpleCollisionBox playerBB) {
        ArrayList<SimpleCollisionBox> boxes = new ArrayList<SimpleCollisionBox>();
        Collisions.getCollisionBoxes(this.player, playerBB, boxes, false);
        for (SimpleCollisionBox box : boxes) {
            if (!playerBB.collidesVertically(box)) continue;
            return true;
        }
        return this.player.compensatedWorld.isNearHardEntity(playerBB.copy().expand(4.0));
    }
}

