/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockBoundingBox;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugStructureInfos {
    private final List<DebugStructureInfo> infos;

    public DebugStructureInfos(List<DebugStructureInfo> infos) {
        this.infos = infos;
    }

    public static DebugStructureInfos read(PacketWrapper<?> wrapper) {
        List<DebugStructureInfo> infos = wrapper.readList(DebugStructureInfo::read);
        return new DebugStructureInfos(infos);
    }

    public static void write(PacketWrapper<?> wrapper, DebugStructureInfos infos) {
        wrapper.writeList(infos.infos, DebugStructureInfo::write);
    }

    public List<DebugStructureInfo> getInfos() {
        return this.infos;
    }

    public static final class DebugStructureInfo {
        private final BlockBoundingBox boundingBox;
        private final List<Piece> pieces;

        public DebugStructureInfo(BlockBoundingBox boundingBox, List<Piece> pieces) {
            this.boundingBox = boundingBox;
            this.pieces = pieces;
        }

        public static DebugStructureInfo read(PacketWrapper<?> wrapper) {
            BlockBoundingBox box = BlockBoundingBox.read(wrapper);
            List<Piece> pieces = wrapper.readList(Piece::read);
            return new DebugStructureInfo(box, pieces);
        }

        public static void write(PacketWrapper<?> wrapper, DebugStructureInfo info) {
            BlockBoundingBox.write(wrapper, info.boundingBox);
            wrapper.writeList(info.pieces, Piece::write);
        }

        public BlockBoundingBox getBoundingBox() {
            return this.boundingBox;
        }

        public List<Piece> getPieces() {
            return this.pieces;
        }

        public static final class Piece {
            private final BlockBoundingBox boundingBox;
            private final boolean start;

            public Piece(BlockBoundingBox boundingBox, boolean start) {
                this.boundingBox = boundingBox;
                this.start = start;
            }

            public static Piece read(PacketWrapper<?> wrapper) {
                BlockBoundingBox boundingBox = BlockBoundingBox.read(wrapper);
                boolean start = wrapper.readBoolean();
                return new Piece(boundingBox, start);
            }

            public static void write(PacketWrapper<?> wrapper, Piece piece) {
                BlockBoundingBox.write(wrapper, piece.boundingBox);
                wrapper.writeBoolean(piece.start);
            }

            public BlockBoundingBox getBoundingBox() {
                return this.boundingBox;
            }

            public boolean isStart() {
                return this.start;
            }
        }
    }
}

