/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.fastutil.objects.Object2IntMap;
import ac.grim.grimac.shaded.fastutil.objects.Object2IntOpenHashMap;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.ReachInterpolationData;
import ac.grim.grimac.utils.data.TrackedPosition;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.TypedPacketEntity;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import lombok.Generated;

public class PacketEntity
extends TypedPacketEntity {
    public final TrackedPosition trackedServerPosition;
    protected final Map<Attribute, ValuedAttribute> attributeMap = new IdentityHashMap<Attribute, ValuedAttribute>();
    private final UUID uuid;
    public PacketEntity riding;
    public final List<PacketEntity> passengers = new ArrayList<PacketEntity>(0);
    public boolean isDead = false;
    public boolean isBaby = false;
    public boolean hasGravity = true;
    private ReachInterpolationData oldPacketLocation;
    private ReachInterpolationData newPacketLocation;
    private Object2IntMap<PotionType> potionsMap = null;
    public boolean trackEntityEquipment = false;
    private EnumMap<EquipmentSlot, ItemStack> equipment = null;

    public PacketEntity(GrimPlayer player, EntityType type) {
        super(type);
        this.uuid = null;
        this.initAttributes(player);
        this.trackedServerPosition = new TrackedPosition();
    }

    public PacketEntity(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
        super(type);
        this.uuid = uuid;
        this.initAttributes(player);
        this.trackedServerPosition = new TrackedPosition();
        this.trackedServerPosition.setPos(new Vector3d(x, y, z));
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
            this.trackedServerPosition.setPos(new Vector3d((double)((int)(x * 32.0)) / 32.0, (double)((int)(y * 32.0)) / 32.0, (double)((int)(z * 32.0)) / 32.0));
        }
        Vector3d pos = this.trackedServerPosition.getPos();
        this.newPacketLocation = new ReachInterpolationData(player, new SimpleCollisionBox(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z, false), this.trackedServerPosition, this);
    }

    protected void trackAttribute(ValuedAttribute valuedAttribute) {
        if (this.attributeMap.containsKey(valuedAttribute.attribute())) {
            throw new IllegalArgumentException("Attribute already exists on entity!");
        }
        this.attributeMap.put(valuedAttribute.attribute(), valuedAttribute);
    }

    protected void initAttributes(GrimPlayer player) {
        this.trackAttribute(ValuedAttribute.ranged(Attributes.SCALE, 1.0, 0.0625, 16.0).requiredVersion(player, ClientVersion.V_1_20_5));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.STEP_HEIGHT, 0.6f, 0.0, 10.0).requiredVersion(player, ClientVersion.V_1_20_5));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.GRAVITY, 0.08, -1.0, 1.0).requiredVersion(player, ClientVersion.V_1_20_5));
    }

    public Optional<ValuedAttribute> getAttribute(Attribute attribute) {
        if (attribute == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.attributeMap.get(attribute));
    }

    public void setAttribute(Attribute attribute, double value) {
        ValuedAttribute property = this.attributeMap.get(attribute);
        if (property == null) {
            throw new IllegalArgumentException("Cannot set attribute " + String.valueOf(attribute.getName()) + " for entity " + String.valueOf(this.type.getName()) + "!");
        }
        property.override(value);
    }

    public double getAttributeValue(Attribute attribute) {
        ValuedAttribute property = this.attributeMap.get(attribute);
        if (property == null) {
            throw new IllegalArgumentException("Cannot get attribute " + String.valueOf(attribute.getName()) + " for entity " + String.valueOf(this.type.getName()) + "!");
        }
        return property.get();
    }

    public void resetAttributes() {
        this.attributeMap.values().forEach(ValuedAttribute::reset);
    }

    public void onFirstTransaction(boolean relative, boolean hasPos, double relX, double relY, double relZ, GrimPlayer player) {
        SimpleCollisionBox clientArea;
        if (hasPos) {
            if (relative) {
                double scale = this.trackedServerPosition.getScale();
                Vector3d vec3d = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) ? this.trackedServerPosition.withDelta(TrackedPosition.pack(relX, scale), TrackedPosition.pack(relY, scale), TrackedPosition.pack(relZ, scale)) : this.trackedServerPosition.withDeltaLegacy(TrackedPosition.packLegacy(relX, scale), TrackedPosition.packLegacy(relY, scale), TrackedPosition.packLegacy(relZ, scale));
                this.trackedServerPosition.setPos(vec3d);
            } else {
                this.trackedServerPosition.setPos(new Vector3d(relX, relY, relZ));
                if (player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
                    this.trackedServerPosition.setPos(new Vector3d((double)((int)(relX * 32.0)) / 32.0, (double)((int)(relY * 32.0)) / 32.0, (double)((int)(relZ * 32.0)) / 32.0));
                }
            }
        }
        this.oldPacketLocation = this.newPacketLocation;
        this.newPacketLocation = !hasPos && (player.getClientVersion().isOlderThan(ClientVersion.V_1_21_9) && player.getClientVersion().isNewerThan(ClientVersion.V_1_21_4) || player.getClientVersion().isOlderThan(ClientVersion.V_1_20_2) && player.getClientVersion().isNewerThan(ClientVersion.V_1_14_4)) ? new ReachInterpolationData(player, this.oldPacketLocation.getPossibleLocationCombined(), this) : new ReachInterpolationData(player, this.oldPacketLocation.getPossibleLocationCombined(), this.trackedServerPosition, this);
        if (hasPos && !relative && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_16_1) && (clientArea = this.newPacketLocation.getPossibleLocationCombined()).distanceX(relX) < 0.03125 && clientArea.distanceY(relY) < 0.015625 && clientArea.distanceZ(relZ) < 0.03125) {
            this.newPacketLocation.expandNonRelative();
        }
    }

    public void onSecondTransaction() {
        this.oldPacketLocation = null;
    }

    public void onMovement(boolean tickingReliably) {
        this.newPacketLocation.tickMovement(this.oldPacketLocation == null, tickingReliably);
        if (this.oldPacketLocation != null) {
            this.oldPacketLocation.tickMovement(true, tickingReliably);
            this.newPacketLocation.updatePossibleStartingLocation(this.oldPacketLocation.getPossibleLocationCombined());
        }
    }

    public boolean hasPassenger(PacketEntity entity) {
        return this.passengers.contains(entity);
    }

    public void mount(PacketEntity vehicle) {
        if (this.riding != null) {
            this.eject();
        }
        vehicle.passengers.add(this);
        this.riding = vehicle;
    }

    public void eject() {
        if (this.riding != null) {
            this.riding.passengers.remove(this);
        }
        this.riding = null;
    }

    public void setPositionRaw(GrimPlayer player, SimpleCollisionBox box) {
        this.trackedServerPosition.setPos(new Vector3d((box.maxX - box.minX) / 2.0 + box.minX, box.minY, (box.maxZ - box.minZ) / 2.0 + box.minZ));
        this.newPacketLocation = new ReachInterpolationData(player, box, this);
    }

    public SimpleCollisionBox getPossibleLocationBoxes() {
        if (this.oldPacketLocation == null) {
            return this.newPacketLocation.getPossibleLocationCombined();
        }
        return ReachInterpolationData.combineCollisionBox(this.oldPacketLocation.getPossibleLocationCombined(), this.newPacketLocation.getPossibleLocationCombined());
    }

    public SimpleCollisionBox getPossibleCollisionBoxes() {
        if (this.oldPacketLocation == null) {
            return this.newPacketLocation.getPossibleHitboxCombined();
        }
        return ReachInterpolationData.combineCollisionBox(this.oldPacketLocation.getPossibleHitboxCombined(), this.newPacketLocation.getPossibleHitboxCombined());
    }

    public OptionalInt getPotionEffectLevel(PotionType effect) {
        int amplifier = this.potionsMap == null ? -1 : this.potionsMap.getInt(effect);
        return amplifier == -1 ? OptionalInt.empty() : OptionalInt.of(amplifier);
    }

    public boolean hasPotionEffect(PotionType effect) {
        return this.potionsMap != null && this.potionsMap.containsKey(effect);
    }

    public void addPotionEffect(PotionType effect, int amplifier) {
        if (this.potionsMap == null) {
            this.potionsMap = new Object2IntOpenHashMap<PotionType>();
            this.potionsMap.defaultReturnValue(-1);
        }
        this.potionsMap.put(effect, amplifier);
    }

    public void removePotionEffect(PotionType effect) {
        if (this.potionsMap == null) {
            return;
        }
        this.potionsMap.removeInt(effect);
    }

    public boolean canHit() {
        return !this.isDead;
    }

    public void setItemBySlot(EquipmentSlot slot, ItemStack item) {
        if (item == ItemStack.EMPTY && this.getItemBySlot(slot) == ItemStack.EMPTY) {
            return;
        }
        if (this.equipment == null) {
            this.equipment = new EnumMap(EquipmentSlot.class);
        }
        this.equipment.put(slot, item);
    }

    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (this.equipment == null) {
            return ItemStack.EMPTY;
        }
        return this.equipment.getOrDefault((Object)slot, ItemStack.EMPTY);
    }

    public boolean hasItemInSlot(EquipmentSlot slot) {
        if (this.equipment == null) {
            return false;
        }
        ItemStack item = this.equipment.get((Object)slot);
        return item != null && !item.isEmpty();
    }

    @Generated
    public UUID getUuid() {
        return this.uuid;
    }

    @Generated
    public PacketEntity getRiding() {
        return this.riding;
    }
}

