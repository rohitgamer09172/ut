/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.DialogType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.DialogTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialogs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.dialog.DialogLike;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Dialog
extends MappedEntity,
DeepComparableEntity,
CopyableEntity<Dialog>,
DialogLike {
    public static Dialog read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(Dialogs.getRegistry(), Dialog::readDirect);
    }

    public static void write(PacketWrapper<?> wrapper, Dialog dialog) {
        wrapper.writeMappedEntityOrDirect(dialog, Dialog::writeDirect);
    }

    public static Dialog readDirect(PacketWrapper<?> wrapper) {
        return Dialog.decodeDirect(wrapper.readNBTRaw(), wrapper, null);
    }

    public static void writeDirect(PacketWrapper<?> wrapper, Dialog dialog) {
        wrapper.writeNBTRaw(Dialog.encodeDirect(dialog, wrapper));
    }

    public static Dialog decode(NBT nbt, PacketWrapper<?> wrapper) {
        if (nbt instanceof NBTString) {
            return wrapper.replaceRegistry(Dialogs.getRegistry()).getByNameOrThrow(((NBTString)nbt).getValue());
        }
        return Dialog.decodeDirect(nbt, wrapper, null);
    }

    public static NBT encode(PacketWrapper<?> wrapper, Dialog dialog) {
        if (dialog.isRegistered()) {
            return new NBTString(dialog.getName().toString());
        }
        return Dialog.encodeDirect(dialog, wrapper);
    }

    @ApiStatus.Internal
    public static Dialog decodeDirect(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        String dialogTypeName = compound.getStringTagValueOrThrow("type");
        DialogType dialogType = (DialogType)DialogTypes.getRegistry().getByNameOrThrow(dialogTypeName);
        return (Dialog)dialogType.decode(compound, wrapper).copy(data);
    }

    @ApiStatus.Internal
    public static NBT encodeDirect(Dialog dialog, PacketWrapper<?> wrapper) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("type", new NBTString(dialog.getType().getName().toString()));
        dialog.getType().encode(compound, wrapper, dialog);
        return compound;
    }

    public DialogType<?> getType();
}

