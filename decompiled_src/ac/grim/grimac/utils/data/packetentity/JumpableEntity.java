/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.VectorData;
import java.util.Set;

public interface JumpableEntity {
    public boolean isJumping();

    public void setJumping(boolean var1);

    public float getJumpPower();

    public void setJumpPower(float var1);

    public boolean canPlayerJump(GrimPlayer var1);

    public boolean hasSaddle();

    public void executeJump(GrimPlayer var1, Set<VectorData> var2);
}

