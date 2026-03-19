/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

public enum ChatCompletionAction {
    ADD,
    REMOVE,
    SET;

    private static final ChatCompletionAction[] VALUES;

    public static ChatCompletionAction fromId(int id) {
        return VALUES[id];
    }

    static {
        VALUES = ChatCompletionAction.values();
    }
}

