/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.sender;

import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public interface Sender {
    public static final UUID CONSOLE_UUID = new UUID(0L, 0L);
    public static final String CONSOLE_NAME = "Console";

    public String getName();

    public UUID getUniqueId();

    public void sendMessage(String var1);

    public void sendMessage(Component var1);

    public boolean hasPermission(String var1);

    public boolean hasPermission(String var1, boolean var2);

    public void performCommand(String var1);

    public boolean isConsole();

    public boolean isPlayer();

    default public boolean isValid() {
        return true;
    }

    @NotNull
    public Object getNativeSender();

    @Nullable
    public PlatformPlayer getPlatformPlayer();
}

