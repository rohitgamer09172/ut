/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter.FilterMaskType;
import java.util.BitSet;

public class FilterMask {
    public static final FilterMask FULLY_FILTERED = new FilterMask(new BitSet(0), FilterMaskType.FULLY_FILTERED);
    public static final FilterMask PASS_THROUGH = new FilterMask(new BitSet(0), FilterMaskType.PASS_THROUGH);
    private final BitSet mask;
    private final FilterMaskType type;

    private FilterMask(BitSet mask, FilterMaskType type) {
        this.type = type;
        this.mask = mask;
    }

    public FilterMask(BitSet mask) {
        this.type = FilterMaskType.PARTIALLY_FILTERED;
        this.mask = mask;
    }

    public BitSet getMask() {
        return this.mask;
    }

    public FilterMaskType getType() {
        return this.type;
    }
}

