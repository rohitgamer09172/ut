/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBodyType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.ItemDialogBody;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.StaticDialogBodyType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogBodyTypes {
    private static final VersionedRegistry<DialogBodyType<?>> REGISTRY = new VersionedRegistry("dialog_body_type");
    public static final DialogBodyType<ItemDialogBody> ITEM = DialogBodyTypes.define("item", ItemDialogBody::decode, ItemDialogBody::encode);
    public static final DialogBodyType<PlainMessageDialogBody> PLAIN_MESSAGE = DialogBodyTypes.define("plain_message", PlainMessageDialogBody::decode, PlainMessageDialogBody::encode);

    private DialogBodyTypes() {
    }

    @ApiStatus.Internal
    public static <T extends DialogBody> DialogBodyType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticDialogBodyType((TypesBuilderData)data, decoder, encoder));
    }

    public static VersionedRegistry<DialogBodyType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

