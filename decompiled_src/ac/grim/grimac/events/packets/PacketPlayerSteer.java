/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.KnownInput;
import ac.grim.grimac.utils.data.packetentity.JumpableEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.math.Vec2;

public class PacketPlayerSteer
extends PacketListenerAbstract {
    public PacketPlayerSteer() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public boolean isPreVia() {
        return true;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
                return;
            }
            WrapperPlayClientSteerVehicle steer = new WrapperPlayClientSteerVehicle(event);
            float forwards = steer.getForward();
            float sideways = steer.getSideways();
            player.vehicleData.nextVehicleForward = forwards;
            player.vehicleData.nextVehicleHorizontal = sideways;
            this.tickPlayerWorld(player);
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_INPUT) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
                return;
            }
            WrapperPlayClientPlayerInput input = new WrapperPlayClientPlayerInput(event);
            int forward = 0;
            int sideways = 0;
            if (input.isForward()) {
                forward = (byte)(forward + 1);
            }
            if (input.isBackward()) {
                forward = (byte)(forward - 1);
            }
            if (input.isLeft()) {
                sideways = (byte)(sideways + 1);
            }
            if (input.isRight()) {
                sideways = (byte)(sideways - 1);
            }
            Vec2 inputVector = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5) ? PredictionEngine.modifyInput(player, new Vec2(forward, sideways).normalized()) : new Vec2((float)forward * 0.98f, (float)sideways * 0.98f);
            player.vehicleData.nextVehicleForward = inputVector.x();
            player.vehicleData.nextVehicleHorizontal = inputVector.y();
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_6)) {
                player.isSneaking = input.isShift();
            }
            player.packetStateData.knownInput = new KnownInput(input.isForward(), input.isBackward(), input.isLeft(), input.isRight(), input.isJump(), input.isShift(), input.isSprint());
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null || !player.inVehicle() || player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2)) {
                return;
            }
            this.tickPlayerWorld(player);
        }
    }

    private void tickPlayerWorld(GrimPlayer player) {
        PacketEntity riding = player.compensatedEntities.self.getRiding();
        if (player.packetStateData.receivedSteerVehicle && riding != null) {
            JumpableEntity jumpable;
            if ((riding.isBoat || riding.isHappyGhast || riding instanceof JumpableEntity && (jumpable = (JumpableEntity)((Object)riding)).hasSaddle()) && riding.passengers.get(0) == player.compensatedEntities.self && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                return;
            }
            player.compensatedWorld.tickPlayerInPistonPushingArea();
            player.compensatedEntities.tick();
            player.dashableEntities.tick();
            player.vehicleData.lastDummy = true;
            int controllingEntityId = player.inVehicle() ? player.getRidingVehicleId() : player.entityID;
            player.firstBreadKB = player.checkManager.getKnockbackHandler().calculateFirstBreadKnockback(controllingEntityId, player.lastTransactionReceived.get());
            player.likelyKB = player.checkManager.getKnockbackHandler().calculateRequiredKB(controllingEntityId, player.lastTransactionReceived.get(), false);
            if (player.firstBreadKB != null) {
                player.clientVelocity = player.firstBreadKB.vector;
            }
            if (player.likelyKB != null) {
                player.clientVelocity = player.likelyKB.vector;
            }
            player.firstBreadExplosion = player.checkManager.getExplosionHandler().getFirstBreadAddedExplosion(player.lastTransactionReceived.get());
            player.likelyExplosions = player.checkManager.getExplosionHandler().getPossibleExplosions(player.lastTransactionReceived.get(), false);
            player.checkManager.getExplosionHandler().forceExempt();
            player.checkManager.getKnockbackHandler().forceExempt();
            player.lastX = player.x;
            player.lastY = player.y;
            player.lastZ = player.z;
            SimpleCollisionBox vehiclePos = player.compensatedEntities.self.getRiding().getPossibleCollisionBoxes();
            player.x = (vehiclePos.minX + vehiclePos.maxX) / 2.0;
            player.y = (vehiclePos.minY + vehiclePos.maxY) / 2.0;
            player.z = (vehiclePos.minZ + vehiclePos.maxZ) / 2.0;
            if (player.isSprinting != player.lastSprinting) {
                player.compensatedEntities.hasSprintingAttributeEnabled = player.isSprinting;
            }
            player.lastSprinting = player.isSprinting;
        }
        player.packetStateData.receivedSteerVehicle = true;
    }
}

