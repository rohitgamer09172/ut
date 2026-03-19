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
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SingleOptionInputControl
implements InputControl {
    private final int width;
    private final List<Entry> options;
    private final Component label;
    private final boolean labelVisible;

    public SingleOptionInputControl(int width, List<Entry> options, Component label, boolean labelVisible) {
        boolean initial = false;
        for (Entry entry : options) {
            if (!entry.initial) continue;
            if (initial) {
                throw new IllegalArgumentException("Multiple initial values");
            }
            initial = true;
        }
        this.width = width;
        this.options = options;
        this.label = label;
        this.labelVisible = labelVisible;
    }

    public static SingleOptionInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
        List<Entry> options = compound.getListOrThrow("options", Entry::decode, wrapper);
        Component label = compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
        boolean labelVisible = compound.getBooleanOr("label_visible", true);
        return new SingleOptionInputControl(width, options, label, labelVisible);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, SingleOptionInputControl control) {
        if (control.width != 200) {
            compound.setTag("width", new NBTInt(control.width));
        }
        compound.setList("options", control.options, Entry::encode, wrapper);
        compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
        if (!control.labelVisible) {
            compound.setTag("label_visible", new NBTByte(false));
        }
    }

    @Override
    public InputControlType<?> getType() {
        return InputControlTypes.SINGLE_OPTION;
    }

    public int getWidth() {
        return this.width;
    }

    public List<Entry> getOptions() {
        return this.options;
    }

    public Component getLabel() {
        return this.label;
    }

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public static final class Entry {
        private final String id;
        private final @Nullable Component display;
        private final boolean initial;

        public Entry(String id, @Nullable Component display, boolean initial) {
            this.id = id;
            this.display = display;
            this.initial = initial;
        }

        public static Entry decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (nbt instanceof NBTString) {
                return new Entry(((NBTString)nbt).getValue(), null, false);
            }
            NBTCompound compound = (NBTCompound)nbt;
            String id = compound.getStringTagValueOrThrow("id");
            Component display = compound.getOrNull("display", AdventureSerializer.serializer(wrapper), wrapper);
            boolean initial = compound.getBooleanOr("initial", false);
            return new Entry(id, display, initial);
        }

        public static NBT encode(PacketWrapper<?> wrapper, Entry entry) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("id", new NBTString(entry.id));
            if (entry.display != null) {
                compound.set("display", entry.display, AdventureSerializer.serializer(wrapper), wrapper);
            }
            if (entry.initial) {
                compound.setTag("initial", new NBTByte(true));
            }
            return compound;
        }

        public String getId() {
            return this.id;
        }

        public @Nullable Component getDisplay() {
            return this.display;
        }

        public boolean isInitial() {
            return this.initial;
        }
    }
}

