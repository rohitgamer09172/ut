/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.latency;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PositionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseCooldown;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;
import ac.grim.grimac.utils.data.CooldownData;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CompensatedCooldown
extends Check
implements PositionCheck {
    private final ConcurrentHashMap<ResourceLocation, CooldownData> itemCooldownMap = new ConcurrentHashMap();

    public CompensatedCooldown(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPositionUpdate(PositionUpdate positionUpdate) {
        Iterator<Map.Entry<ResourceLocation, CooldownData>> it = this.itemCooldownMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ResourceLocation, CooldownData> entry = it.next();
            if (entry.getValue().getTransaction() < this.player.lastTransactionReceived.get()) {
                entry.getValue().tick();
            }
            if (entry.getValue().getTicksRemaining() > 0) continue;
            it.remove();
        }
    }

    public boolean hasItem(ItemStack item) {
        Optional<ResourceLocation> cooldownGroup;
        ItemUseCooldown cooldown;
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2) && (cooldown = (ItemUseCooldown)item.getComponentOr(ComponentTypes.USE_COOLDOWN, null)) != null && (cooldownGroup = cooldown.getCooldownGroup()).isPresent()) {
            return this.itemCooldownMap.containsKey(cooldownGroup.get());
        }
        return this.itemCooldownMap.containsKey(item.getType().getName());
    }

    public void addCooldown(ResourceLocation location, int cooldown, int transaction) {
        if (cooldown == 0) {
            this.removeCooldown(location);
            return;
        }
        this.itemCooldownMap.put(location, new CooldownData(cooldown, transaction));
    }

    public void removeCooldown(ResourceLocation location) {
        this.itemCooldownMap.remove(location);
    }
}

