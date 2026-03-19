/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.DialogType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.DialogTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MultiActionDialog
extends AbstractMappedEntity
implements Dialog {
    private final CommonDialogData common;
    private final List<ActionButton> actions;
    private final @Nullable ActionButton exitAction;
    private final int columns;

    public MultiActionDialog(CommonDialogData common, List<ActionButton> actions, @Nullable ActionButton exitAction, int columns) {
        this(null, common, actions, exitAction, columns);
    }

    @ApiStatus.Internal
    public MultiActionDialog(@Nullable TypesBuilderData data, CommonDialogData common, List<ActionButton> actions, @Nullable ActionButton exitAction, int columns) {
        super(data);
        this.common = common;
        this.actions = actions;
        this.exitAction = exitAction;
        this.columns = columns;
    }

    public static MultiActionDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        CommonDialogData common = CommonDialogData.decode(compound, wrapper);
        List<ActionButton> actions = compound.getListOrThrow("actions", ActionButton::decode, wrapper);
        ActionButton action = compound.getOrNull("exit_action", ActionButton::decode, wrapper);
        int columns = compound.getNumberTagValueOrDefault("columns", 2).intValue();
        return new MultiActionDialog(null, common, actions, action, columns);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, MultiActionDialog dialog) {
        CommonDialogData.encode(compound, wrapper, dialog.common);
        compound.setList("actions", dialog.actions, ActionButton::encode, wrapper);
        if (dialog.exitAction != null) {
            compound.set("exit_action", dialog.exitAction, ActionButton::encode, wrapper);
        }
        if (dialog.columns != 2) {
            compound.setTag("columns", new NBTInt(dialog.columns));
        }
    }

    @Override
    public Dialog copy(@Nullable TypesBuilderData newData) {
        return new MultiActionDialog(newData, this.common, this.actions, this.exitAction, this.columns);
    }

    public CommonDialogData getCommon() {
        return this.common;
    }

    public List<ActionButton> getActions() {
        return this.actions;
    }

    public @Nullable ActionButton getExitAction() {
        return this.exitAction;
    }

    public int getColumns() {
        return this.columns;
    }

    @Override
    public DialogType<?> getType() {
        return DialogTypes.MULTI_ACTION;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof MultiActionDialog)) {
            return false;
        }
        MultiActionDialog that = (MultiActionDialog)obj;
        if (this.columns != that.columns) {
            return false;
        }
        if (!this.common.equals(that.common)) {
            return false;
        }
        if (!this.actions.equals(that.actions)) {
            return false;
        }
        return Objects.equals(this.exitAction, that.exitAction);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.common, this.actions, this.exitAction, this.columns);
    }
}

