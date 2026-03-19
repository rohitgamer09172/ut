/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;

@CheckData(name="CrashE")
public class CrashE
extends Check
implements PacketCheck {
    public CrashE(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientSettings wrapper;
        int viewDistance;
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS && (viewDistance = (wrapper = new WrapperPlayClientSettings(event)).getViewDistance()) < 2) {
            this.flagAndAlert("distance=" + viewDistance);
            wrapper.setViewDistance(2);
        }
    }
}

