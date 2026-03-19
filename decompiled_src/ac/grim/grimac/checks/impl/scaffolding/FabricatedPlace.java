/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.nmsutil.Materials;

@CheckData(name="FabricatedPlace", description="Sent out of bounds cursor position")
public class FabricatedPlace
extends BlockPlaceCheck {
    private static final double MAX_DOUBLE_ERROR = Math.ulp(3.0E7) * 2.0;
    private static final double FLOAT_STEP_AT_ONE = Math.ulp(1.0f);

    public FabricatedPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(BlockPlace place) {
        String debug;
        Vector3f cursor = place.cursor;
        if (cursor == null) {
            return;
        }
        boolean isExtended = Materials.isShapeExceedsCube(place.getPlacedAgainstMaterial()) || place.getPlacedAgainstMaterial() == StateTypes.LECTERN;
        double maxBound = isExtended ? 1.5 : 1.0;
        double minBound = 1.0 - maxBound;
        if ((double)cursor.getX() < minBound - MAX_DOUBLE_ERROR || (double)cursor.getY() < minBound - MAX_DOUBLE_ERROR || (double)cursor.getZ() < minBound - MAX_DOUBLE_ERROR) {
            String debug2 = String.format("cursor=%s limit=%.16f", cursor, minBound - MAX_DOUBLE_ERROR);
            if (this.flagAndAlert(debug2) && this.shouldModifyPackets() && this.shouldCancel()) {
                place.resync();
            }
            return;
        }
        double upperTolerance = FLOAT_STEP_AT_ONE;
        if (((double)cursor.getX() > maxBound + upperTolerance || (double)cursor.getY() > maxBound + upperTolerance || (double)cursor.getZ() > maxBound + upperTolerance) && this.flagAndAlert(debug = String.format("cursor=%s limit=%.16f", cursor, maxBound + upperTolerance)) && this.shouldModifyPackets() && this.shouldCancel()) {
            place.resync();
        }
    }
}

