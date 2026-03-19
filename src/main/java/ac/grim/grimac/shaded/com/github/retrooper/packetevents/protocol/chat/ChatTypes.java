/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.StaticChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class ChatTypes {
    private static final VersionedRegistry<ChatType> REGISTRY = new VersionedRegistry("chat_type");
    public static final ChatType CHAT = ChatTypes.define("chat");
    public static final ChatType SAY_COMMAND = ChatTypes.define("say_command", ChatTypeDecoration.withSender("chat.type.announcement"));
    public static final ChatType MSG_COMMAND_INCOMING = ChatTypes.define("msg_command_incoming", ChatTypeDecoration.incomingDirectMessage("commands.message.display.incoming"));
    public static final ChatType MSG_COMMAND_OUTGOING = ChatTypes.define("msg_command_outgoing", ChatTypeDecoration.outgoingDirectMessage("commands.message.display.outgoing"));
    public static final ChatType TEAM_MSG_COMMAND_INCOMING = ChatTypes.define("team_msg_command_incoming", ChatTypeDecoration.teamMessage("chat.type.team.text"));
    public static final ChatType TEAM_MSG_COMMAND_OUTGOING = ChatTypes.define("team_msg_command_outgoing", ChatTypeDecoration.teamMessage("chat.type.team.sent"));
    public static final ChatType EMOTE_COMMAND = ChatTypes.define("emote_command", ChatTypeDecoration.withSender("chat.type.emote"), ChatTypeDecoration.withSender("chat.type.emote"));
    public static final ChatType RAW = ChatTypes.define("raw");
    @Deprecated
    public static final ChatType SYSTEM = ChatTypes.define("system");
    @Deprecated
    public static final ChatType GAME_INFO = ChatTypes.define("game_info");
    @Deprecated
    public static final ChatType MSG_COMMAND = ChatTypes.define("msg_command");
    @Deprecated
    public static final ChatType TEAM_MSG_COMMAND = ChatTypes.define("team_msg_command");

    private ChatTypes() {
    }

    @ApiStatus.Internal
    public static ChatType define(String key) {
        return ChatTypes.define(key, ChatTypeDecoration.withSender("chat.type.text"));
    }

    @ApiStatus.Internal
    public static ChatType define(String key, ChatTypeDecoration chatDeco) {
        return ChatTypes.define(key, chatDeco, ChatTypeDecoration.withSender("chat.type.text.narrate"));
    }

    @ApiStatus.Internal
    public static ChatType define(String key, ChatTypeDecoration chatDeco, ChatTypeDecoration narrationDeco) {
        return REGISTRY.define(key, data -> new StaticChatType((TypesBuilderData)data, chatDeco, narrationDeco));
    }

    public static VersionedRegistry<ChatType> getRegistry() {
        return REGISTRY;
    }

    public static ChatType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static ChatType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static Collection<ChatType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

