/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBodyType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBodyTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PlainMessageDialogBody
implements DialogBody {
    private final PlainMessage message;

    public PlainMessageDialogBody(PlainMessage message) {
        this.message = message;
    }

    public static PlainMessageDialogBody decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        return new PlainMessageDialogBody(PlainMessage.decode(compound, wrapper));
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, PlainMessageDialogBody body) {
        PlainMessage.encode(compound, wrapper, body.message);
    }

    @Override
    public DialogBodyType<?> getType() {
        return DialogBodyTypes.PLAIN_MESSAGE;
    }

    public PlainMessage getMessage() {
        return this.message;
    }
}

