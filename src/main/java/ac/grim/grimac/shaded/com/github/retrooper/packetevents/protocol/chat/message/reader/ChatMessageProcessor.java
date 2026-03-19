/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface ChatMessageProcessor {
    public ChatMessage readChatMessage(@NotNull PacketWrapper<?> var1);

    public void writeChatMessage(@NotNull PacketWrapper<?> var1, @NotNull ChatMessage var2);
}

