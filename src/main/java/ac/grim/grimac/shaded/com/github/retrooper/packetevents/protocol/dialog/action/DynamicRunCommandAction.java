/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.Action;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.ActionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.ActionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.DialogTemplate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DynamicRunCommandAction
implements Action {
    private final DialogTemplate template;

    public DynamicRunCommandAction(DialogTemplate template) {
        this.template = template;
    }

    public static DynamicRunCommandAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        DialogTemplate template = compound.getOrThrow("template", DialogTemplate::decode, wrapper);
        return new DynamicRunCommandAction(template);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DynamicRunCommandAction action) {
        compound.set("template", action.template, DialogTemplate::encode, wrapper);
    }

    public DialogTemplate getTemplate() {
        return this.template;
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypes.DYNAMIC_RUN_COMMAND;
    }
}

