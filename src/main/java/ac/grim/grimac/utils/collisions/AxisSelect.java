/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

/*
 * Uses 'sealed' constructs - enablewith --sealed true
 */
public enum AxisSelect {
    EAST{

        @Override
        public SimpleCollisionBox modify(SimpleCollisionBox box) {
            box.maxX = 1.0;
            return box;
        }
    }
    ,
    WEST{

        @Override
        public SimpleCollisionBox modify(SimpleCollisionBox box) {
            box.minX = 0.0;
            return box;
        }
    }
    ,
    NORTH{

        @Override
        public SimpleCollisionBox modify(SimpleCollisionBox box) {
            box.minZ = 0.0;
            return box;
        }
    }
    ,
    SOUTH{

        @Override
        public SimpleCollisionBox modify(SimpleCollisionBox box) {
            box.maxZ = 1.0;
            return box;
        }
    }
    ,
    UP{

        @Override
        public SimpleCollisionBox modify(SimpleCollisionBox box) {
            box.minY = 0.0;
            return box;
        }
    }
    ,
    DOWN{

        @Override
        public SimpleCollisionBox modify(SimpleCollisionBox box) {
            box.maxY = 1.0;
            return box;
        }
    };


    public abstract SimpleCollisionBox modify(SimpleCollisionBox var1);

    @Contract(pure=true)
    public static AxisSelect byFace(@NotNull BlockFace face) {
        return switch (face) {
            case BlockFace.EAST -> EAST;
            case BlockFace.WEST -> WEST;
            case BlockFace.NORTH -> NORTH;
            case BlockFace.SOUTH -> SOUTH;
            case BlockFace.UP -> UP;
            default -> DOWN;
        };
    }
}

