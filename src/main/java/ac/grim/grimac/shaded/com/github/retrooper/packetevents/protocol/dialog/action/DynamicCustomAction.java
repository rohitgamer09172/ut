/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.Action;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.ActionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.ActionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class DynamicCustomAction
implements Action {
    private final ResourceLocation id;
    private final @Nullable NBTCompound additions;

    public DynamicCustomAction(ResourceLocation id, @Nullable NBTCompound additions) {
        this.id = id;
        this.additions = additions;
    }

    public static DynamicCustomAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        ResourceLocation id = compound.getOrThrow("id", ResourceLocation::decode, wrapper);
        NBTCompound additions = compound.getCompoundTagOrNull("additions");
        return new DynamicCustomAction(id, additions);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DynamicCustomAction action) {
        compound.set("id", action.id, ResourceLocation::encode, wrapper);
        if (action.additions != null) {
            compound.setTag("additions", action.additions);
        }
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypes.DYNAMIC_CUSTOM;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public @Nullable NBTCompound getAdditions() {
        return this.additions;
    }
}

