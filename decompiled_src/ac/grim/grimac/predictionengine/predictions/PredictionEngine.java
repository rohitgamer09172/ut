/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.SneakingEstimator;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWater;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.KnownInput;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vec2;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import ac.grim.grimac.utils.nmsutil.Riptide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PredictionEngine {
    private static final boolean USE_EFFECTS_COMPONENT_EXISTS = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);

    public static Vector3dm clampMovementToHardBorder(GrimPlayer player, Vector3dm outputVel) {
        return outputVel;
    }

    public static Vector3dm transformInputsToVector(GrimPlayer player, Vector3dm theoreticalInput) {
        float bestPossibleZ;
        float bestPossibleX;
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
            Vec2 moveVector = new Vec2((float)theoreticalInput.getX(), (float)theoreticalInput.getZ()).normalized();
            Vec2 input = PredictionEngine.modifyInput(player, moveVector);
            return new Vector3dm(input.x(), 0.0f, input.y());
        }
        if (player.isSlowMovement) {
            bestPossibleX = (float)(theoreticalInput.getX() * (double)player.sneakingSpeedMultiplier);
            bestPossibleZ = (float)(theoreticalInput.getZ() * (double)player.sneakingSpeedMultiplier);
        } else {
            bestPossibleX = Math.min(Math.max(-1.0f, (float)Math.round(theoreticalInput.getX())), 1.0f);
            bestPossibleZ = Math.min(Math.max(-1.0f, (float)Math.round(theoreticalInput.getZ())), 1.0f);
        }
        if (player.packetStateData.isSlowedByUsingItem()) {
            bestPossibleX *= 0.2f;
            bestPossibleZ *= 0.2f;
        }
        Vector3dm inputVector = new Vector3dm(bestPossibleX, 0.0f, bestPossibleZ);
        inputVector.multiply(0.98f);
        inputVector = new Vector3dm((float)inputVector.getX(), (float)inputVector.getY(), (float)inputVector.getZ());
        if (inputVector.lengthSquared() > 1.0) {
            double d0 = Math.sqrt(inputVector.getX() * inputVector.getX() + inputVector.getY() * inputVector.getY() + inputVector.getZ() * inputVector.getZ());
            inputVector = new Vector3dm(inputVector.getX() / d0, inputVector.getY() / d0, inputVector.getZ() / d0);
        }
        return inputVector;
    }

    public static Vec2 modifyInput(GrimPlayer player, Vec2 moveVector) {
        if (moveVector.lengthSquared() == 0.0f) {
            return moveVector;
        }
        Vec2 input = moveVector.scale(0.98f);
        if (player.packetStateData.isSlowedByUsingItem() && !player.inVehicle()) {
            input = input.scale(PredictionEngine.getItemUseSpeedMultiplier(player));
        }
        if (player.isSlowMovement) {
            input = input.scale(player.sneakingSpeedMultiplier);
        }
        return PredictionEngine.modifyInputSpeedForSquareMovement(input);
    }

    private static Vec2 modifyInputSpeedForSquareMovement(Vec2 input) {
        float length = input.length();
        if (length <= 0.0f) {
            return input;
        }
        Vec2 multiplied = input.scale(1.0f / length);
        float distance = PredictionEngine.distanceToUnitSquare(multiplied);
        float min = Math.min(length * distance, 1.0f);
        return multiplied.scale(min);
    }

    private static float distanceToUnitSquare(Vec2 input) {
        float x = Math.abs(input.x());
        float z = Math.abs(input.y());
        float additional = z > x ? x / z : z / x;
        return GrimMath.sqrt(1.0f + GrimMath.square(additional));
    }

    private static float getItemUseSpeedMultiplier(GrimPlayer player) {
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21_11) || !USE_EFFECTS_COMPONENT_EXISTS) {
            return 0.2f;
        }
        ItemStack itemInHand = player.inventory.getItemInHand(player.packetStateData.itemInUseHand);
        ItemUseEffects useEffects = itemInHand.getComponentOr(ComponentTypes.USE_EFFECTS, null);
        return useEffects == null ? 0.2f : useEffects.getSpeedMultiplier();
    }

    public void guessBestMovement(float speed, GrimPlayer player) {
        Set<VectorData> init = this.fetchPossibleStartTickVectors(player);
        if (player.uncertaintyHandler.influencedByBouncyBlock()) {
            for (VectorData data : init) {
                Vector3dm toZeroVec = new PredictionEngine().handleStartingVelocityUncertainty(player, data, new Vector3dm(0, -1000000000, 0));
                player.uncertaintyHandler.nextTickSlimeBlockUncertainty = Math.max(Math.abs(toZeroVec.getY()), player.uncertaintyHandler.nextTickSlimeBlockUncertainty);
            }
        }
        player.updateVelocityMovementSkipping();
        player.couldSkipTick = player.couldSkipTick || player.pointThreeEstimator.determineCanSkipTick(speed, init);
        List<VectorData> possibleVelocities = this.applyInputsToVelocityPossibilities(player, init, speed);
        if (player.couldSkipTick) {
            this.addZeroPointThreeToPossibilities(speed, player, possibleVelocities);
        }
        this.doPredictions(player, possibleVelocities, speed);
        new MovementTickerPlayer(player).move(player.clientVelocity.clone(), player.predictedVelocity.vector);
        this.endOfTick(player, player.gravity);
    }

    private void doPredictions(GrimPlayer player, List<VectorData> possibleVelocities, float speed) {
        possibleVelocities.sort((a, b) -> this.sortVectorData((VectorData)a, (VectorData)b, player));
        player.checkManager.getPostPredictionCheck(SneakingEstimator.class).storePossibleVelocities(possibleVelocities);
        double bestInput = Double.MAX_VALUE;
        VectorData bestCollisionVel = null;
        Vector3dm beforeCollisionMovement = null;
        Vector3dm originalClientVel = player.clientVelocity.clone();
        SimpleCollisionBox originalBB = player.boundingBox;
        SimpleCollisionBox pointThreeThanksMojang = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) ? GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, player.lastY, player.lastZ, 0.6f, 0.6f) : originalBB;
        player.skippedTickInActualMovement = false;
        for (VectorData clientVelAfterInput : possibleVelocities) {
            Vector3dm primaryPushMovement = this.handleStartingVelocityUncertainty(player, clientVelAfterInput, player.actualMovement);
            Vector3dm bestTheoreticalCollisionResult = VectorUtils.cutBoxToVector(player.actualMovement, new SimpleCollisionBox(0.0, Math.min(0.0, primaryPushMovement.getY()), 0.0, primaryPushMovement.getX(), Math.max(0.6, primaryPushMovement.getY()), primaryPushMovement.getZ()).sort());
            if (bestTheoreticalCollisionResult.distanceSquared(player.actualMovement) > bestInput && !clientVelAfterInput.isKnockback() && !clientVelAfterInput.isExplosion()) continue;
            player.boundingBox = clientVelAfterInput.isZeroPointZeroThree() ? pointThreeThanksMojang : originalBB;
            Pair<Vector3dm, Vector3dm> output = this.doSeekingWallCollisions(player, primaryPushMovement, originalClientVel, clientVelAfterInput);
            primaryPushMovement = output.first();
            Vector3dm outputVel = PredictionEngine.clampMovementToHardBorder(player, output.second());
            double resultAccuracy = outputVel.distanceSquared(player.actualMovement);
            if (clientVelAfterInput.isZeroPointZeroThree() && resultAccuracy < 1.0E-6) {
                player.skippedTickInActualMovement = true;
            }
            if (clientVelAfterInput.isKnockback()) {
                player.checkManager.getKnockbackHandler().handlePredictionAnalysis(Math.sqrt(player.uncertaintyHandler.reduceOffset(resultAccuracy)));
            }
            if (clientVelAfterInput.isExplosion()) {
                player.checkManager.getExplosionHandler().handlePredictionAnalysis(Math.sqrt(player.uncertaintyHandler.reduceOffset(resultAccuracy)));
            }
            if ((clientVelAfterInput.isKnockback() || clientVelAfterInput.isExplosion()) && !clientVelAfterInput.isZeroPointZeroThree()) {
                boolean wasVelocityPointThree = player.pointThreeEstimator.determineCanSkipTick(speed, new HashSet<VectorData>(Collections.singletonList(clientVelAfterInput)));
                if (clientVelAfterInput.isKnockback()) {
                    player.checkManager.getKnockbackHandler().setPointThree(wasVelocityPointThree);
                }
                if (clientVelAfterInput.isExplosion()) {
                    player.checkManager.getExplosionHandler().setPointThree(wasVelocityPointThree);
                }
            }
            if (player.packetStateData.isSlowedByUsingItem() && !clientVelAfterInput.isFlipItem()) {
                player.checkManager.getNoSlow().handlePredictionAnalysis(Math.sqrt(player.uncertaintyHandler.reduceOffset(resultAccuracy)));
            }
            if (player.checkManager.getKnockbackHandler().shouldIgnoreForPrediction(clientVelAfterInput) || player.checkManager.getExplosionHandler().shouldIgnoreForPrediction(clientVelAfterInput)) continue;
            if (resultAccuracy < bestInput) {
                bestCollisionVel = clientVelAfterInput.returnNewModified(outputVel, VectorData.VectorType.BestVelPicked);
                bestCollisionVel.preUncertainty = clientVelAfterInput;
                beforeCollisionMovement = primaryPushMovement;
                if (player.wouldCollisionResultFlagGroundSpoof(primaryPushMovement.getY(), bestCollisionVel.vector.getY())) {
                    resultAccuracy += 1.0E-8;
                }
                bestInput = resultAccuracy;
            }
            if (!(bestInput < 1.0000000000000002E-10) || player.checkManager.getKnockbackHandler().wouldFlag() || player.checkManager.getExplosionHandler().wouldFlag()) continue;
            break;
        }
        assert (beforeCollisionMovement != null);
        player.clientVelocity = beforeCollisionMovement.clone();
        player.predictedVelocity = bestCollisionVel;
        player.boundingBox = originalBB;
        if (player.predictedVelocity.isZeroPointZeroThree()) {
            player.skippedTickInActualMovement = true;
        }
    }

    private Pair<Vector3dm, Vector3dm> doSeekingWallCollisions(GrimPlayer player, Vector3dm primaryPushMovement, Vector3dm originalClientVel, VectorData clientVelAfterInput) {
        double testZ;
        double testY;
        Vector3dm outputVel;
        boolean vehicleKB = player.inVehicle() && clientVelAfterInput.isKnockback() && clientVelAfterInput.vector.getY() == 0.0;
        double xAdditional = Math.signum(primaryPushMovement.getX()) * 1.0E-7;
        double yAdditional = vehicleKB ? 0.0 : (double)(primaryPushMovement.getY() > 0.0 ? 1 : -1) * 1.0E-7;
        double zAdditional = Math.signum(primaryPushMovement.getZ()) * 1.0E-7;
        double testX = primaryPushMovement.getX() + xAdditional;
        if (testX == (outputVel = Collisions.collide(player, (primaryPushMovement = new Vector3dm(testX, testY = primaryPushMovement.getY() + yAdditional, testZ = primaryPushMovement.getZ() + zAdditional)).getX(), primaryPushMovement.getY(), primaryPushMovement.getZ(), originalClientVel.getY(), clientVelAfterInput)).getX()) {
            primaryPushMovement.setX(primaryPushMovement.getX() - xAdditional);
            outputVel.setX(outputVel.getX() - xAdditional);
        }
        if (testY == outputVel.getY()) {
            primaryPushMovement.setY(primaryPushMovement.getY() - yAdditional);
            outputVel.setY(outputVel.getY() - yAdditional);
        }
        if (testZ == outputVel.getZ()) {
            primaryPushMovement.setZ(primaryPushMovement.getZ() - zAdditional);
            outputVel.setZ(outputVel.getZ() - zAdditional);
        }
        return new Pair<Vector3dm, Vector3dm>(primaryPushMovement, outputVel);
    }

    private void addZeroPointThreeToPossibilities(float speed, GrimPlayer player, List<VectorData> possibleVelocities) {
        Set<VectorData> pointThreePossibilities = new HashSet<VectorData>();
        Vector3dm pointThreeVector = new Vector3dm();
        if (!player.pointThreeEstimator.controlsVerticalMovement()) {
            pointThreeVector.setY(player.clientVelocity.getY());
        } else {
            pointThreePossibilities.add(new VectorData(new Vector3dm(0.0, player.clientVelocity.getY(), 0.0), VectorData.VectorType.ZeroPointZeroThree));
        }
        pointThreePossibilities.add(new VectorData(pointThreeVector, VectorData.VectorType.ZeroPointZeroThree));
        if (player.pointThreeEstimator.isNearFluid && !Collisions.isEmpty(player, player.boundingBox.copy().expand(0.4, 0.0, 0.4)) && !player.onGround) {
            pointThreePossibilities.add(new VectorData(new Vector3dm(0.0, 0.3, 0.0), VectorData.VectorType.ZeroPointZeroThree));
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && player.isSwimming) {
            pointThreePossibilities = PredictionEngineWater.transformSwimmingVectors(player, pointThreePossibilities);
        }
        if (player.pointThreeEstimator.isNearClimbable() && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) || !Collisions.isEmpty(player, player.boundingBox.copy().expand(player.clientVelocity.getX(), 0.0, player.clientVelocity.getZ()).expand(0.5, -1.0E-7, 0.5)))) {
            Vector3dm hackyClimbVector = new Vector3dm(0.0, 0.2, 0.0);
            PredictionEngineNormal.staticVectorEndOfTick(player, hackyClimbVector);
            pointThreePossibilities.add(new VectorData(hackyClimbVector, VectorData.VectorType.ZeroPointZeroThree));
        }
        this.addJumpsToPossibilities(player, pointThreePossibilities);
        this.addExplosionToPossibilities(player, pointThreePossibilities);
        if (player.packetStateData.tryingToRiptide) {
            Vector3dm riptideAddition = Riptide.getRiptideVelocity(player);
            pointThreePossibilities.add(new VectorData(player.clientVelocity.clone().add(riptideAddition), new VectorData(new Vector3dm(), VectorData.VectorType.ZeroPointZeroThree), VectorData.VectorType.Trident));
        }
        possibleVelocities.addAll(this.applyInputsToVelocityPossibilities(player, pointThreePossibilities, speed));
    }

    public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        ArrayList<VectorData> returnVectors = new ArrayList<VectorData>();
        this.loopVectors(player, possibleVectors, speed, returnVectors);
        return returnVectors;
    }

    public void addFluidPushingToStartingVectors(GrimPlayer player, Set<VectorData> data) {
        for (VectorData vectorData : data) {
            if (vectorData.isKnockback() && player.baseTickAddition.lengthSquared() != 0.0) {
                vectorData.vector = vectorData.vector.add(player.baseTickAddition);
            }
            if (!vectorData.isKnockback() || player.baseTickWaterPushing.lengthSquared() == 0.0) continue;
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
                Vector3dm vec3 = player.baseTickWaterPushing.clone();
                if (Math.abs(vectorData.vector.getX()) < 0.003 && Math.abs(vectorData.vector.getZ()) < 0.003 && player.baseTickWaterPushing.length() < 0.0045000000000000005) {
                    vec3 = vec3.normalize().multiply(0.0045000000000000005);
                }
                vectorData.vector = vectorData.vector.add(vec3);
                continue;
            }
            vectorData.vector = vectorData.vector.add(player.baseTickWaterPushing);
        }
    }

    public Set<VectorData> fetchPossibleStartTickVectors(GrimPlayer player) {
        Set<VectorData> velocities = player.getPossibleVelocities();
        this.addExplosionToPossibilities(player, velocities);
        if (player.packetStateData.tryingToRiptide) {
            Vector3dm riptideAddition = Riptide.getRiptideVelocity(player);
            velocities.add(new VectorData(player.clientVelocity.clone().add(riptideAddition), VectorData.VectorType.Trident));
        }
        this.addFluidPushingToStartingVectors(player, velocities);
        this.addAttackSlowToPossibilities(player, velocities);
        this.addNonEffectiveAI(player, velocities);
        this.applyMovementThreshold(player, velocities);
        this.addJumpsToPossibilities(player, velocities);
        return velocities;
    }

    private void addNonEffectiveAI(GrimPlayer player, Set<VectorData> data) {
        if (!player.inVehicle() || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
            return;
        }
        for (VectorData vectorData : data) {
            vectorData.vector = vectorData.vector.clone().multiply(0.98);
        }
    }

    private void addAttackSlowToPossibilities(GrimPlayer player, Set<VectorData> velocities) {
        for (int x = 1; x <= Math.min(player.maxAttackSlow, 5); ++x) {
            for (VectorData data : new HashSet<VectorData>(velocities)) {
                if (player.minAttackSlow > 0) {
                    data.vector.setX(data.vector.getX() * 0.6);
                    data.vector.setZ(data.vector.getZ() * 0.6);
                    data.addVectorType(VectorData.VectorType.AttackSlow);
                    continue;
                }
                velocities.add(data.returnNewModified(data.vector.clone().multiply(0.6, 1.0, 0.6), VectorData.VectorType.AttackSlow));
            }
            if (player.minAttackSlow <= 0) continue;
            --player.minAttackSlow;
        }
    }

    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
    }

    public void applyMovementThreshold(GrimPlayer player, Set<VectorData> velocities) {
        double minimumMovement = 0.003;
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
            minimumMovement = 0.005;
        }
        boolean stupidVectors = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5) && !player.inVehicle();
        boolean stuckOnEdge = player.uncertaintyHandler.stuckOnEdge.hasOccurredSince(2);
        HashSet<VectorData> vectors = stupidVectors && stuckOnEdge ? new HashSet<VectorData>(velocities) : velocities;
        for (VectorData vector : vectors) {
            if (stupidVectors) {
                if (Collisions.getHorizontalDistanceSqr(vector.vector) < 9.0E-6) {
                    if (stuckOnEdge) {
                        VectorData edgeVector = vector.returnNewModified(vector.vector.clone(), vector.vectorType);
                        if (Math.abs(edgeVector.vector.getY()) < minimumMovement) {
                            edgeVector.vector.setY(0.0);
                        }
                        velocities.add(edgeVector);
                    }
                    vector.vector.setX(0.0);
                    vector.vector.setZ(0.0);
                }
            } else {
                if (Math.abs(vector.vector.getX()) < minimumMovement) {
                    vector.vector.setX(0.0);
                }
                if (Math.abs(vector.vector.getZ()) < minimumMovement) {
                    vector.vector.setZ(0.0);
                }
            }
            if (!(Math.abs(vector.vector.getY()) < minimumMovement)) continue;
            vector.vector.setY(0.0);
        }
    }

    public void addExplosionToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        if (player.likelyExplosions == null && player.firstBreadExplosion == null) {
            return;
        }
        for (VectorData vector : new HashSet<VectorData>(existingVelocities)) {
            if (player.likelyExplosions != null) {
                existingVelocities.add(new VectorData(vector.vector.clone().add(player.likelyExplosions.vector), vector, VectorData.VectorType.Explosion));
            }
            if (player.firstBreadExplosion == null) continue;
            existingVelocities.add(new VectorData(vector.vector.clone().add(player.firstBreadExplosion.vector), vector, VectorData.VectorType.Explosion).returnNewModified(vector.vector.clone().add(player.firstBreadExplosion.vector), VectorData.VectorType.FirstBreadExplosion));
        }
    }

    public int sortVectorData(VectorData a, VectorData b, GrimPlayer player) {
        int aScore = 0;
        int bScore = 0;
        if (a.isExplosion()) {
            aScore -= 5;
        }
        if (a.isKnockback()) {
            aScore -= 5;
        }
        if (b.isExplosion()) {
            bScore -= 5;
        }
        if (b.isKnockback()) {
            bScore -= 5;
        }
        if (a.isFirstBreadExplosion()) {
            ++aScore;
        }
        if (b.isFirstBreadExplosion()) {
            ++bScore;
        }
        if (a.isFirstBreadKb()) {
            ++aScore;
        }
        if (b.isFirstBreadKb()) {
            ++bScore;
        }
        if (a.isFlipItem()) {
            aScore += 3;
        }
        if (b.isFlipItem()) {
            bScore += 3;
        }
        if (a.isZeroPointZeroThree()) {
            --aScore;
        }
        if (b.isZeroPointZeroThree()) {
            --bScore;
        }
        if ((player.inVehicle() ? player.clientControlledVerticalCollision : player.onGround) && a.vector.getY() >= 0.0) {
            aScore += 2;
        }
        if ((player.inVehicle() ? player.clientControlledVerticalCollision : player.onGround) && b.vector.getY() >= 0.0) {
            bScore += 2;
        }
        if (aScore != bScore) {
            return Integer.compare(aScore, bScore);
        }
        return Double.compare(a.vector.distanceSquared(player.actualMovement), b.vector.distanceSquared(player.actualMovement));
    }

    public Vector3dm handleStartingVelocityUncertainty(GrimPlayer player, VectorData vector, Vector3dm targetVec) {
        SimpleCollisionBox rod;
        double gravityOffset;
        double avgColliding = Collections.max(player.uncertaintyHandler.collidingEntities).intValue();
        double additionHorizontal = player.uncertaintyHandler.getOffsetHorizontal(vector);
        double additionVertical = player.uncertaintyHandler.getVerticalOffset(vector);
        double pistonX = Collections.max(player.uncertaintyHandler.pistonX);
        double pistonY = Collections.max(player.uncertaintyHandler.pistonY);
        double pistonZ = Collections.max(player.uncertaintyHandler.pistonZ);
        additionHorizontal += player.uncertaintyHandler.lastHorizontalOffset;
        additionVertical += player.uncertaintyHandler.lastVerticalOffset;
        VectorData originalVec = vector;
        while (originalVec.lastVector != null) {
            originalVec = originalVec.lastVector;
        }
        double bonusY = 0.0;
        if (player.uncertaintyHandler.lastFlyingStatusChange.hasOccurredSince(4)) {
            additionHorizontal += 0.3;
            bonusY += 0.3;
        }
        if (player.uncertaintyHandler.lastUnderwaterFlyingHack.hasOccurredSince(9)) {
            bonusY += 0.2;
        }
        if (player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(2)) {
            additionHorizontal += 0.1;
            bonusY += 0.1;
        }
        if (pistonX != 0.0 || pistonY != 0.0 || pistonZ != 0.0) {
            additionHorizontal += 0.1;
            bonusY += 0.1;
        }
        double horizontalFluid = player.pointThreeEstimator.getHorizontalFluidPushingUncertainty(vector);
        Vector3dm uncertainty = new Vector3dm(avgColliding * 0.08, additionVertical, avgColliding * 0.08);
        Vector3dm min = new Vector3dm(player.uncertaintyHandler.xNegativeUncertainty - (additionHorizontal += horizontalFluid), -bonusY + player.uncertaintyHandler.yNegativeUncertainty, player.uncertaintyHandler.zNegativeUncertainty - additionHorizontal);
        Vector3dm max = new Vector3dm(player.uncertaintyHandler.xPositiveUncertainty + additionHorizontal, bonusY + player.uncertaintyHandler.yPositiveUncertainty, player.uncertaintyHandler.zPositiveUncertainty + additionHorizontal);
        Vector3dm minVector = vector.vector.clone().add(min.subtract(uncertainty));
        Vector3dm maxVector = vector.vector.clone().add(max.add(uncertainty));
        if (player.uncertaintyHandler.onGroundUncertain && vector.vector.getY() < 0.0) {
            maxVector.setY(0);
        }
        if ((gravityOffset = player.pointThreeEstimator.getAdditionalVerticalUncertainty(vector)) > 0.0) {
            maxVector.setY(maxVector.getY() + gravityOffset);
        } else {
            minVector.setY(minVector.getY() + gravityOffset);
        }
        double verticalFluid = player.pointThreeEstimator.getVerticalFluidPushingUncertainty(vector);
        minVector.setY(minVector.getY() - verticalFluid);
        double bubbleFluid = player.pointThreeEstimator.getVerticalBubbleUncertainty(vector);
        maxVector.setY(maxVector.getY() + bubbleFluid);
        minVector.setY(minVector.getY() - bubbleFluid);
        if (!player.pointThreeEstimator.canPredictNextVerticalMovement()) {
            minVector.setY(minVector.getY() - player.compensatedEntities.self.getAttributeValue(Attributes.GRAVITY));
        }
        if (player.actualMovement.getY() >= 0.0 && player.uncertaintyHandler.influencedByBouncyBlock() && player.uncertaintyHandler.thisTickSlimeBlockUncertainty != 0.0 && !vector.isJump()) {
            if (player.uncertaintyHandler.thisTickSlimeBlockUncertainty > maxVector.getY()) {
                maxVector.setY(player.uncertaintyHandler.thisTickSlimeBlockUncertainty);
            }
            if (minVector.getY() > 0.0) {
                minVector.setY(0);
            }
        }
        if (vector.isZeroPointZeroThree() && vector.isSwimHop()) {
            minVector.setY(minVector.getY() - 0.06);
        }
        SimpleCollisionBox box = new SimpleCollisionBox(minVector, maxVector);
        box.sort();
        double levitation = player.pointThreeEstimator.positiveLevitation(maxVector.getY());
        box.combineToMinimum(box.minX, levitation, box.minZ);
        levitation = player.pointThreeEstimator.positiveLevitation(minVector.getY());
        box.combineToMinimum(box.minX, levitation, box.minZ);
        levitation = player.pointThreeEstimator.negativeLevitation(maxVector.getY());
        box.combineToMinimum(box.minX, levitation, box.minZ);
        levitation = player.pointThreeEstimator.negativeLevitation(minVector.getY());
        box.combineToMinimum(box.minX, levitation, box.minZ);
        SneakingEstimator sneaking = player.checkManager.getPostPredictionCheck(SneakingEstimator.class);
        box.minX += sneaking.getSneakingPotentialHiddenVelocity().minX;
        box.minZ += sneaking.getSneakingPotentialHiddenVelocity().minZ;
        box.maxX += sneaking.getSneakingPotentialHiddenVelocity().maxX;
        box.maxZ += sneaking.getSneakingPotentialHiddenVelocity().maxZ;
        if (player.uncertaintyHandler.fireworksBox != null) {
            double minXdiff = Math.min(0.0, player.uncertaintyHandler.fireworksBox.minX - originalVec.vector.getX());
            double minYdiff = Math.min(0.0, player.uncertaintyHandler.fireworksBox.minY - originalVec.vector.getY());
            double minZdiff = Math.min(0.0, player.uncertaintyHandler.fireworksBox.minZ - originalVec.vector.getZ());
            double maxXdiff = Math.max(0.0, player.uncertaintyHandler.fireworksBox.maxX - originalVec.vector.getX());
            double maxYdiff = Math.max(0.0, player.uncertaintyHandler.fireworksBox.maxY - originalVec.vector.getY());
            double maxZdiff = Math.max(0.0, player.uncertaintyHandler.fireworksBox.maxZ - originalVec.vector.getZ());
            box.expandMin(minXdiff, minYdiff, minZdiff);
            box.expandMax(maxXdiff, maxYdiff, maxZdiff);
        }
        if ((rod = player.uncertaintyHandler.fishingRodPullBox) != null) {
            box.expandMin(rod.minX, rod.minY, rod.minZ);
            box.expandMax(rod.maxX, rod.maxY, rod.maxZ);
        }
        if (player.uncertaintyHandler.stuckOnEdge.hasOccurredSince(0) || player.uncertaintyHandler.isSteppingOnSlime) {
            box.expandToAbsoluteCoordinates(0.0, box.maxY, 0.0);
        }
        if (player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(0) || player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(3) || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && vector.vector.getY() > 0.0 && vector.isZeroPointZeroThree() && !Collisions.isEmpty(player, GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, vector.vector.getY() + player.lastY + 0.6, player.lastZ, 0.6f, 1.26f))) {
            box.expandToAbsoluteCoordinates(0.0, 0.0, 0.0);
        }
        if (player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(1)) {
            double trueFriction;
            double d = trueFriction = player.lastOnGround ? (double)player.friction * 0.91 : 0.91;
            if (player.wasTouchingLava) {
                trueFriction = 0.5;
            }
            if (player.wasTouchingWater) {
                trueFriction = 0.96;
            }
            double maxY = Math.max(box.maxY, box.maxY + (box.maxY - player.gravity) * 0.91);
            double minY = Math.min(box.minY, box.minY + (box.minY - player.gravity) * 0.91);
            double minX = Math.min(box.minX, box.minX + -player.speed * trueFriction);
            double minZ = Math.min(box.minZ, box.minZ + -player.speed * trueFriction);
            double maxX = Math.max(box.maxX, box.maxX + player.speed * trueFriction);
            double maxZ = Math.max(box.maxZ, box.maxZ + player.speed * trueFriction);
            box = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ);
            box.expand(0.05, 0.0, 0.05);
        }
        if (player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(10)) {
            box.expand(0.001);
        }
        minVector = box.min();
        maxVector = box.max();
        if (pistonX != 0.0) {
            minVector.setX(Math.min(minVector.getX() - pistonX, pistonX));
            maxVector.setX(Math.max(maxVector.getX() + pistonX, pistonX));
        }
        if (pistonY != 0.0) {
            minVector.setY(Math.min(minVector.getY() - pistonY, pistonY));
            maxVector.setY(Math.max(maxVector.getY() + pistonY, pistonY));
        }
        if (pistonZ != 0.0) {
            minVector.setZ(Math.min(minVector.getZ() - pistonZ, pistonZ));
            maxVector.setZ(Math.max(maxVector.getZ() + pistonZ, pistonZ));
        }
        return VectorUtils.cutBoxToVector(targetVec, minVector, maxVector);
    }

    public void endOfTick(GrimPlayer player, double d) {
        player.canSwimHop = this.canSwimHop(player);
        player.lastWasClimbing = 0.0;
    }

    private void loopVectors(GrimPlayer player, Set<VectorData> possibleVectors, float speed, List<VectorData> returnVectors) {
        int forwardMin = player.isSprinting && !player.isSwimming ? 1 : -1;
        int forwardMax = 1;
        int strafeMin = -1;
        int strafeMax = 1;
        if (player.supportsEndTick()) {
            strafeMax = 0;
            strafeMin = 0;
            forwardMax = 0;
            forwardMin = 0;
            KnownInput knownInput = player.packetStateData.knownInput;
            if (knownInput.forward() || player.isSprinting && !player.isSwimming) {
                ++forwardMax;
                ++forwardMin;
            }
            if (knownInput.backward() && (!player.isSprinting || player.isSwimming)) {
                --forwardMax;
                --forwardMin;
            }
            if (knownInput.left()) {
                ++strafeMax;
                ++strafeMin;
            }
            if (knownInput.right()) {
                --strafeMax;
                --strafeMin;
            }
        }
        for (int loopSlowed = 0; loopSlowed <= 1; ++loopSlowed) {
            for (int loopUsingItem = 0; loopUsingItem <= 1; ++loopUsingItem) {
                for (VectorData possibleLastTickOutput : possibleVectors) {
                    if (loopSlowed == 1 && !possibleLastTickOutput.isZeroPointZeroThree() && player.isForceSlowMovement()) continue;
                    for (int strafe = strafeMin; strafe <= strafeMax; ++strafe) {
                        for (int forward = forwardMin; forward <= forwardMax; ++forward) {
                            for (int applyStuckSpeed = 1; !(applyStuckSpeed < 0 || applyStuckSpeed == 0 && player.isForceStuckSpeed()); --applyStuckSpeed) {
                                Vector3dm input = PredictionEngine.transformInputsToVector(player, new Vector3dm(strafe, 0, forward));
                                VectorData result = new VectorData(possibleLastTickOutput.vector.clone().add(this.getMovementResultFromInput(player, input, speed, player.yaw)), possibleLastTickOutput, VectorData.VectorType.InputResult);
                                result.input = input;
                                if (applyStuckSpeed != 0) {
                                    result = result.returnNewModified(result.vector.clone().multiply(player.stuckSpeedMultiplier), VectorData.VectorType.StuckMultiplier);
                                }
                                result = result.returnNewModified(this.handleOnClimbable(result.vector.clone(), player), VectorData.VectorType.Climbable);
                                if (loopUsingItem == 1) {
                                    result = result.returnNewModified(VectorData.VectorType.Flip_Use_Item);
                                }
                                returnVectors.add(result);
                            }
                        }
                    }
                }
                player.packetStateData.setSlowedByUsingItem(!player.packetStateData.isSlowedByUsingItem());
            }
            player.isSlowMovement = !player.isSlowMovement;
        }
    }

    public boolean canSwimHop(GrimPlayer player) {
        SimpleCollisionBox oldBox;
        if (player.inVehicle() && player.compensatedEntities.self.getRiding().isBoat) {
            return false;
        }
        SimpleCollisionBox simpleCollisionBox = oldBox = player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ) : GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, player.lastY, player.lastZ, 0.6f, 1.8f);
        if (!player.compensatedWorld.containsLiquid(oldBox.expand(0.1, 0.1, 0.1))) {
            return false;
        }
        SimpleCollisionBox oldBB = player.boundingBox;
        player.boundingBox = player.boundingBox.copy().expand(-player.getMovementThreshold(), 0.0, -player.getMovementThreshold());
        double pointThreeToGround = Collisions.collide(player, 0.0, -player.getMovementThreshold(), 0.0).getY() + 1.0E-7;
        player.boundingBox = oldBB;
        SimpleCollisionBox newBox = player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z) : GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.x, player.y, player.z, 0.6f, 1.8f);
        return player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(3) || !Collisions.isEmpty(player, newBox.expand(player.clientVelocity.getX(), -1.0 * pointThreeToGround, player.clientVelocity.getZ()).expand(0.5, 0.03, 0.5));
    }

    public Vector3dm getMovementResultFromInput(GrimPlayer player, Vector3dm inputVector, float f, float f2) {
        float f2InRadians = GrimMath.radians(f2);
        float f3 = player.trigHandler.sin(f2InRadians);
        float f4 = player.trigHandler.cos(f2InRadians);
        double xResult = inputVector.getX() * (double)f4 - inputVector.getZ() * (double)f3;
        double zResult = inputVector.getZ() * (double)f4 + inputVector.getX() * (double)f3;
        return new Vector3dm(xResult * (double)f, 0.0, zResult * (double)f);
    }

    public Vector3dm handleOnClimbable(Vector3dm vector, GrimPlayer player) {
        return vector;
    }

    public void doJump(GrimPlayer player, Vector3dm vector) {
        if (!player.lastOnGround || player.onGround) {
            return;
        }
        JumpPower.jumpFromGround(player, vector);
    }
}

