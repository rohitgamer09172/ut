/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControlType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControlTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface InputControl {
    public static InputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        String typeName = compound.getStringTagValueOrThrow("type");
        InputControlType type = (InputControlType)InputControlTypes.getRegistry().getByNameOrThrow(typeName);
        return type.decode(compound, wrapper);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, InputControl control) {
        compound.set("type", control.getType().getName(), ResourceLocation::encode, wrapper);
        control.getType().encode(compound, wrapper, control);
    }

    public InputControlType<?> getType();
}

