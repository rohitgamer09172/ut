/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.team;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import ac.grim.grimac.utils.team.TeamHandler;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Generated;

public final class EntityTeam {
    public final String name;
    public final Set<String> entries = new HashSet<String>();
    private final GrimPlayer player;
    private WrapperPlayServerTeams.CollisionRule collisionRule;

    public EntityTeam(GrimPlayer player, String name) {
        this.player = player;
        this.name = name;
    }

    public void update(WrapperPlayServerTeams teams) {
        teams.getTeamInfo().ifPresent(info -> {
            this.collisionRule = info.getCollisionRule();
        });
        TeamHandler teamHandler = this.player.checkManager.getPacketCheck(TeamHandler.class);
        WrapperPlayServerTeams.TeamMode mode = teams.getTeamMode();
        if (mode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || mode == WrapperPlayServerTeams.TeamMode.CREATE) {
            block0: for (String teamPlayer : teams.getPlayers()) {
                if (teamPlayer.equals(this.player.user.getName())) {
                    teamHandler.setPlayerTeam(this);
                    continue;
                }
                for (UserProfile profile : this.player.compensatedEntities.profiles.values()) {
                    if (profile.getName() == null || !profile.getName().equals(teamPlayer)) continue;
                    teamHandler.addEntityToTeam(profile.getUUID().toString(), this);
                    continue block0;
                }
                teamHandler.addEntityToTeam(teamPlayer, this);
            }
        } else if (mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            block2: for (String teamPlayer : teams.getPlayers()) {
                if (teamPlayer.equals(this.player.user.getName())) {
                    teamHandler.setPlayerTeam(null);
                    continue;
                }
                for (UserProfile profile : this.player.compensatedEntities.profiles.values()) {
                    if (profile.getName() == null || !profile.getName().equals(teamPlayer)) continue;
                    String uuid = profile.getUUID().toString();
                    this.entries.remove(uuid);
                    teamHandler.removeEntityFromTeam(uuid);
                    continue block2;
                }
                teamHandler.removeEntityFromTeam(teamPlayer);
                this.entries.remove(teamPlayer);
            }
        } else if (mode == WrapperPlayServerTeams.TeamMode.REMOVE) {
            EntityTeam playersTeam = teamHandler.getPlayerTeam();
            if (playersTeam != null && playersTeam.name.equals(this.name)) {
                teamHandler.setPlayerTeam(null);
            }
            for (String entry : this.entries) {
                teamHandler.removeEntityFromTeam(entry);
            }
            this.entries.clear();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityTeam)) return false;
        EntityTeam t = (EntityTeam)o;
        if (!Objects.equals(this.name, t.name)) return false;
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Generated
    public WrapperPlayServerTeams.CollisionRule getCollisionRule() {
        return this.collisionRule;
    }
}

