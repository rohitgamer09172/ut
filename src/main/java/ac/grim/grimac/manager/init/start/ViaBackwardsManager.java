/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.manager.init.start.StartableInitable;

public class ViaBackwardsManager
implements StartableInitable {
    @Override
    public void start() {
        System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");
    }
}

