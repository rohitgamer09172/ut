/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.init.stop;

import ac.grim.grimac.manager.init.stop.StoppableInitable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.utils.anticheat.LogUtil;

public class TerminatePacketEvents
implements StoppableInitable {
    @Override
    public void stop() {
        LogUtil.info("Terminating PacketEvents...");
        PacketEvents.getAPI().terminate();
    }
}

