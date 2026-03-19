/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypeDecoration;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticChatType
extends AbstractMappedEntity
implements ChatType {
    private final @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration chatDecoration;
    private final @Nullable ChatTypeDecoration overlayDecoration;
    private final @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration narrationDecoration;
    private final @Nullable ChatType.NarrationPriority narrationPriority;

    public StaticChatType(ChatTypeDecoration chatDecoration, ChatTypeDecoration narrationDecoration) {
        this(null, chatDecoration, null, narrationDecoration, null);
    }

    @ApiStatus.Internal
    public StaticChatType(@Nullable TypesBuilderData data, ChatTypeDecoration chatDecoration, ChatTypeDecoration narrationDecoration) {
        this(data, chatDecoration, null, narrationDecoration, null);
    }

    public StaticChatType(@UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration chatDecoration, @Nullable ChatTypeDecoration overlayDecoration, @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration narrationDecoration, @Nullable ChatType.NarrationPriority narrationPriority) {
        this(null, chatDecoration, overlayDecoration, narrationDecoration, narrationPriority);
    }

    @ApiStatus.Internal
    public StaticChatType(@Nullable TypesBuilderData data, @Nullable ChatTypeDecoration chatDecoration, @Nullable ChatTypeDecoration overlayDecoration, @Nullable ChatTypeDecoration narrationDecoration, @Nullable ChatType.NarrationPriority narrationPriority) {
        super(data);
        this.chatDecoration = chatDecoration;
        this.overlayDecoration = overlayDecoration;
        this.narrationDecoration = narrationDecoration;
        this.narrationPriority = narrationPriority;
    }

    @Override
    public ChatType copy(@Nullable TypesBuilderData newData) {
        return new StaticChatType(newData, this.chatDecoration, this.overlayDecoration, this.narrationDecoration, this.narrationPriority);
    }

    @Override
    public @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration getChatDecoration() {
        return this.chatDecoration;
    }

    @Override
    public @Nullable ChatTypeDecoration getOverlayDecoration() {
        return this.overlayDecoration;
    }

    @Override
    public @UnknownNullability(value="only nullable for 1.19") ChatTypeDecoration getNarrationDecoration() {
        return this.narrationDecoration;
    }

    @Override
    public @Nullable ChatType.NarrationPriority getNarrationPriority() {
        return this.narrationPriority;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticChatType)) {
            return false;
        }
        StaticChatType that = (StaticChatType)obj;
        if (!Objects.equals(this.chatDecoration, that.chatDecoration)) {
            return false;
        }
        if (!Objects.equals(this.overlayDecoration, that.overlayDecoration)) {
            return false;
        }
        if (!Objects.equals(this.narrationDecoration, that.narrationDecoration)) {
            return false;
        }
        return this.narrationPriority == that.narrationPriority;
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(new Object[]{this.chatDecoration, this.overlayDecoration, this.narrationDecoration, this.narrationPriority});
    }

    @Override
    public String toString() {
        return "StaticChatType{chatDecoration=" + this.chatDecoration + ", overlayDecoration=" + this.overlayDecoration + ", narrationDecoration=" + this.narrationDecoration + ", narrationPriority=" + (Object)((Object)this.narrationPriority) + '}';
    }
}

