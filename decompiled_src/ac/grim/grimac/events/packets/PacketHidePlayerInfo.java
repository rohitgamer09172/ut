/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PacketHidePlayerInfo
extends PacketListenerAbstract {
    public PacketHidePlayerInfo() {
        super(PacketListenerPriority.HIGHEST);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                return;
            }
            GrimPlayer receiver = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (receiver == null) {
                return;
            }
            WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(event);
            if (info.getAction() == WrapperPlayServerPlayerInfo.Action.UPDATE_GAME_MODE || info.getAction() == WrapperPlayServerPlayerInfo.Action.ADD_PLAYER) {
                List<WrapperPlayServerPlayerInfo.PlayerData> nmsPlayerInfoDataList = info.getPlayerDataList();
                int hideCount = 0;
                for (WrapperPlayServerPlayerInfo.PlayerData playerData : nmsPlayerInfoDataList) {
                    if (!GrimAPI.INSTANCE.getSpectateManager().shouldHidePlayer(receiver, playerData)) continue;
                    ++hideCount;
                    if (playerData.getGameMode() != GameMode.SPECTATOR) continue;
                    playerData.setGameMode(GameMode.SURVIVAL);
                }
                if (hideCount == nmsPlayerInfoDataList.size() && info.getAction() == WrapperPlayServerPlayerInfo.Action.UPDATE_GAME_MODE) {
                    event.setCancelled(true);
                } else if (hideCount > 0) {
                    event.markForReEncode(true);
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            GrimPlayer receiver = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (receiver == null) {
                return;
            }
            WrapperPlayServerPlayerInfoUpdate wrapper = new WrapperPlayServerPlayerInfoUpdate(event);
            EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions = wrapper.getActions();
            if (actions.contains((Object)WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE)) {
                boolean onlyGameMode = actions.size() == 1;
                int hideCount = 0;
                ArrayList<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> modified = new ArrayList<WrapperPlayServerPlayerInfoUpdate.PlayerInfo>(wrapper.getEntries().size());
                for (WrapperPlayServerPlayerInfoUpdate.PlayerInfo entry : wrapper.getEntries()) {
                    WrapperPlayServerPlayerInfoUpdate.PlayerInfo modifiedPacket = null;
                    UserProfile gameProfile = entry.getGameProfile();
                    if (GrimAPI.INSTANCE.getSpectateManager().shouldHidePlayer(receiver, gameProfile.getUUID())) {
                        ++hideCount;
                        if (entry.getGameMode() == GameMode.SPECTATOR) {
                            modifiedPacket = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, entry.isListed(), entry.getLatency(), GameMode.SURVIVAL, entry.getDisplayName(), entry.getChatSession());
                            modified.add(modifiedPacket);
                        }
                    }
                    if (modifiedPacket == null) {
                        modified.add(entry);
                        continue;
                    }
                    if (onlyGameMode) continue;
                    modified.add(modifiedPacket);
                }
                if (hideCount == modified.size()) {
                    if (onlyGameMode) {
                        event.setCancelled(true);
                    } else {
                        wrapper.getActions().remove((Object)WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE);
                        event.markForReEncode(true);
                    }
                } else {
                    wrapper.setEntries(modified);
                    event.markForReEncode(true);
                }
            }
        }
    }
}

