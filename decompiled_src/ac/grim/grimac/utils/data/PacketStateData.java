/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.data.KnownInput;
import lombok.Generated;

public class PacketStateData {
    public boolean packetPlayerOnGround = false;
    public boolean lastPacketWasTeleport = false;
    public boolean cancelDuplicatePacket;
    public boolean lastPacketWasOnePointSeventeenDuplicate = false;
    public boolean lastTransactionPacketWasValid = false;
    public int lastSlotSelected;
    public InteractionHand itemInUseHand = InteractionHand.MAIN_HAND;
    public long lastRiptide = 0L;
    public boolean tryingToRiptide = false;
    public int slowedByUsingItemTransaction = Integer.MIN_VALUE;
    public boolean receivedSteerVehicle = false;
    public boolean didLastLastMovementIncludePosition = false;
    public boolean didLastMovementIncludePosition = false;
    public boolean didSendMovementBeforeTickEnd = false;
    public KnownInput knownInput = KnownInput.DEFAULT;
    public Vector3d lastClaimedPosition = new Vector3d(0.0, 0.0, 0.0);
    public float lastHealth;
    public float lastSaturation;
    public int lastFood;
    public boolean lastServerTransWasValid = false;
    private int slowedByUsingItemSlot = Integer.MIN_VALUE;
    public boolean sendingBundlePacket;
    public boolean horseInteractCausedForcedRotation = false;

    public void setSlowedByUsingItem(boolean slowedByUsingItem) {
        this.slowedByUsingItemSlot = slowedByUsingItem ? this.lastSlotSelected : Integer.MIN_VALUE;
    }

    public boolean isSlowedByUsingItem() {
        return this.slowedByUsingItemSlot != Integer.MIN_VALUE;
    }

    @Generated
    public int getSlowedByUsingItemSlot() {
        return this.slowedByUsingItemSlot;
    }
}

