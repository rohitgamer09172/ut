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
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ServerLinksDialog
extends AbstractMappedEntity
implements Dialog {
    private final CommonDialogData common;
    private final @Nullable ActionButton exitAction;
    private final int columns;
    private final int buttonWidth;

    public ServerLinksDialog(CommonDialogData common, @Nullable ActionButton exitAction, int columns, int buttonWidth) {
        this(null, common, exitAction, columns, buttonWidth);
    }

    @ApiStatus.Internal
    public ServerLinksDialog(@Nullable TypesBuilderData data, CommonDialogData common, @Nullable ActionButton exitAction, int columns, int buttonWidth) {
        super(data);
        this.common = common;
        this.exitAction = exitAction;
        this.columns = columns;
        this.buttonWidth = buttonWidth;
    }

    public static ServerLinksDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        CommonDialogData common = CommonDialogData.decode(compound, wrapper);
        ActionButton action = compound.getOrNull("exit_action", ActionButton::decode, wrapper);
        int columns = compound.getNumberTagValueOrDefault("columns", 2).intValue();
        int buttonWidth = compound.getNumberTagValueOrDefault("button_width", 150).intValue();
        return new ServerLinksDialog(null, common, action, columns, buttonWidth);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ServerLinksDialog dialog) {
        CommonDialogData.encode(compound, wrapper, dialog.common);
        if (dialog.exitAction != null) {
            compound.set("exit_action", dialog.exitAction, ActionButton::encode, wrapper);
        }
        if (dialog.columns != 2) {
            compound.setTag("columns", new NBTInt(dialog.columns));
        }
        if (dialog.buttonWidth != 150) {
            compound.setTag("button_width", new NBTInt(dialog.buttonWidth));
        }
    }

    @Override
    public Dialog copy(@Nullable TypesBuilderData newData) {
        return new ServerLinksDialog(newData, this.common, this.exitAction, this.columns, this.buttonWidth);
    }

    public CommonDialogData getCommon() {
        return this.common;
    }

    public @Nullable ActionButton getExitAction() {
        return this.exitAction;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getButtonWidth() {
        return this.buttonWidth;
    }

    @Override
    public DialogType<?> getType() {
        return DialogTypes.SERVER_LINKS;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof ServerLinksDialog)) {
            return false;
        }
        ServerLinksDialog that = (ServerLinksDialog)obj;
        if (this.columns != that.columns) {
            return false;
        }
        if (this.buttonWidth != that.buttonWidth) {
            return false;
        }
        if (!this.common.equals(that.common)) {
            return false;
        }
        return Objects.equals(this.exitAction, that.exitAction);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.common, this.exitAction, this.columns, this.buttonWidth);
    }
}

