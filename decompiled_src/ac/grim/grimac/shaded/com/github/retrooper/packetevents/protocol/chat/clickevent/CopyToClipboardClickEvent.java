/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventActions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CopyToClipboardClickEvent
implements ClickEvent {
    private final String value;

    public CopyToClipboardClickEvent(String value) {
        this.value = value;
    }

    public static CopyToClipboardClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        String value = compound.getStringTagValueOrThrow("value");
        return new CopyToClipboardClickEvent(value);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, CopyToClipboardClickEvent clickEvent) {
        compound.setTag("value", new NBTString(clickEvent.value));
    }

    @Override
    public ClickEventAction<?> getAction() {
        return ClickEventActions.COPY_TO_CLIPBOARD;
    }

    @Override
    public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
        return ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.copyToClipboard(this.value);
    }

    public String getValue() {
        return this.value;
    }
}

