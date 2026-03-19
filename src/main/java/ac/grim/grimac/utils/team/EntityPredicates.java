/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.team;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import ac.grim.grimac.utils.team.EntityTeam;
import lombok.Generated;

public final class EntityPredicates {
    public static boolean canBePushedBy(EntityTeam entityTeam, EntityTeam playersTeam) {
        WrapperPlayServerTeams.CollisionRule playerCollisionRule;
        WrapperPlayServerTeams.CollisionRule entityCollisionRule;
        WrapperPlayServerTeams.CollisionRule collisionRule = entityCollisionRule = entityTeam == null ? WrapperPlayServerTeams.CollisionRule.ALWAYS : entityTeam.getCollisionRule();
        if (entityCollisionRule == WrapperPlayServerTeams.CollisionRule.NEVER) {
            return false;
        }
        WrapperPlayServerTeams.CollisionRule collisionRule2 = playerCollisionRule = playersTeam == null ? WrapperPlayServerTeams.CollisionRule.ALWAYS : playersTeam.getCollisionRule();
        if (playerCollisionRule == WrapperPlayServerTeams.CollisionRule.NEVER) {
            return false;
        }
        boolean isSameTeam = entityTeam != null && entityTeam.equals(playersTeam);
        return (!isSameTeam || entityCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OWN_TEAM && playerCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OWN_TEAM) && (entityCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OTHER_TEAMS && playerCollisionRule != WrapperPlayServerTeams.CollisionRule.PUSH_OTHER_TEAMS || isSameTeam);
    }

    @Generated
    private EntityPredicates() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

