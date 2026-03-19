/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.nmsutil.BlockBreakSpeed;
import java.util.ArrayDeque;

@CheckData(name="PacketOrderI", experimental=true)
public class PacketOrderI
extends Check
implements PostPredictionCheck {
    private boolean exemptPlacingWhileDigging;
    private boolean setback;
    private boolean digging;
    private final ArrayDeque<String> flags = new ArrayDeque();

    public PacketOrderI(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        String verbose;
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            if (new WrapperPlayClientInteractEntity(event).getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                if (this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isPicking() || this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isDigging()) {
                    verbose = "type=attack, rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", picking=" + this.player.packetOrderProcessor.isPicking() + ", releasing=" + this.player.packetOrderProcessor.isReleasing() + ", digging=" + this.player.packetOrderProcessor.isDigging();
                    if (!this.player.canSkipTicks()) {
                        if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                            event.setCancelled(true);
                            this.player.onPacketCancel();
                        }
                    } else {
                        this.flags.add(verbose);
                    }
                }
            } else if (this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isDigging()) {
                verbose = "type=interact, releasing=" + this.player.packetOrderProcessor.isReleasing() + ", digging=" + this.player.packetOrderProcessor.isDigging();
                if (!this.player.canSkipTicks()) {
                    if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                        event.setCancelled(true);
                        this.player.onPacketCancel();
                    }
                } else {
                    this.flags.add(verbose);
                }
            }
        }
        if ((event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT || event.getPacketType() == PacketType.Play.Client.USE_ITEM) && (this.player.packetOrderProcessor.isReleasing() || this.digging)) {
            verbose = "type=place/use, releasing=" + this.player.packetOrderProcessor.isReleasing() + ", digging=" + this.digging;
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            } else {
                this.flags.add(verbose);
            }
        }
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);
            switch (packet.getAction()) {
                case RELEASE_USE_ITEM: {
                    if (!this.player.packetOrderProcessor.isAttacking() && !this.player.packetOrderProcessor.isRightClicking() && !this.player.packetOrderProcessor.isPicking() && !this.player.packetOrderProcessor.isDigging()) break;
                    String verbose2 = "type=release, attacking=" + this.player.packetOrderProcessor.isAttacking() + ", rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", picking=" + this.player.packetOrderProcessor.isPicking() + ", digging=" + this.player.packetOrderProcessor.isDigging();
                    if (!this.player.canSkipTicks()) {
                        if (!this.flagAndAlert(verbose2)) break;
                        this.setback = true;
                        break;
                    }
                    this.flags.add(verbose2);
                    this.setback = true;
                    break;
                }
                case START_DIGGING: {
                    double damage = BlockBreakSpeed.getBlockDamage(this.player, this.player.compensatedWorld.getBlock(packet.getBlockPosition()));
                    if (damage >= 1.0 || damage <= 0.0 && this.player.gamemode == GameMode.CREATIVE) {
                        return;
                    }
                }
                case CANCELLED_DIGGING: 
                case FINISHED_DIGGING: {
                    if (this.exemptPlacingWhileDigging || this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
                        return;
                    }
                    this.digging = true;
                }
            }
        }
        if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
            this.digging = false;
        }
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (!this.player.canSkipTicks()) {
            if (this.setback) {
                this.setbackIfAboveSetbackVL();
                this.setback = false;
            }
            return;
        }
        if (this.player.isTickingReliablyFor(3)) {
            for (String verbose : this.flags) {
                if (!this.flagAndAlert(verbose) || !this.setback) continue;
                this.setbackIfAboveSetbackVL();
                this.setback = false;
            }
        }
        this.flags.clear();
        this.setback = false;
    }

    @Override
    public void onReload(ConfigManager config) {
        this.exemptPlacingWhileDigging = config.getBooleanElse(this.getConfigName() + ".exempt-placing-while-digging", false);
    }
}

