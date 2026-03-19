/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import ac.grim.grimac.utils.data.Pair;

public class PacketPingListener
extends PacketListenerAbstract {
    public PacketPingListener() {
        super(PacketListenerPriority.LOWEST);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            WrapperPlayClientWindowConfirmation transaction = new WrapperPlayClientWindowConfirmation(event);
            short id = transaction.getActionId();
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
                return;
            }
            player.packetStateData.lastTransactionPacketWasValid = false;
            if (id <= 0 && player.addTransactionResponse(id)) {
                player.packetStateData.lastTransactionPacketWasValid = true;
                event.setCancelled(true);
            }
        }
        if (event.getPacketType() == PacketType.Play.Client.PONG) {
            short shortID;
            WrapperPlayClientPong pong = new WrapperPlayClientPong(event);
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
                return;
            }
            player.packetStateData.lastTransactionPacketWasValid = false;
            int id = pong.getId();
            if (id == (short)id && player.addTransactionResponse(shortID = (short)id)) {
                player.packetStateData.lastTransactionPacketWasValid = true;
                event.setCancelled(!GrimAPI.INSTANCE.getConfigManager().isDisablePongCancelling());
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        GrimPlayer player;
        if (event.getPacketType() == PacketType.Play.Server.WINDOW_CONFIRMATION) {
            WrapperPlayServerWindowConfirmation confirmation = new WrapperPlayServerWindowConfirmation(event);
            short s = confirmation.getActionId();
            player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
                return;
            }
            player.packetStateData.lastServerTransWasValid = false;
            if (s <= 0 && player.didWeSendThatTrans.remove(s)) {
                player.packetStateData.lastServerTransWasValid = true;
                player.transactionsSent.add(new Pair<Short, Long>(s, System.nanoTime()));
                player.lastTransactionSent.getAndIncrement();
            }
        }
        if (event.getPacketType() == PacketType.Play.Server.PING) {
            Short shortID;
            WrapperPlayServerPing pong = new WrapperPlayServerPing(event);
            int n = pong.getId();
            player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
                return;
            }
            player.packetStateData.lastServerTransWasValid = false;
            if (n == (short)n && player.didWeSendThatTrans.remove(shortID = Short.valueOf((short)n))) {
                player.packetStateData.lastServerTransWasValid = true;
                player.transactionsSent.add(new Pair<Short, Long>(shortID, System.nanoTime()));
                player.lastTransactionSent.getAndIncrement();
            }
        }
    }
}

