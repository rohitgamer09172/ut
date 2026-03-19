/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package ac.grim.grimac.events.packets.worldreader;

import ac.grim.grimac.events.packets.worldreader.BasePacketWorldReader;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.ListPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;

public class PacketWorldReaderEight
extends BasePacketWorldReader {
    @Override
    public void handleMapChunkBulk(GrimPlayer player, PacketSendEvent event) {
        int column;
        PacketWrapper wrapper = new PacketWrapper(event);
        ByteBuf buffer = (ByteBuf)wrapper.getBuffer();
        boolean skylight = wrapper.readBoolean();
        int columns = wrapper.readVarInt();
        int[] x = new int[columns];
        int[] z = new int[columns];
        int[] mask = new int[columns];
        for (column = 0; column < columns; ++column) {
            x[column] = wrapper.readInt();
            z[column] = wrapper.readInt();
            mask[column] = wrapper.readUnsignedShort();
        }
        for (column = 0; column < columns; ++column) {
            BitSet bitset = BitSet.valueOf(new long[]{mask[column]});
            BaseChunk[] chunkSections = new Chunk_v1_9[16];
            this.readChunk(buffer, (Chunk_v1_9[])chunkSections, bitset);
            int chunks = Integer.bitCount(mask[column]);
            buffer.readerIndex(buffer.readerIndex() + 256 + chunks * 2048 + (skylight ? chunks * 2048 : 0));
            this.addChunkToCache(event, player, chunkSections, true, x[column], z[column]);
        }
    }

    @Override
    public void handleMapChunk(GrimPlayer player, PacketSendEvent event) {
        PacketWrapper wrapper = new PacketWrapper(event);
        int chunkX = wrapper.readInt();
        int chunkZ = wrapper.readInt();
        boolean groundUp = wrapper.readBoolean();
        BitSet mask = BitSet.valueOf(new long[]{wrapper.readUnsignedShort()});
        int size = wrapper.readVarInt();
        BaseChunk[] chunks = new Chunk_v1_9[16];
        this.readChunk((ByteBuf)event.getByteBuf(), (Chunk_v1_9[])chunks, mask);
        this.addChunkToCache(event, player, chunks, groundUp, chunkX, chunkZ);
        event.setLastUsedWrapper(null);
    }

    private void readChunk(ByteBuf buf, Chunk_v1_9[] chunks, BitSet set) {
        for (int ind = 0; ind < 16; ++ind) {
            if (!set.get(ind)) continue;
            chunks[ind] = this.readChunk(buf);
        }
    }

    public Chunk_v1_9 readChunk(ByteBuf in) {
        ListPalette palette = new ListPalette(4);
        BitStorage storage = new BitStorage(4, 4096);
        DataPalette dataPalette = new DataPalette(palette, storage, PaletteType.CHUNK);
        palette.stateToId(0);
        short lastNext = -1;
        int lastID = -1;
        int blockCount = 0;
        for (int i = 0; i < 4096; ++i) {
            short next = in.readShort();
            if (next != 0) {
                ++blockCount;
            }
            if (next != lastNext) {
                lastNext = next;
                next = (short)((next & 0xFF00) >> 8 | next << 8);
                dataPalette.set(i & 0xF, i >> 8 & 0xF, i >> 4 & 0xF, next);
                lastID = dataPalette.storage.get(i);
                continue;
            }
            dataPalette.storage.set(i, lastID);
        }
        return new Chunk_v1_9(blockCount, dataPalette);
    }
}

