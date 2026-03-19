/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.MessageSignature;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;

public class LastSeenMessages {
    public static final LastSeenMessages EMPTY = new LastSeenMessages(new ArrayList<Entry>());
    private final List<Entry> entries;

    public LastSeenMessages(List<Entry> entries) {
        this.entries = entries;
    }

    public void updateHash(DataOutput output) throws IOException {
        for (Entry entry : this.entries) {
            UUID uuid = entry.getUUID();
            byte[] lastVerifier = entry.getLastVerifier();
            output.writeByte(70);
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
            output.write(lastVerifier);
        }
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public static class Entry {
        private final UUID uuid;
        private final byte[] signature;

        public Entry(UUID uuid, byte[] lastVerifier) {
            this.uuid = uuid;
            this.signature = lastVerifier;
        }

        public UUID getUUID() {
            return this.uuid;
        }

        public byte[] getLastVerifier() {
            return this.signature;
        }
    }

    public static class Update {
        private final int offset;
        private final BitSet acknowledged;
        private final byte checksum;

        @ApiStatus.Obsolete
        public Update(int offset, BitSet acknowledged) {
            this(offset, acknowledged, 0);
        }

        public Update(int offset, BitSet acknowledged, byte checksum) {
            this.offset = offset;
            this.acknowledged = acknowledged;
            this.checksum = checksum;
        }

        public int getOffset() {
            return this.offset;
        }

        public BitSet getAcknowledged() {
            return this.acknowledged;
        }

        public byte getChecksum() {
            return this.checksum;
        }
    }

    public static class LegacyUpdate {
        private final LastSeenMessages lastSeenMessages;
        @Nullable
        private final Entry lastReceived;

        public LegacyUpdate(LastSeenMessages lastSeenMessages, @Nullable Entry lastReceived) {
            this.lastSeenMessages = lastSeenMessages;
            this.lastReceived = lastReceived;
        }

        public LastSeenMessages getLastSeenMessages() {
            return this.lastSeenMessages;
        }

        @Nullable
        public Entry getLastReceived() {
            return this.lastReceived;
        }
    }

    public static class Packed {
        private List<MessageSignature.Packed> packedMessageSignatures;

        public Packed(List<MessageSignature.Packed> packedMessageSignatures) {
            this.packedMessageSignatures = packedMessageSignatures;
        }

        public List<MessageSignature.Packed> getPackedMessageSignatures() {
            return this.packedMessageSignatures;
        }

        public void setPackedMessageSignatures(List<MessageSignature.Packed> packedMessageSignatures) {
            this.packedMessageSignatures = packedMessageSignatures;
        }
    }
}

