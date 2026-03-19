/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.advancements;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public final class AdvancementProgress {
    private Map<String, CriterionProgress> criteria;

    public AdvancementProgress(Map<String, CriterionProgress> criteria) {
        this.criteria = criteria;
    }

    public static AdvancementProgress read(PacketWrapper<?> wrapper) {
        Map<String, CriterionProgress> criteria = wrapper.readMap(PacketWrapper::readString, CriterionProgress::read);
        return new AdvancementProgress(criteria);
    }

    public static void write(PacketWrapper<?> wrapper, AdvancementProgress progress) {
        wrapper.writeMap(progress.getCriteria(), PacketWrapper::writeString, CriterionProgress::write);
    }

    public Map<String, CriterionProgress> getCriteria() {
        return this.criteria;
    }

    public void setCriteria(Map<String, CriterionProgress> criteria) {
        this.criteria = criteria;
    }

    public static final class CriterionProgress {
        private @Nullable Long obtainedTimestamp;

        public CriterionProgress(@Nullable Long obtainedTimestamp) {
            this.obtainedTimestamp = obtainedTimestamp;
        }

        public static CriterionProgress read(PacketWrapper<?> wrapper) {
            return new CriterionProgress((Long)wrapper.readOptional(PacketWrapper::readLong));
        }

        public static void write(PacketWrapper<?> wrapper, CriterionProgress progress) {
            wrapper.writeOptional(progress.obtainedTimestamp, PacketWrapper::writeLong);
        }

        public @Nullable Long getObtainedTimestamp() {
            return this.obtainedTimestamp;
        }

        public void setObtainedTimestamp(@Nullable Long obtainedTimestamp) {
            this.obtainedTimestamp = obtainedTimestamp;
        }
    }
}

