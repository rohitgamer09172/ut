/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.sender;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.UUID;

public final class AbstractSender<T>
implements Sender {
    private final SenderFactory<T> factory;
    private final T sender;
    private final UUID uniqueId;
    private final String name;
    private final boolean isConsole;

    AbstractSender(@NotNull SenderFactory<T> factory, @NotNull T sender) {
        this.factory = factory;
        this.sender = sender;
        this.uniqueId = factory.getUniqueId(this.sender);
        this.name = factory.getName(this.sender);
        this.isConsole = factory.isConsole(this.sender);
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(String message) {
        this.factory.sendMessage(this.sender, message);
    }

    @Override
    public void sendMessage(Component message) {
        this.factory.sendMessage(this.sender, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.isConsole() || this.factory.hasPermission(this.sender, permission);
    }

    @Override
    public boolean hasPermission(String permission, boolean defaultIfUnset) {
        return this.isConsole() || this.factory.hasPermission(this.sender, permission, defaultIfUnset);
    }

    @Override
    public void performCommand(String commandLine) {
        this.factory.performCommand(this.sender, commandLine);
    }

    @Override
    public boolean isConsole() {
        return this.isConsole;
    }

    @Override
    public boolean isPlayer() {
        return this.factory.isPlayer(this.sender);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbstractSender)) {
            return false;
        }
        AbstractSender that = (AbstractSender)o;
        return this.getUniqueId().equals(that.getUniqueId());
    }

    public int hashCode() {
        return this.uniqueId.hashCode();
    }

    @NotNull
    public T getNativeSender() {
        return this.sender;
    }

    @Override
    @Nullable
    public PlatformPlayer getPlatformPlayer() {
        return GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromUUID(this.getUniqueId());
    }
}

