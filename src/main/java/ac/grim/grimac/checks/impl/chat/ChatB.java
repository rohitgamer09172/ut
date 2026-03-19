/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.chat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommandUnsigned;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;

@CheckData(name="ChatB", description="Invalid chat message")
public class ChatB
extends Check
implements PacketCheck {
    public ChatB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        String command;
        String message;
        if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE && this.checkChatMessage(message = new WrapperPlayClientChatMessage(event).getMessage())) {
            event.setCancelled(true);
        }
        if (event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND_UNSIGNED && !(command = "/" + new WrapperPlayClientChatCommandUnsigned(event).getCommand()).stripTrailing().equals(command) && this.flagAndAlert("command=" + command)) {
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
        if (event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND && !(command = "/" + new WrapperPlayClientChatCommand(event).getCommand()).trim().equals(command) && this.flagAndAlert("command=" + command)) {
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
    }

    public boolean checkChatMessage(String message) {
        if ((message.isEmpty() || !message.trim().equals(message) || message.startsWith("/") && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) && this.flagAndAlert("message=" + message) && this.shouldModifyPackets()) {
            this.player.onPacketCancel();
            return true;
        }
        return false;
    }
}

