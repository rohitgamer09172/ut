/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DialogAction {
    CLOSE("close", true),
    NONE("none", false),
    WAIT_FOR_RESPONSE("wait_for_response", true);

    public static final Index<String, DialogAction> NAME_INDEX;
    private final String name;
    private final boolean willUnpause;

    private DialogAction(String name, boolean willUnpause) {
        this.name = name;
        this.willUnpause = willUnpause;
    }

    public static DialogAction decode(NBT nbt, PacketWrapper<?> wrapper) {
        return AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, ((NBTString)nbt).getValue());
    }

    public static NBT encode(PacketWrapper<?> wrapper, DialogAction action) {
        return new NBTString(action.name);
    }

    public String getName() {
        return this.name;
    }

    public boolean isWillUnpause() {
        return this.willUnpause;
    }

    static {
        NAME_INDEX = Index.create(DialogAction.class, DialogAction::getName);
    }
}

