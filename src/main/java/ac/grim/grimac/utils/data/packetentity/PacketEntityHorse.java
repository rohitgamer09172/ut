/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.JumpableEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityTrackXRot;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public class PacketEntityHorse
extends PacketEntityTrackXRot
implements JumpableEntity {
    public boolean isRearing = false;
    public boolean hasSaddle = false;
    public boolean isTame = false;
    private boolean horseJumping = false;
    private float horseJump = 0.0f;
    private static final boolean HAS_SADDLE_SENT_BY_SERVER = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_21_4);

    public PacketEntityHorse(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, float xRot) {
        super(player, uuid, type, x, y, z, xRot);
        this.trackEntityEquipment = true;
        this.setAttribute(Attributes.STEP_HEIGHT, 1.0);
        boolean preAttribute = player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5);
        this.trackAttribute(ValuedAttribute.ranged(Attributes.JUMP_STRENGTH, 0.7, 0.0, preAttribute ? 2.0 : 32.0).withSetRewriter((oldValue, newValue) -> {
            if (preAttribute && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                return oldValue;
            }
            return newValue;
        }));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.225f, 0.0, 1024.0));
        if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CHESTED_HORSE)) {
            this.setAttribute(Attributes.JUMP_STRENGTH, 0.5);
            this.setAttribute(Attributes.MOVEMENT_SPEED, 0.175f);
        }
        if (type == EntityTypes.ZOMBIE_HORSE || type == EntityTypes.SKELETON_HORSE) {
            this.setAttribute(Attributes.MOVEMENT_SPEED, 0.2f);
        }
    }

    @Override
    public boolean hasSaddle() {
        if (HAS_SADDLE_SENT_BY_SERVER) {
            return this.hasSaddle;
        }
        return this.hasItemInSlot(EquipmentSlot.SADDLE);
    }

    @Override
    public boolean isJumping() {
        return this.horseJumping;
    }

    @Override
    public void setJumping(boolean jumping) {
        this.horseJumping = jumping;
    }

    @Override
    public float getJumpPower() {
        return this.horseJump;
    }

    @Override
    public void setJumpPower(float jumpPower) {
        this.horseJump = jumpPower;
    }

    @Override
    public boolean canPlayerJump(GrimPlayer player) {
        return this.hasSaddle();
    }

    @Override
    public void executeJump(GrimPlayer player, Set<VectorData> possibleVectors) {
        boolean wantsToJump;
        boolean bl = wantsToJump = this.getJumpPower() > 0.0f && !this.isJumping() && player.lastOnGround;
        if (!wantsToJump) {
            return;
        }
        float forwardInput = player.vehicleData.vehicleForward;
        if (forwardInput <= 0.0f) {
            forwardInput *= 0.25f;
        }
        double jumpFactor = (float)this.getAttributeValue(Attributes.JUMP_STRENGTH) * this.getJumpPower() * JumpPower.getPlayerJumpFactor(player);
        OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
        double jumpVelocity = jumpBoost.isPresent() ? jumpFactor + (double)((float)(jumpBoost.getAsInt() + 1) * 0.1f) : jumpFactor;
        this.setJumping(true);
        float yawRadians = GrimMath.radians(player.yaw);
        float f2 = player.trigHandler.sin(yawRadians);
        float f3 = player.trigHandler.cos(yawRadians);
        for (VectorData vectorData : possibleVectors) {
            vectorData.vector.setY(jumpVelocity);
            if (!(forwardInput > 0.0f)) continue;
            vectorData.vector.add(new Vector3dm((double)(-0.4f * f2 * this.getJumpPower()), 0.0, (double)(0.4f * f3 * this.getJumpPower())));
        }
        this.setJumpPower(0.0f);
    }
}

