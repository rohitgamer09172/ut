/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ChangePageClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventActions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.CopyToClipboardClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.CustomClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.OpenFileClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.OpenUrlClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.RunCommandClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ShowDialogClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.SuggestCommandClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.NbtTagHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ClickEvent {
    public static ClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        String actionName = compound.getStringTagValueOrThrow("action");
        ClickEventAction action = (ClickEventAction)ClickEventActions.getRegistry().getByNameOrThrow(actionName);
        return action.decode(compound, wrapper);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ClickEvent clickEvent) {
        compound.set("action", clickEvent.getAction().getName(), ResourceLocation::encode, wrapper);
        clickEvent.getAction().encode(compound, wrapper, clickEvent);
    }

    public ClickEventAction<?> getAction();

    public static ClickEvent fromAdventure(ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent clickEvent) {
        switch (clickEvent.action()) {
            case OPEN_URL: {
                return new OpenUrlClickEvent(clickEvent.value());
            }
            case OPEN_FILE: {
                return new OpenFileClickEvent(clickEvent.value());
            }
            case RUN_COMMAND: {
                return new RunCommandClickEvent(clickEvent.value());
            }
            case SUGGEST_COMMAND: {
                return new SuggestCommandClickEvent(clickEvent.value());
            }
            case CHANGE_PAGE: {
                return new ChangePageClickEvent(clickEvent.value());
            }
            case COPY_TO_CLIPBOARD: {
                return new CopyToClipboardClickEvent(clickEvent.value());
            }
            case SHOW_DIALOG: {
                return new ShowDialogClickEvent((Dialog)((ClickEvent.Payload.Dialog)clickEvent.payload()).dialog());
            }
            case CUSTOM: {
                ClickEvent.Payload.Custom payload = (ClickEvent.Payload.Custom)clickEvent.payload();
                NbtTagHolder nbtTag = (NbtTagHolder)payload.nbt();
                return new CustomClickEvent(new ResourceLocation(payload.key()), nbtTag.getTag() instanceof NBTEnd ? null : nbtTag.getTag());
            }
        }
        throw new UnsupportedOperationException("Unsupported clickevent: " + clickEvent);
    }

    public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure();
}

