/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.checks.impl.velocity;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.VelocityData;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.Deque;
import java.util.LinkedList;
import lombok.Generated;

@CheckData(name="AntiExplosion", configName="Explosion", setback=10.0)
public class ExplosionHandler
extends Check
implements PostPredictionCheck {
    private final Deque<VelocityData> firstBreadMap = new LinkedList<VelocityData>();
    private VelocityData lastExplosionsKnownTaken = null;
    private VelocityData firstBreadAddedExplosion = null;
    private boolean explosionPointThree = false;
    private double offsetToFlag;

    public ExplosionHandler(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.EXPLOSION) {
            Vector3d velocity;
            WrapperPlayServerExplosion explosion = new WrapperPlayServerExplosion(event);
            boolean hasBlocks = PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21_2);
            if (hasBlocks) {
                this.handleBlockExplosions(explosion);
            }
            if ((velocity = explosion.getKnockback()) != null && (velocity.x != 0.0 || velocity.y != 0.0 || velocity.z != 0.0)) {
                if (!hasBlocks || explosion.getRecords().isEmpty()) {
                    this.player.sendTransaction();
                }
                this.addPlayerExplosion(this.player.lastTransactionSent.get(), velocity);
                event.getTasksAfterSend().add(this.player::sendTransaction);
            }
        }
    }

    private void handleBlockExplosions(WrapperPlayServerExplosion explosion) {
        boolean shouldDestroy;
        @Nullable WrapperPlayServerExplosion.BlockInteraction blockInteraction = explosion.getBlockInteraction();
        boolean bl = shouldDestroy = blockInteraction != WrapperPlayServerExplosion.BlockInteraction.KEEP_BLOCKS;
        if (explosion.getRecords().isEmpty() || !shouldDestroy) {
            return;
        }
        this.player.sendTransaction();
        this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
            for (Vector3i record : explosion.getRecords()) {
                boolean canFlip;
                if (blockInteraction != WrapperPlayServerExplosion.BlockInteraction.TRIGGER_BLOCKS) {
                    this.player.compensatedWorld.updateBlock(record.x, record.y, record.z, 0);
                    continue;
                }
                WrappedBlockState state = this.player.compensatedWorld.getBlock(record);
                StateType type = state.getType();
                if (BlockTags.CANDLES.contains(type) || BlockTags.CANDLE_CAKES.contains(type)) {
                    state.setLit(false);
                    continue;
                }
                if (type == StateTypes.BELL || !(canFlip = state.hasProperty(StateValue.POWERED) && !state.isPowered() || type == StateTypes.LEVER)) continue;
                this.player.compensatedWorld.tickOpenable(record.x, record.y, record.z);
            }
        });
    }

    public VelocityData getFutureExplosion() {
        if (!this.firstBreadMap.isEmpty()) {
            return this.firstBreadMap.peek();
        }
        if (this.lastExplosionsKnownTaken != null) {
            return this.lastExplosionsKnownTaken;
        }
        if (this.player.firstBreadExplosion != null && this.player.likelyExplosions == null) {
            return this.player.firstBreadExplosion;
        }
        if (this.player.likelyExplosions != null) {
            return this.player.likelyExplosions;
        }
        return null;
    }

    public boolean shouldIgnoreForPrediction(VectorData data) {
        if (data.isExplosion() && data.isFirstBreadExplosion()) {
            return this.player.firstBreadExplosion.offset > this.offsetToFlag;
        }
        return false;
    }

    public boolean wouldFlag() {
        return this.player.likelyExplosions != null && this.player.likelyExplosions.offset > this.offsetToFlag || this.player.firstBreadExplosion != null && this.player.firstBreadExplosion.offset > this.offsetToFlag;
    }

    public void addPlayerExplosion(int breadOne, Vector3d explosion) {
        this.firstBreadMap.add(new VelocityData(-1, breadOne, this.player.getSetbackTeleportUtil().isSendingSetback, new Vector3dm(explosion.getX(), explosion.getY(), explosion.getZ())));
    }

    public void setPointThree(boolean isPointThree) {
        this.explosionPointThree = this.explosionPointThree || isPointThree;
    }

    public void handlePredictionAnalysis(double offset) {
        if (this.player.firstBreadExplosion != null) {
            this.player.firstBreadExplosion.offset = Math.min(this.player.firstBreadExplosion.offset, offset);
        }
        if (this.player.likelyExplosions != null) {
            this.player.likelyExplosions.offset = Math.min(this.player.likelyExplosions.offset, offset);
        }
    }

    public void forceExempt() {
        if (this.player.firstBreadExplosion != null) {
            this.player.firstBreadExplosion.offset = 0.0;
        }
        if (this.player.likelyExplosions != null) {
            this.player.likelyExplosions.offset = 0.0;
        }
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        double offset = predictionComplete.getOffset();
        boolean wasZero = this.explosionPointThree;
        this.explosionPointThree = false;
        if (this.player.likelyExplosions == null && this.player.firstBreadExplosion == null) {
            this.firstBreadAddedExplosion = null;
            return;
        }
        int minTrans = Math.min(this.player.likelyExplosions != null ? this.player.likelyExplosions.transaction : Integer.MAX_VALUE, this.player.firstBreadExplosion != null ? this.player.firstBreadExplosion.transaction : Integer.MAX_VALUE);
        int kbTrans = Math.max(this.player.likelyKB != null ? this.player.likelyKB.transaction : Integer.MIN_VALUE, this.player.firstBreadKB != null ? this.player.firstBreadKB.transaction : Integer.MIN_VALUE);
        if (this.player.predictedVelocity.isFirstBreadExplosion()) {
            this.firstBreadAddedExplosion = null;
            this.firstBreadMap.poll();
        }
        if (wasZero || this.player.predictedVelocity.isExplosion() || minTrans < kbTrans) {
            if (this.player.firstBreadExplosion != null) {
                this.player.firstBreadExplosion.offset = Math.min(this.player.firstBreadExplosion.offset, offset);
            }
            if (this.player.likelyExplosions != null) {
                this.player.likelyExplosions.offset = Math.min(this.player.likelyExplosions.offset, offset);
            }
        }
        if (this.player.likelyExplosions != null && !this.player.compensatedEntities.self.isDead) {
            if (this.player.likelyExplosions.offset > this.offsetToFlag) {
                this.flagAndAlertWithSetback((String)(this.player.likelyExplosions.offset == 2.147483647E9 ? "ignored explosion" : "o: " + this.formatOffset(offset)));
            } else {
                this.reward();
            }
        }
    }

    public VelocityData getPossibleExplosions(int lastTransaction, boolean isJustTesting) {
        this.handleTransactionPacket(lastTransaction);
        if (this.lastExplosionsKnownTaken == null) {
            return null;
        }
        VelocityData returnLastExplosion = this.lastExplosionsKnownTaken;
        if (!isJustTesting) {
            this.lastExplosionsKnownTaken = null;
        }
        return returnLastExplosion;
    }

    private void handleTransactionPacket(int transactionID) {
        VelocityData data = this.firstBreadMap.peek();
        while (data != null) {
            if (data.transaction == transactionID) {
                if (this.lastExplosionsKnownTaken != null) {
                    this.firstBreadAddedExplosion = new VelocityData(-1, data.transaction, data.isSetback, this.lastExplosionsKnownTaken.vector.clone().add(data.vector));
                    break;
                }
                this.firstBreadAddedExplosion = new VelocityData(-1, data.transaction, data.isSetback, data.vector);
                break;
            }
            if (data.transaction >= transactionID) break;
            if (this.lastExplosionsKnownTaken != null) {
                this.lastExplosionsKnownTaken.vector.add(data.vector);
            } else {
                this.lastExplosionsKnownTaken = new VelocityData(-1, data.transaction, data.isSetback, data.vector);
            }
            this.firstBreadAddedExplosion = null;
            this.firstBreadMap.poll();
            data = this.firstBreadMap.peek();
        }
    }

    public VelocityData getFirstBreadAddedExplosion(int lastTransaction) {
        this.handleTransactionPacket(lastTransaction);
        return this.firstBreadAddedExplosion;
    }

    @Override
    public void onReload(ConfigManager config) {
        this.offsetToFlag = config.getDoubleElse("Explosion.threshold", 1.0E-5);
    }

    @Generated
    public boolean isExplosionPointThree() {
        return this.explosionPointThree;
    }
}

