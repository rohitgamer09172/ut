/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.BlockBreakSpeed;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import java.util.Set;

@CheckData(name="FastBreak", description="Breaking blocks too quickly")
public class FastBreak
extends Check
implements BlockBreakCheck {
    private static final Set<StateType> EXEMPT_STATES = Set.of();
    private final boolean clientOlderThanServer;
    Vector3i targetBlockPosition;
    double maximumBlockDamage;
    long lastFinishBreak;
    long startBreak;
    double blockBreakBalance;
    double blockDelayBalance;

    public FastBreak(GrimPlayer playerData) {
        super(playerData);
        this.clientOlderThanServer = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion() > this.player.getClientVersion().getProtocolVersion();
        this.targetBlockPosition = null;
        this.maximumBlockDamage = 0.0;
        this.lastFinishBreak = 0L;
        this.startBreak = 0L;
        this.blockBreakBalance = 0.0;
        this.blockDelayBalance = 0.0;
    }

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        if (blockBreak.action == DiggingAction.START_DIGGING) {
            WrappedBlockState defaultState;
            if (!ViaVersionUtil.isAvailable && ((defaultState = WrappedBlockState.getDefaultState(this.player.getClientVersion(), blockBreak.block.getType())).getType() == StateTypes.AIR || EXEMPT_STATES.contains(defaultState.getType()))) {
                return;
            }
            WrappedBlockState block = this.clientOlderThanServer ? WrappedBlockState.getByGlobalId(this.player.getClientVersion(), this.player.getViaTranslatedClientBlockID(blockBreak.block.getGlobalId())) : blockBreak.block;
            this.startBreak = System.currentTimeMillis() - (long)(this.targetBlockPosition == null ? 50 : 0);
            this.targetBlockPosition = blockBreak.position;
            this.maximumBlockDamage = BlockBreakSpeed.getBlockDamage(this.player, block);
            double breakDelay = System.currentTimeMillis() - this.lastFinishBreak;
            this.blockDelayBalance = breakDelay >= 275.0 ? (this.blockDelayBalance *= 0.9) : (this.blockDelayBalance += 300.0 - breakDelay);
            if (this.blockDelayBalance > 1000.0 && this.flagAndAlert("delay=" + breakDelay + "ms, type=" + String.valueOf(blockBreak.block.getType())) && this.shouldModifyPackets()) {
                blockBreak.cancel();
            }
            this.clampBalance();
        }
        if (blockBreak.action == DiggingAction.FINISHED_DIGGING && this.targetBlockPosition != null) {
            double predictedTime = Math.ceil(1.0 / this.maximumBlockDamage) * 50.0;
            double realTime = System.currentTimeMillis() - this.startBreak;
            double diff = predictedTime - realTime;
            this.clampBalance();
            this.blockBreakBalance = diff < 25.0 ? (this.blockBreakBalance *= 0.9) : (this.blockBreakBalance += diff);
            if (this.blockBreakBalance > 1000.0 && this.flagAndAlert("diff=" + diff + "ms, balance=" + this.blockBreakBalance + "ms, type=" + String.valueOf(blockBreak.block.getType())) && this.shouldModifyPackets()) {
                blockBreak.cancel();
            }
            this.lastFinishBreak = this.startBreak = System.currentTimeMillis();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if ((this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? event.getPacketType() == PacketType.Play.Client.ANIMATION : WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) && this.targetBlockPosition != null) {
            this.maximumBlockDamage = Math.max(this.maximumBlockDamage, BlockBreakSpeed.getBlockDamage(this.player, this.player.compensatedWorld.getBlock(this.targetBlockPosition)));
        }
    }

    private void clampBalance() {
        double balance = Math.max(1000, this.player.getTransactionPing());
        this.blockBreakBalance = GrimMath.clamp(this.blockBreakBalance, -balance, balance);
        this.blockDelayBalance = GrimMath.clamp(this.blockDelayBalance, -balance, balance);
    }
}

