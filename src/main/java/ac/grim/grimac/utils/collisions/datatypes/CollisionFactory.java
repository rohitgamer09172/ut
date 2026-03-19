/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;

public interface CollisionFactory {
    public CollisionBox fetch(GrimPlayer var1, ClientVersion var2, WrappedBlockState var3, int var4, int var5, int var6);
}

