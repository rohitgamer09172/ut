/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBodyType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBodyTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DialogBody {
    public static DialogBody decode(NBT nbt, PacketWrapper<?> wrapper) {
        NBTCompound compound = (NBTCompound)nbt;
        String typeName = compound.getStringTagValueOrThrow("type");
        DialogBodyType type = (DialogBodyType)DialogBodyTypes.getRegistry().getByNameOrThrow(typeName);
        return type.decode(compound, wrapper);
    }

    public static NBT encode(PacketWrapper<?> wrapper, DialogBody body) {
        NBTCompound compound = new NBTCompound();
        compound.set("type", body.getType().getName(), ResourceLocation::encode, wrapper);
        body.getType().encode(compound, wrapper, body);
        return compound;
    }

    public DialogBodyType<?> getType();
}

