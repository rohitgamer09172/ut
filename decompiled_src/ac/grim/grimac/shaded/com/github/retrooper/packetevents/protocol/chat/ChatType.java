/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.StaticChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ChatType
extends MappedEntity,
CopyableEntity<ChatType>,
DeepComparableEntity {
    public @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration getChatDecoration();

    @ApiStatus.Obsolete(since="1.19.1")
    public @Nullable ChatTypeDecoration getOverlayDecoration();

    public @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration getNarrationDecoration();

    @ApiStatus.Obsolete(since="1.19.1")
    public @Nullable NarrationPriority getNarrationPriority();

    public static ChatType readDirect(PacketWrapper<?> wrapper) {
        ChatTypeDecoration chatDecoration = ChatTypeDecoration.read(wrapper);
        ChatTypeDecoration narrationDecoration = ChatTypeDecoration.read(wrapper);
        return new StaticChatType(chatDecoration, narrationDecoration);
    }

    public static void writeDirect(PacketWrapper<?> wrapper, ChatType chatType) {
        ChatTypeDecoration.write(wrapper, chatType.getChatDecoration());
        ChatTypeDecoration.write(wrapper, chatType.getNarrationDecoration());
    }

    public static ChatType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        NBTCompound chatTag = compound.getCompoundTagOrNull("chat");
        NBTCompound narrationTag = compound.getCompoundTagOrNull("narration");
        ChatTypeDecoration overlay = null;
        NarrationPriority narrationPriority = null;
        if (version.isOlderThan(ClientVersion.V_1_19_1)) {
            NBTCompound overlayTag = compound.getCompoundTagOrNull("overlay");
            if (overlayTag != null && (overlayTag = overlayTag.getCompoundTagOrNull("description")) != null) {
                overlay = ChatTypeDecoration.decode((NBT)overlayTag, version);
            }
            if (chatTag != null) {
                chatTag = chatTag.getCompoundTagOrNull("description");
            }
            if (narrationTag != null) {
                narrationPriority = AdventureIndexUtil.indexValueOrThrow(NarrationPriority.ID_INDEX, narrationTag.getStringTagValueOrThrow("priority"));
                narrationTag = narrationTag.getCompoundTagOrNull("description");
            }
        } else {
            Objects.requireNonNull(chatTag, "NBT chat does not exist");
            Objects.requireNonNull(narrationTag, "NBT narration does not exist");
        }
        ChatTypeDecoration chat = chatTag == null ? null : ChatTypeDecoration.decode((NBT)chatTag, version);
        ChatTypeDecoration narration = narrationTag == null ? null : ChatTypeDecoration.decode((NBT)narrationTag, version);
        return new StaticChatType(data, chat, overlay, narration, narrationPriority);
    }

    public static NBT encode(ChatType chatType, ClientVersion version) {
        NBT narrationTag;
        NBTCompound compound = new NBTCompound();
        NBT chatTag = chatType.getChatDecoration() == null ? null : ChatTypeDecoration.encode(chatType.getChatDecoration(), version);
        NBT nBT = narrationTag = chatType.getNarrationDecoration() == null ? null : ChatTypeDecoration.encode(chatType.getNarrationDecoration(), version);
        if (version.isOlderThan(ClientVersion.V_1_19_1)) {
            ChatTypeDecoration overlayDeco = chatType.getOverlayDecoration();
            if (overlayDeco != null) {
                NBTCompound overlayCompound = new NBTCompound();
                overlayCompound.setTag("description", ChatTypeDecoration.encode(overlayDeco, version));
                compound.setTag("overlay", overlayCompound);
            }
            if (narrationTag != null) {
                NBTCompound narrationCompound = new NBTCompound();
                narrationCompound.setTag("description", narrationTag);
                if (chatType.getNarrationPriority() != null) {
                    narrationCompound.setTag("priority", new NBTString(chatType.getNarrationPriority().getId()));
                }
                narrationTag = narrationCompound;
            }
            if (chatTag != null) {
                NBTCompound chatCompound = new NBTCompound();
                chatCompound.setTag("description", chatTag);
                chatTag = chatCompound;
            }
        }
        if (chatTag != null) {
            compound.setTag("chat", chatTag);
        }
        if (narrationTag != null) {
            compound.setTag("narration", narrationTag);
        }
        return compound;
    }

    @ApiStatus.Obsolete(since="1.19.1")
    public static enum NarrationPriority {
        CHAT("chat"),
        SYSTEM("system");

        public static final Index<String, NarrationPriority> ID_INDEX;
        private final String id;

        private NarrationPriority(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        static {
            ID_INDEX = Index.create(NarrationPriority.class, NarrationPriority::getId);
        }
    }

    public static class Bound
    extends ChatMessage_v1_19_1.ChatTypeBoundNetwork {
        public Bound(ChatType type, Component name, @Nullable Component targetName) {
            super(type, name, targetName);
        }
    }
}

