/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;

public class PistonHeadCollision
implements CollisionFactory {
    @Override
    public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        double longAmount;
        double d = longAmount = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && block.isShort() ? 0.0 : 4.0;
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
            longAmount = 4.0;
        }
        if (version.isOlderThan(ClientVersion.V_1_9) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
            longAmount = 0.0;
        }
        return switch (block.getFacing()) {
            case BlockFace.UP -> new ComplexCollisionBox(2, new HexCollisionBox(0.0, 12.0, 0.0, 16.0, 16.0, 16.0), new HexCollisionBox(6.0, 0.0 - longAmount, 6.0, 10.0, 12.0, 10.0));
            case BlockFace.NORTH -> new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, 4.0), new HexCollisionBox(6.0, 6.0, 4.0, 10.0, 10.0, 16.0 + longAmount));
            case BlockFace.SOUTH -> {
                if (version.isOlderThanOrEquals(ClientVersion.V_1_8)) {
                    yield new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 12.0, 16.0, 16.0, 16.0), new HexCollisionBox(4.0, 6.0, 0.0, 12.0, 10.0, 12.0));
                }
                yield new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 12.0, 16.0, 16.0, 16.0), new HexCollisionBox(6.0, 6.0, 0.0 - longAmount, 10.0, 10.0, 12.0));
            }
            case BlockFace.WEST -> {
                if (version.isOlderThanOrEquals(ClientVersion.V_1_8)) {
                    yield new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 0.0, 4.0, 16.0, 16.0), new HexCollisionBox(6.0, 4.0, 4.0, 10.0, 12.0, 16.0));
                }
                yield new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 0.0, 4.0, 16.0, 16.0), new HexCollisionBox(4.0, 6.0, 6.0, 16.0 + longAmount, 10.0, 10.0));
            }
            case BlockFace.EAST -> new ComplexCollisionBox(2, new HexCollisionBox(12.0, 0.0, 0.0, 16.0, 16.0, 16.0), new HexCollisionBox(0.0 - longAmount, 6.0, 4.0, 12.0, 10.0, 12.0));
            default -> new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), new HexCollisionBox(6.0, 4.0, 6.0, 10.0, 16.0 + longAmount, 10.0));
        };
    }
}

