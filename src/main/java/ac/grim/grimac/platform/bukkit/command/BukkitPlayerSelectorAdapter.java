/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.CommandSender
 */
package ac.grim.grimac.platform.bukkit.command;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.command.PlayerSelector;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.bukkit.sender.BukkitSenderFactory;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.SinglePlayerSelector;
import java.util.Collection;
import java.util.Collections;
import lombok.Generated;
import org.bukkit.command.CommandSender;

public class BukkitPlayerSelectorAdapter
implements PlayerSelector {
    private final SinglePlayerSelector bukkitSelector;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Sender getSinglePlayer() {
        return ((BukkitSenderFactory)GrimAPI.INSTANCE.getSenderFactory()).map((CommandSender)this.bukkitSelector.single());
    }

    @Override
    public Collection<Sender> getPlayers() {
        return Collections.singletonList(((BukkitSenderFactory)GrimAPI.INSTANCE.getSenderFactory()).map((CommandSender)this.bukkitSelector.single()));
    }

    @Override
    public String inputString() {
        return this.bukkitSelector.inputString();
    }

    @Generated
    public BukkitPlayerSelectorAdapter(SinglePlayerSelector bukkitSelector) {
        this.bukkitSelector = bukkitSelector;
    }
}

