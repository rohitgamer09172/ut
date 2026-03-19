/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ClassToInstanceMap
 *  com.google.common.collect.ImmutableClassToInstanceMap
 *  com.google.common.collect.ImmutableClassToInstanceMap$Builder
 */
package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.checks.impl.aim.AimDuplicateLook;
import ac.grim.grimac.checks.impl.aim.AimModulo360;
import ac.grim.grimac.checks.impl.aim.processor.AimProcessor;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsA;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsB;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsC;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsD;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsE;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsF;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsG;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsH;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsI;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsJ;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsK;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsL;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsM;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsN;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsO;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsP;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsQ;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsR;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsS;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsT;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsU;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsV;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsW;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsX;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsY;
import ac.grim.grimac.checks.impl.badpackets.BadPacketsZ;
import ac.grim.grimac.checks.impl.breaking.AirLiquidBreak;
import ac.grim.grimac.checks.impl.breaking.FarBreak;
import ac.grim.grimac.checks.impl.breaking.FastBreak;
import ac.grim.grimac.checks.impl.breaking.InvalidBreak;
import ac.grim.grimac.checks.impl.breaking.MultiBreak;
import ac.grim.grimac.checks.impl.breaking.NoSwingBreak;
import ac.grim.grimac.checks.impl.breaking.PositionBreakA;
import ac.grim.grimac.checks.impl.breaking.PositionBreakB;
import ac.grim.grimac.checks.impl.breaking.RotationBreak;
import ac.grim.grimac.checks.impl.breaking.WrongBreak;
import ac.grim.grimac.checks.impl.chat.ChatA;
import ac.grim.grimac.checks.impl.chat.ChatB;
import ac.grim.grimac.checks.impl.chat.ChatC;
import ac.grim.grimac.checks.impl.chat.ChatD;
import ac.grim.grimac.checks.impl.combat.Hitboxes;
import ac.grim.grimac.checks.impl.combat.MultiInteractA;
import ac.grim.grimac.checks.impl.combat.MultiInteractB;
import ac.grim.grimac.checks.impl.combat.Reach;
import ac.grim.grimac.checks.impl.crash.CrashA;
import ac.grim.grimac.checks.impl.crash.CrashB;
import ac.grim.grimac.checks.impl.crash.CrashC;
import ac.grim.grimac.checks.impl.crash.CrashD;
import ac.grim.grimac.checks.impl.crash.CrashE;
import ac.grim.grimac.checks.impl.crash.CrashF;
import ac.grim.grimac.checks.impl.crash.CrashG;
import ac.grim.grimac.checks.impl.crash.CrashH;
import ac.grim.grimac.checks.impl.crash.CrashI;
import ac.grim.grimac.checks.impl.elytra.ElytraA;
import ac.grim.grimac.checks.impl.elytra.ElytraB;
import ac.grim.grimac.checks.impl.elytra.ElytraC;
import ac.grim.grimac.checks.impl.elytra.ElytraD;
import ac.grim.grimac.checks.impl.elytra.ElytraE;
import ac.grim.grimac.checks.impl.elytra.ElytraF;
import ac.grim.grimac.checks.impl.elytra.ElytraG;
import ac.grim.grimac.checks.impl.elytra.ElytraH;
import ac.grim.grimac.checks.impl.elytra.ElytraI;
import ac.grim.grimac.checks.impl.exploit.ExploitA;
import ac.grim.grimac.checks.impl.exploit.ExploitB;
import ac.grim.grimac.checks.impl.groundspoof.NoFall;
import ac.grim.grimac.checks.impl.misc.ClientBrand;
import ac.grim.grimac.checks.impl.misc.GhostBlockMitigation;
import ac.grim.grimac.checks.impl.misc.Post;
import ac.grim.grimac.checks.impl.misc.TransactionOrder;
import ac.grim.grimac.checks.impl.movement.NoSlow;
import ac.grim.grimac.checks.impl.movement.PredictionRunner;
import ac.grim.grimac.checks.impl.movement.SetbackBlocker;
import ac.grim.grimac.checks.impl.movement.VehiclePredictionRunner;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsA;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsB;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsC;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsD;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsE;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsF;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsG;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderA;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderB;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderC;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderD;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderE;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderF;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderG;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderH;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderI;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderJ;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderK;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderL;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderM;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderN;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderO;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderProcessor;
import ac.grim.grimac.checks.impl.prediction.DebugHandler;
import ac.grim.grimac.checks.impl.prediction.GroundSpoof;
import ac.grim.grimac.checks.impl.prediction.OffsetHandler;
import ac.grim.grimac.checks.impl.prediction.Phase;
import ac.grim.grimac.checks.impl.scaffolding.AirLiquidPlace;
import ac.grim.grimac.checks.impl.scaffolding.DuplicateRotPlace;
import ac.grim.grimac.checks.impl.scaffolding.FabricatedPlace;
import ac.grim.grimac.checks.impl.scaffolding.FarPlace;
import ac.grim.grimac.checks.impl.scaffolding.InvalidPlaceA;
import ac.grim.grimac.checks.impl.scaffolding.InvalidPlaceB;
import ac.grim.grimac.checks.impl.scaffolding.MultiPlace;
import ac.grim.grimac.checks.impl.scaffolding.PositionPlace;
import ac.grim.grimac.checks.impl.scaffolding.RotationPlace;
import ac.grim.grimac.checks.impl.sprint.SprintA;
import ac.grim.grimac.checks.impl.sprint.SprintB;
import ac.grim.grimac.checks.impl.sprint.SprintC;
import ac.grim.grimac.checks.impl.sprint.SprintD;
import ac.grim.grimac.checks.impl.sprint.SprintE;
import ac.grim.grimac.checks.impl.sprint.SprintF;
import ac.grim.grimac.checks.impl.sprint.SprintG;
import ac.grim.grimac.checks.impl.timer.NegativeTimer;
import ac.grim.grimac.checks.impl.timer.TickTimer;
import ac.grim.grimac.checks.impl.timer.Timer;
import ac.grim.grimac.checks.impl.timer.TimerLimit;
import ac.grim.grimac.checks.impl.timer.VehicleTimer;
import ac.grim.grimac.checks.impl.vehicle.VehicleA;
import ac.grim.grimac.checks.impl.vehicle.VehicleB;
import ac.grim.grimac.checks.impl.vehicle.VehicleC;
import ac.grim.grimac.checks.impl.vehicle.VehicleD;
import ac.grim.grimac.checks.impl.vehicle.VehicleE;
import ac.grim.grimac.checks.impl.vehicle.VehicleF;
import ac.grim.grimac.checks.impl.velocity.ExplosionHandler;
import ac.grim.grimac.checks.impl.velocity.KnockbackHandler;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.checks.type.PositionCheck;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.checks.type.VehicleCheck;
import ac.grim.grimac.events.packets.PacketChangeGameState;
import ac.grim.grimac.events.packets.PacketEntityReplication;
import ac.grim.grimac.events.packets.PacketPlayerAbilities;
import ac.grim.grimac.events.packets.PacketWorldBorder;
import ac.grim.grimac.manager.ActionManager;
import ac.grim.grimac.manager.LastInstanceManager;
import ac.grim.grimac.manager.SetbackTeleportUtil;
import ac.grim.grimac.manager.init.start.SuperDebug;
import ac.grim.grimac.platform.api.permissions.PermissionDefaultValue;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.GhostBlockDetector;
import ac.grim.grimac.predictionengine.SneakingEstimator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;
import ac.grim.grimac.utils.anticheat.update.VehiclePositionUpdate;
import ac.grim.grimac.utils.latency.CompensatedCameraEntity;
import ac.grim.grimac.utils.latency.CompensatedCooldown;
import ac.grim.grimac.utils.latency.CompensatedFireworks;
import ac.grim.grimac.utils.latency.CompensatedInventory;
import ac.grim.grimac.utils.team.TeamHandler;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CheckManager {
    private static final AtomicBoolean initedAtomic = new AtomicBoolean(false);
    private static boolean inited;
    public final ClassToInstanceMap<AbstractCheck> allChecks;
    private final ClassToInstanceMap<PacketCheck> packetChecks;
    private final ClassToInstanceMap<PositionCheck> positionChecks;
    private final ClassToInstanceMap<RotationCheck> rotationChecks;
    private final ClassToInstanceMap<VehicleCheck> vehicleChecks;
    private final ClassToInstanceMap<PacketCheck> prePredictionChecks;
    private final ClassToInstanceMap<BlockBreakCheck> blockBreakChecks;
    private final ClassToInstanceMap<BlockPlaceCheck> blockPlaceChecks;
    private final ClassToInstanceMap<PostPredictionCheck> postPredictionChecks;
    private PacketEntityReplication packetEntityReplication = null;
    private final List<PacketCheck> packetChecksValues;
    private final List<PositionCheck> positionChecksValues;
    private final List<RotationCheck> rotationChecksValues;
    private final List<VehicleCheck> vehicleChecksValues;
    private final List<PacketCheck> prePredictionChecksValues;
    private final List<BlockBreakCheck> blockBreakChecksValues;
    private final List<BlockPlaceCheck> blockPlaceChecksValues;
    private final List<PostPredictionCheck> postPredictionChecksValues;

    public CheckManager(GrimPlayer player) {
        this.packetChecks = new ImmutableClassToInstanceMap.Builder().put(CompensatedCameraEntity.class, (Object)player.cameraEntity).put(PacketOrderProcessor.class, (Object)player.packetOrderProcessor).put(Reach.class, (Object)new Reach(player)).put(PacketEntityReplication.class, (Object)new PacketEntityReplication(player)).put(PacketChangeGameState.class, (Object)new PacketChangeGameState(player)).put(CompensatedInventory.class, (Object)player.inventory).put(PacketPlayerAbilities.class, (Object)new PacketPlayerAbilities(player)).put(PacketWorldBorder.class, (Object)new PacketWorldBorder(player)).put(ActionManager.class, (Object)player.actionManager).put(TeamHandler.class, (Object)new TeamHandler(player)).put(ClientBrand.class, (Object)new ClientBrand(player)).put(NoFall.class, (Object)new NoFall(player)).put(ChatA.class, (Object)new ChatA(player)).put(ChatB.class, (Object)new ChatB(player)).put(ChatC.class, (Object)new ChatC(player)).put(ChatD.class, (Object)new ChatD(player)).put(ExploitA.class, (Object)new ExploitA(player)).put(ExploitB.class, (Object)new ExploitB(player)).put(BadPacketsA.class, (Object)new BadPacketsA(player)).put(BadPacketsB.class, (Object)new BadPacketsB(player)).put(BadPacketsC.class, (Object)new BadPacketsC(player)).put(BadPacketsD.class, (Object)new BadPacketsD(player)).put(BadPacketsE.class, (Object)new BadPacketsE(player)).put(BadPacketsF.class, (Object)new BadPacketsF(player)).put(BadPacketsG.class, (Object)new BadPacketsG(player)).put(BadPacketsI.class, (Object)new BadPacketsI(player)).put(BadPacketsJ.class, (Object)new BadPacketsJ(player)).put(BadPacketsK.class, (Object)new BadPacketsK(player)).put(BadPacketsL.class, (Object)new BadPacketsL(player)).put(BadPacketsM.class, (Object)new BadPacketsM(player)).put(BadPacketsO.class, (Object)new BadPacketsO(player)).put(BadPacketsP.class, (Object)new BadPacketsP(player)).put(BadPacketsQ.class, (Object)new BadPacketsQ(player)).put(BadPacketsR.class, (Object)new BadPacketsR(player)).put(BadPacketsS.class, (Object)new BadPacketsS(player)).put(BadPacketsT.class, (Object)new BadPacketsT(player)).put(BadPacketsU.class, (Object)new BadPacketsU(player)).put(BadPacketsV.class, (Object)new BadPacketsV(player)).put(BadPacketsY.class, (Object)new BadPacketsY(player)).put(BadPacketsZ.class, (Object)new BadPacketsZ(player)).put(MultiActionsA.class, (Object)new MultiActionsA(player)).put(MultiActionsC.class, (Object)new MultiActionsC(player)).put(MultiActionsD.class, (Object)new MultiActionsD(player)).put(MultiActionsE.class, (Object)new MultiActionsE(player)).put(PacketOrderB.class, (Object)new PacketOrderB(player)).put(PacketOrderC.class, (Object)new PacketOrderC(player)).put(PacketOrderD.class, (Object)new PacketOrderD(player)).put(PacketOrderO.class, (Object)new PacketOrderO(player)).put(SprintA.class, (Object)new SprintA(player)).put(VehicleA.class, (Object)new VehicleA(player)).put(VehicleB.class, (Object)new VehicleB(player)).put(VehicleD.class, (Object)new VehicleD(player)).put(VehicleE.class, (Object)new VehicleE(player)).put(VehicleF.class, (Object)new VehicleF(player)).put(CrashB.class, (Object)new CrashB(player)).put(CrashD.class, (Object)new CrashD(player)).put(CrashE.class, (Object)new CrashE(player)).put(CrashF.class, (Object)new CrashF(player)).put(CrashH.class, (Object)new CrashH(player)).put(CrashI.class, (Object)new CrashI(player)).put(SetbackBlocker.class, (Object)new SetbackBlocker(player)).build();
        this.positionChecks = new ImmutableClassToInstanceMap.Builder().put(PredictionRunner.class, (Object)new PredictionRunner(player)).put(CompensatedCooldown.class, (Object)new CompensatedCooldown(player)).build();
        this.rotationChecks = new ImmutableClassToInstanceMap.Builder().put(AimProcessor.class, (Object)new AimProcessor(player)).put(AimModulo360.class, (Object)new AimModulo360(player)).put(AimDuplicateLook.class, (Object)new AimDuplicateLook(player)).build();
        this.vehicleChecks = new ImmutableClassToInstanceMap.Builder().put(VehiclePredictionRunner.class, (Object)new VehiclePredictionRunner(player)).build();
        this.postPredictionChecks = new ImmutableClassToInstanceMap.Builder().put(NegativeTimer.class, (Object)new NegativeTimer(player)).put(ExplosionHandler.class, (Object)new ExplosionHandler(player)).put(KnockbackHandler.class, (Object)new KnockbackHandler(player)).put(GhostBlockDetector.class, (Object)new GhostBlockDetector(player)).put(Phase.class, (Object)new Phase(player)).put(Post.class, (Object)new Post(player)).put(PacketOrderA.class, (Object)new PacketOrderA(player)).put(PacketOrderE.class, (Object)new PacketOrderE(player)).put(PacketOrderF.class, (Object)new PacketOrderF(player)).put(PacketOrderG.class, (Object)new PacketOrderG(player)).put(PacketOrderH.class, (Object)new PacketOrderH(player)).put(PacketOrderI.class, (Object)new PacketOrderI(player)).put(PacketOrderJ.class, (Object)new PacketOrderJ(player)).put(PacketOrderK.class, (Object)new PacketOrderK(player)).put(PacketOrderL.class, (Object)new PacketOrderL(player)).put(PacketOrderM.class, (Object)new PacketOrderM(player)).put(GroundSpoof.class, (Object)new GroundSpoof(player)).put(OffsetHandler.class, (Object)new OffsetHandler(player)).put(SuperDebug.class, (Object)new SuperDebug(player)).put(DebugHandler.class, (Object)new DebugHandler(player)).put(BadPacketsX.class, (Object)new BadPacketsX(player)).put(NoSlow.class, (Object)new NoSlow(player)).put(SprintB.class, (Object)new SprintB(player)).put(SprintC.class, (Object)new SprintC(player)).put(SprintD.class, (Object)new SprintD(player)).put(SprintE.class, (Object)new SprintE(player)).put(SprintF.class, (Object)new SprintF(player)).put(SprintG.class, (Object)new SprintG(player)).put(MultiInteractA.class, (Object)new MultiInteractA(player)).put(MultiInteractB.class, (Object)new MultiInteractB(player)).put(ElytraA.class, (Object)new ElytraA(player)).put(ElytraB.class, (Object)new ElytraB(player)).put(ElytraC.class, (Object)new ElytraC(player)).put(ElytraD.class, (Object)new ElytraD(player)).put(ElytraE.class, (Object)new ElytraE(player)).put(ElytraF.class, (Object)new ElytraF(player)).put(ElytraG.class, (Object)new ElytraG(player)).put(ElytraH.class, (Object)new ElytraH(player)).put(ElytraI.class, (Object)new ElytraI(player)).put(SetbackTeleportUtil.class, (Object)new SetbackTeleportUtil(player)).put(CompensatedFireworks.class, (Object)player.fireworks).put(SneakingEstimator.class, (Object)new SneakingEstimator(player)).put(LastInstanceManager.class, (Object)player.lastInstanceManager).build();
        this.blockPlaceChecks = new ImmutableClassToInstanceMap.Builder().put(InvalidPlaceA.class, (Object)new InvalidPlaceA(player)).put(InvalidPlaceB.class, (Object)new InvalidPlaceB(player)).put(AirLiquidPlace.class, (Object)new AirLiquidPlace(player)).put(MultiPlace.class, (Object)new MultiPlace(player)).put(MultiActionsF.class, (Object)new MultiActionsF(player)).put(MultiActionsG.class, (Object)new MultiActionsG(player)).put(BadPacketsH.class, (Object)new BadPacketsH(player)).put(CrashG.class, (Object)new CrashG(player)).put(FarPlace.class, (Object)new FarPlace(player)).put(FabricatedPlace.class, (Object)new FabricatedPlace(player)).put(PositionPlace.class, (Object)new PositionPlace(player)).put(RotationPlace.class, (Object)new RotationPlace(player)).put(PacketOrderN.class, (Object)new PacketOrderN(player)).put(DuplicateRotPlace.class, (Object)new DuplicateRotPlace(player)).put(GhostBlockMitigation.class, (Object)new GhostBlockMitigation(player)).build();
        this.prePredictionChecks = new ImmutableClassToInstanceMap.Builder().put(Timer.class, (Object)new Timer(player)).put(TickTimer.class, (Object)new TickTimer(player)).put(TimerLimit.class, (Object)new TimerLimit(player)).put(CrashA.class, (Object)new CrashA(player)).put(CrashC.class, (Object)new CrashC(player)).put(VehicleTimer.class, (Object)new VehicleTimer(player)).build();
        this.blockBreakChecks = new ImmutableClassToInstanceMap.Builder().put(AirLiquidBreak.class, (Object)new AirLiquidBreak(player)).put(WrongBreak.class, (Object)new WrongBreak(player)).put(RotationBreak.class, (Object)new RotationBreak(player)).put(FastBreak.class, (Object)new FastBreak(player)).put(MultiBreak.class, (Object)new MultiBreak(player)).put(NoSwingBreak.class, (Object)new NoSwingBreak(player)).put(FarBreak.class, (Object)new FarBreak(player)).put(InvalidBreak.class, (Object)new InvalidBreak(player)).put(PositionBreakA.class, (Object)new PositionBreakA(player)).put(PositionBreakB.class, (Object)new PositionBreakB(player)).put(MultiActionsB.class, (Object)new MultiActionsB(player)).build();
        ImmutableClassToInstanceMap noneModules = new ImmutableClassToInstanceMap.Builder().put(BadPacketsN.class, (Object)new BadPacketsN(player)).put(BadPacketsW.class, (Object)new BadPacketsW(player)).put(TransactionOrder.class, (Object)new TransactionOrder(player)).put(VehicleC.class, (Object)new VehicleC(player)).put(Hitboxes.class, (Object)new Hitboxes(player)).build();
        this.allChecks = new ImmutableClassToInstanceMap.Builder().putAll(this.packetChecks).putAll(this.positionChecks).putAll(this.rotationChecks).putAll(this.vehicleChecks).putAll(this.postPredictionChecks).putAll(this.blockPlaceChecks).putAll(this.prePredictionChecks).putAll(this.blockBreakChecks).putAll((Map)noneModules).build();
        this.packetChecksValues = new ArrayList<PacketCheck>(this.packetChecks.values());
        this.positionChecksValues = new ArrayList<PositionCheck>(this.positionChecks.values());
        this.rotationChecksValues = new ArrayList<RotationCheck>(this.rotationChecks.values());
        this.vehicleChecksValues = new ArrayList<VehicleCheck>(this.vehicleChecks.values());
        this.prePredictionChecksValues = new ArrayList<PacketCheck>(this.prePredictionChecks.values());
        this.blockBreakChecksValues = new ArrayList<BlockBreakCheck>(this.blockBreakChecks.values());
        this.blockPlaceChecksValues = new ArrayList<BlockPlaceCheck>(this.blockPlaceChecks.values());
        this.postPredictionChecksValues = new ArrayList<PostPredictionCheck>(this.postPredictionChecks.values());
        this.init();
    }

    public <T extends AbstractCheck> T getCheck(Class<T> check) {
        return (T)((AbstractCheck)this.allChecks.get(check));
    }

    public <T extends PositionCheck> T getPositionCheck(Class<T> check) {
        return (T)((PositionCheck)this.positionChecks.get(check));
    }

    public <T extends RotationCheck> T getRotationCheck(Class<T> check) {
        return (T)((RotationCheck)this.rotationChecks.get(check));
    }

    public <T extends BlockPlaceCheck> T getBlockPlaceCheck(Class<T> check) {
        return (T)((BlockPlaceCheck)this.blockPlaceChecks.get(check));
    }

    public void onPrePredictionReceivePacket(PacketReceiveEvent packet) {
        for (PacketCheck check : this.prePredictionChecksValues) {
            check.onPacketReceive(packet);
        }
    }

    public void onPacketReceive(PacketReceiveEvent packet) {
        for (PacketCheck packetCheck : this.packetChecksValues) {
            packetCheck.onPacketReceive(packet);
        }
        for (PostPredictionCheck postPredictionCheck : this.postPredictionChecksValues) {
            postPredictionCheck.onPacketReceive(packet);
        }
        for (BlockPlaceCheck blockPlaceCheck : this.blockPlaceChecksValues) {
            blockPlaceCheck.onPacketReceive(packet);
        }
        for (BlockBreakCheck blockBreakCheck : this.blockBreakChecksValues) {
            blockBreakCheck.onPacketReceive(packet);
        }
    }

    public void onPacketSend(PacketSendEvent packet) {
        for (PacketCheck packetCheck : this.prePredictionChecksValues) {
            packetCheck.onPacketSend(packet);
        }
        for (PacketCheck packetCheck : this.packetChecksValues) {
            packetCheck.onPacketSend(packet);
        }
        for (PostPredictionCheck postPredictionCheck : this.postPredictionChecksValues) {
            postPredictionCheck.onPacketSend(packet);
        }
        for (BlockPlaceCheck blockPlaceCheck : this.blockPlaceChecksValues) {
            blockPlaceCheck.onPacketSend(packet);
        }
        for (BlockBreakCheck blockBreakCheck : this.blockBreakChecksValues) {
            blockBreakCheck.onPacketSend(packet);
        }
    }

    public void onPositionUpdate(PositionUpdate position) {
        for (PositionCheck check : this.positionChecksValues) {
            check.onPositionUpdate(position);
        }
    }

    public void onRotationUpdate(RotationUpdate rotation) {
        for (RotationCheck rotationCheck : this.rotationChecksValues) {
            rotationCheck.process(rotation);
        }
        for (BlockPlaceCheck blockPlaceCheck : this.blockPlaceChecksValues) {
            blockPlaceCheck.process(rotation);
        }
    }

    public void onVehiclePositionUpdate(VehiclePositionUpdate update) {
        for (VehicleCheck check : this.vehicleChecksValues) {
            check.process(update);
        }
    }

    public void onPredictionFinish(PredictionComplete complete) {
        for (PostPredictionCheck postPredictionCheck : this.postPredictionChecksValues) {
            postPredictionCheck.onPredictionComplete(complete);
        }
        for (BlockPlaceCheck blockPlaceCheck : this.blockPlaceChecksValues) {
            blockPlaceCheck.onPredictionComplete(complete);
        }
        for (BlockBreakCheck blockBreakCheck : this.blockBreakChecksValues) {
            blockBreakCheck.onPredictionComplete(complete);
        }
    }

    public void onBlockPlace(BlockPlace place) {
        for (BlockPlaceCheck check : this.blockPlaceChecksValues) {
            check.onBlockPlace(place);
        }
    }

    public void onPostFlyingBlockPlace(BlockPlace place) {
        for (BlockPlaceCheck check : this.blockPlaceChecksValues) {
            check.onPostFlyingBlockPlace(place);
        }
    }

    public void onBlockBreak(BlockBreak blockBreak) {
        for (BlockBreakCheck blockBreakCheck : this.blockBreakChecksValues) {
            blockBreakCheck.onBlockBreak(blockBreak);
        }
        for (BlockPlaceCheck blockPlaceCheck : this.blockPlaceChecksValues) {
            blockPlaceCheck.onBlockBreak(blockBreak);
        }
    }

    public void onPostFlyingBlockBreak(BlockBreak blockBreak) {
        for (BlockBreakCheck blockBreakCheck : this.blockBreakChecksValues) {
            blockBreakCheck.onPostFlyingBlockBreak(blockBreak);
        }
        for (BlockPlaceCheck blockPlaceCheck : this.blockPlaceChecksValues) {
            blockPlaceCheck.onPostFlyingBlockBreak(blockBreak);
        }
    }

    public ExplosionHandler getExplosionHandler() {
        return this.getPostPredictionCheck(ExplosionHandler.class);
    }

    public <T extends PacketCheck> T getPacketCheck(Class<T> check) {
        return (T)((PacketCheck)this.packetChecks.get(check));
    }

    public <T extends PacketCheck> T getPrePredictionCheck(Class<T> check) {
        return (T)((PacketCheck)this.prePredictionChecks.get(check));
    }

    public PacketEntityReplication getEntityReplication() {
        if (this.packetEntityReplication == null) {
            this.packetEntityReplication = this.getPacketCheck(PacketEntityReplication.class);
        }
        return this.packetEntityReplication;
    }

    public NoFall getNoFall() {
        return this.getPacketCheck(NoFall.class);
    }

    public KnockbackHandler getKnockbackHandler() {
        return this.getPostPredictionCheck(KnockbackHandler.class);
    }

    public CompensatedCooldown getCompensatedCooldown() {
        return this.getPositionCheck(CompensatedCooldown.class);
    }

    public NoSlow getNoSlow() {
        return this.getPostPredictionCheck(NoSlow.class);
    }

    public SetbackTeleportUtil getSetbackUtil() {
        return this.getPostPredictionCheck(SetbackTeleportUtil.class);
    }

    public DebugHandler getDebugHandler() {
        return this.getPostPredictionCheck(DebugHandler.class);
    }

    public OffsetHandler getOffsetHandler() {
        return this.getPostPredictionCheck(OffsetHandler.class);
    }

    public <T extends PostPredictionCheck> T getPostPredictionCheck(Class<T> check) {
        return (T)((PostPredictionCheck)this.postPredictionChecks.get(check));
    }

    private void init() {
        if (inited || initedAtomic.getAndSet(true)) {
            return;
        }
        inited = true;
        String[] permissions = new String[]{"grim.exempt.", "grim.nosetback.", "grim.nomodifypacket."};
        for (AbstractCheck check : this.allChecks.values()) {
            if (check.getConfigName() == null) continue;
            String id = check.getConfigName().toLowerCase();
            for (String string : permissions) {
                String string2 = string + id;
                GrimAPI.INSTANCE.getPermissionManager().registerPermission(string2, PermissionDefaultValue.FALSE);
            }
        }
    }
}

