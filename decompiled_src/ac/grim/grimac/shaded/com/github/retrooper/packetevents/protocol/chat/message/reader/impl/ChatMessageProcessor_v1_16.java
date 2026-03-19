/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public class ChatMessageProcessor_v1_16
implements ChatMessageProcessor {
    @Override
    public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
        Component chatContent = wrapper.readComponent();
        ChatType type = wrapper.readMappedEntity(ChatTypes.getRegistry());
        UUID senderUUID = wrapper.readUUID();
        return new ChatMessage_v1_16(chatContent, type, senderUUID);
    }

    @Override
    public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
        wrapper.writeComponent(data.getChatContent());
        wrapper.writeMappedEntity(data.getType());
        wrapper.writeUUID(((ChatMessage_v1_16)data).getSenderUUID());
    }
}

