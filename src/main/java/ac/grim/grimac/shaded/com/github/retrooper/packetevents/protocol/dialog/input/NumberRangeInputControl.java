/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControlType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControlTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class NumberRangeInputControl
implements InputControl {
    private final int width;
    private final Component label;
    private final String labelFormat;
    private final RangeInfo rangeInfo;

    public NumberRangeInputControl(int width, Component label, String labelFormat, RangeInfo rangeInfo) {
        this.width = width;
        this.label = label;
        this.labelFormat = labelFormat;
        this.rangeInfo = rangeInfo;
    }

    public static NumberRangeInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
        Component label = compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
        String labelFormat = compound.getStringTagValueOrDefault("label_format", "options.generic_value");
        RangeInfo rangeInfo = RangeInfo.decode(compound, wrapper);
        return new NumberRangeInputControl(width, label, labelFormat, rangeInfo);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, NumberRangeInputControl control) {
        if (control.width != 200) {
            compound.setTag("width", new NBTInt(control.width));
        }
        compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
        if (!"options.generic_value".equals(control.labelFormat)) {
            compound.setTag("label_format", new NBTString(control.labelFormat));
        }
        RangeInfo.encode(compound, wrapper, control.rangeInfo);
    }

    @Override
    public InputControlType<?> getType() {
        return InputControlTypes.NUMBER_RANGE;
    }

    public int getWidth() {
        return this.width;
    }

    public Component getLabel() {
        return this.label;
    }

    public String getLabelFormat() {
        return this.labelFormat;
    }

    public RangeInfo getRangeInfo() {
        return this.rangeInfo;
    }

    public static final class RangeInfo {
        private final float start;
        private final float end;
        private final @Nullable Float initial;
        private final @Nullable Float step;

        public RangeInfo(float start, float end, @Nullable Float initial, @Nullable Float step) {
            if (initial != null) {
                float min = Math.min(start, end);
                float max = Math.max(start, end);
                if (initial.floatValue() < min || initial.floatValue() > max) {
                    throw new IllegalArgumentException("Initial value " + initial + " is outside of range [" + min + ", " + max + "]");
                }
            }
            this.start = start;
            this.end = end;
            this.initial = initial;
            this.step = step;
        }

        public static RangeInfo decode(NBTCompound compound, PacketWrapper<?> wrapper) {
            float start = compound.getNumberTagValueOrThrow("start").floatValue();
            float end = compound.getNumberTagValueOrThrow("end").floatValue();
            NBTNumber initialTag = compound.getNumberTagOrNull("initial");
            Float initial = initialTag != null ? Float.valueOf(initialTag.getAsFloat()) : null;
            NBTNumber stepTag = compound.getNumberTagOrNull("step");
            Float step = stepTag != null ? Float.valueOf(stepTag.getAsFloat()) : null;
            return new RangeInfo(start, end, initial, step);
        }

        public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, RangeInfo rangeInfo) {
            compound.setTag("start", new NBTFloat(rangeInfo.start));
            compound.setTag("end", new NBTFloat(rangeInfo.end));
            if (rangeInfo.initial != null) {
                compound.setTag("initial", new NBTFloat(rangeInfo.initial.floatValue()));
            }
            if (rangeInfo.step != null) {
                compound.setTag("step", new NBTFloat(rangeInfo.step.floatValue()));
            }
        }

        public float getStart() {
            return this.start;
        }

        public float getEnd() {
            return this.end;
        }

        public @Nullable Float getInitial() {
            return this.initial;
        }

        public @Nullable Float getStep() {
            return this.step;
        }
    }
}

