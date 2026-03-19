/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.command;

import ac.grim.grimac.platform.api.sender.Sender;
import java.util.Collection;

public interface PlayerSelector {
    public boolean isSingle();

    public Sender getSinglePlayer();

    public Collection<Sender> getPlayers();

    public String inputString();
}

