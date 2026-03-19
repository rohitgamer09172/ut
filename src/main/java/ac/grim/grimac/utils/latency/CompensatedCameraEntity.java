/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.latency;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySelf;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class CompensatedCameraEntity
extends Check
implements PacketCheck {
    private final ArrayDeque<PacketEntity> entities = new ArrayDeque(1);

    public CompensatedCameraEntity(GrimPlayer player) {
        super(player);
        this.entities.add(player.compensatedEntities.self);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.CAMERA) {
            return;
        }
        int camera = new WrapperPlayServerCamera(event).getCameraId();
        this.player.sendTransaction();
        this.player.addRealTimeTaskNow(() -> {
            PacketEntity entity = this.player.compensatedEntities.getEntity(camera);
            if (entity != null) {
                this.entities.add(entity);
            }
        });
        this.player.addRealTimeTaskNext(() -> {
            while (this.entities.size() > 1) {
                this.entities.poll();
            }
            if (this.entities.isEmpty()) {
                this.entities.add(this.player.compensatedEntities.self);
            }
        });
    }

    public boolean isSelf() {
        PacketEntitySelf self = this.player.compensatedEntities.self;
        for (PacketEntity entity : this.entities) {
            if (entity == self) continue;
            return false;
        }
        return true;
    }

    public List<PacketEntity> getPossibilities() {
        return new ArrayList<PacketEntity>(this.entities);
    }
}

