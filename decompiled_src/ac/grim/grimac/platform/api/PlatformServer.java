/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api;

import ac.grim.grimac.platform.api.sender.Sender;

public interface PlatformServer {
    public String getPlatformImplementationString();

    public void dispatchCommand(Sender var1, String var2);

    public Sender getConsoleSender();

    public void registerOutgoingPluginChannel(String var1);

    public double getTPS();
}

