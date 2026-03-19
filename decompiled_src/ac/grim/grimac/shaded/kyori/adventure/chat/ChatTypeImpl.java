/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.chat;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.chat.ChatType;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

final class ChatTypeImpl
implements ChatType {
    private final Key key;

    ChatTypeImpl(@NotNull Key key) {
        this.key = key;
    }

    @Override
    @NotNull
    public Key key() {
        return this.key;
    }

    public String toString() {
        return Internals.toString(this);
    }

    static final class BoundImpl
    implements ChatType.Bound {
        private final ChatType chatType;
        private final Component name;
        @Nullable
        private final Component target;

        BoundImpl(ChatType chatType, Component name, @Nullable Component target) {
            this.chatType = chatType;
            this.name = name;
            this.target = target;
        }

        @Override
        @NotNull
        public ChatType type() {
            return this.chatType;
        }

        @Override
        @NotNull
        public Component name() {
            return this.name;
        }

        @Override
        @Nullable
        public Component target() {
            return this.target;
        }

        public String toString() {
            return Internals.toString(this);
        }
    }
}

