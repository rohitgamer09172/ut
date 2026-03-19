/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.latency;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.Equipment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.shaded.fastutil.ints.IntArraySet;
import ac.grim.grimac.shaded.fastutil.objects.Object2ObjectOpenHashMap;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.ShulkerData;
import ac.grim.grimac.utils.data.TrackerData;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityArmorStand;
import ac.grim.grimac.utils.data.packetentity.PacketEntityCamel;
import ac.grim.grimac.utils.data.packetentity.PacketEntityGuardian;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHappyGhast;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHook;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.data.packetentity.PacketEntityPainting;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySelf;
import ac.grim.grimac.utils.data.packetentity.PacketEntityShulker;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySizeable;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.data.packetentity.PacketEntityTrackXRot;
import ac.grim.grimac.utils.data.packetentity.PacketEntityUnHittable;
import ac.grim.grimac.utils.data.packetentity.dragon.PacketEntityEnderDragon;
import ac.grim.grimac.utils.nmsutil.BoundingBoxSize;
import ac.grim.grimac.utils.nmsutil.WatchableIndexUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public class CompensatedEntities {
    public static final UUID SPRINTING_MODIFIER_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    public static final UUID SNOW_MODIFIER_UUID = UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ce");
    public final Int2ObjectOpenHashMap<PacketEntity> entityMap = new Int2ObjectOpenHashMap(40, 0.7f);
    public final IntArraySet entitiesRemovedThisTick = new IntArraySet();
    public final Int2ObjectOpenHashMap<TrackerData> serverPositionsMap = new Int2ObjectOpenHashMap(40, 0.7f);
    public final Object2ObjectOpenHashMap<UUID, UserProfile> profiles = new Object2ObjectOpenHashMap();
    public Integer serverPlayerVehicle = null;
    public boolean hasSprintingAttributeEnabled = false;
    public TrackerData selfTrackedEntity;
    public PacketEntitySelf self;
    private final GrimPlayer player;

    public CompensatedEntities(GrimPlayer player) {
        this.player = player;
        this.self = new PacketEntitySelf(player);
        this.selfTrackedEntity = new TrackerData(0.0, 0.0, 0.0, 0.0f, 0.0f, EntityTypes.PLAYER, player.lastTransactionSent.get());
    }

    public int getPacketEntityID(PacketEntity entity) {
        for (Map.Entry entry : this.entityMap.int2ObjectEntrySet()) {
            if (entry.getValue() != entity) continue;
            return (Integer)entry.getKey();
        }
        return Integer.MIN_VALUE;
    }

    public void tick() {
        this.self.setPositionRaw(this.player, new SimpleCollisionBox(this.player.x, this.player.y, this.player.z, this.player.x, this.player.y, this.player.z));
        for (PacketEntity vehicle : this.entityMap.values()) {
            for (PacketEntity passenger : vehicle.passengers) {
                this.tickPassenger(vehicle, passenger);
            }
        }
    }

    public void removeEntity(int entityID) {
        PacketEntity entity = this.entityMap.remove(entityID);
        if (entity == null) {
            return;
        }
        if (entity instanceof PacketEntityEnderDragon) {
            PacketEntityEnderDragon dragon = (PacketEntityEnderDragon)entity;
            for (int i = 1; i < dragon.getParts().size() + 1; ++i) {
                this.entityMap.remove(entityID + i);
            }
        }
        for (PacketEntity passenger : new ArrayList<PacketEntity>(entity.passengers)) {
            passenger.eject();
        }
    }

    public OptionalInt getSlowFallingAmplifier() {
        return this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2) ? OptionalInt.empty() : this.getPotionLevelForPlayer(PotionTypes.SLOW_FALLING);
    }

    public OptionalInt getPotionLevelForPlayer(PotionType type) {
        return this.getEntityInControl().getPotionEffectLevel(type);
    }

    public OptionalInt getPotionLevelForSelfPlayer(PotionType type) {
        return this.self.getPotionEffectLevel(type);
    }

    public boolean hasPotionEffect(PotionType type) {
        return this.getEntityInControl().hasPotionEffect(type);
    }

    public PacketEntity getEntityInControl() {
        return this.self.getRiding() != null ? this.self.getRiding() : this.self;
    }

    public void updateAttributes(int entityID, List<WrapperPlayServerUpdateAttributes.Property> objects) {
        PacketEntity entity;
        if (entityID == this.player.entityID) {
            for (WrapperPlayServerUpdateAttributes.Property snapshotWrapper : objects) {
                Attribute attribute = snapshotWrapper.getAttribute();
                if (attribute != Attributes.MOVEMENT_SPEED) continue;
                boolean found = false;
                List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers = snapshotWrapper.getModifiers();
                for (WrapperPlayServerUpdateAttributes.PropertyModifier modifier : modifiers) {
                    ResourceLocation name = modifier.getName();
                    if (!name.getKey().equals(SPRINTING_MODIFIER_UUID.toString()) && !name.getKey().equals("sprinting")) continue;
                    found = true;
                    break;
                }
                this.hasSprintingAttributeEnabled = found;
                break;
            }
        }
        if ((entity = this.player.compensatedEntities.getEntity(entityID)) == null) {
            return;
        }
        for (WrapperPlayServerUpdateAttributes.Property snapshotWrapper : objects) {
            Optional<ValuedAttribute> valuedAttribute;
            Attribute attribute = snapshotWrapper.getAttribute();
            if (attribute == null) continue;
            if (attribute == Attributes.HORSE_JUMP_STRENGTH) {
                attribute = Attributes.JUMP_STRENGTH;
            }
            if ((valuedAttribute = entity.getAttribute(attribute)).isEmpty()) continue;
            valuedAttribute.get().with(snapshotWrapper);
        }
    }

    private void tickPassenger(PacketEntity riding, PacketEntity passenger) {
        if (riding == null || passenger == null) {
            return;
        }
        passenger.setPositionRaw(this.player, riding.getPossibleLocationBoxes().offset(0.0, BoundingBoxSize.getMyRidingOffset(riding) + BoundingBoxSize.getPassengerRidingOffset(this.player, passenger), 0.0));
        for (PacketEntity passengerPassenger : riding.passengers) {
            this.tickPassenger(passenger, passengerPassenger);
        }
    }

    public PacketEntity addEntity(int entityID, UUID uuid, EntityType entityType, Vector3d position, float xRot, int data) {
        if (entityType == EntityTypes.ITEM) {
            return null;
        }
        PacketEntity packetEntity = EntityTypes.isTypeInstanceOf(entityType, EntityTypes.ABSTRACT_NAUTILUS) ? new PacketEntityNautilus(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()) : (EntityTypes.HAPPY_GHAST.equals(entityType) ? new PacketEntityHappyGhast(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot) : (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.CAMEL) ? new PacketEntityCamel(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot) : (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.ABSTRACT_HORSE) ? new PacketEntityHorse(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot) : (entityType == EntityTypes.SLIME || entityType == EntityTypes.MAGMA_CUBE || entityType == EntityTypes.PHANTOM ? new PacketEntitySizeable(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()) : (EntityTypes.PIG.equals(entityType) ? new PacketEntityRideable(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()) : (EntityTypes.SHULKER.equals(entityType) ? new PacketEntityShulker(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()) : (EntityTypes.STRIDER.equals(entityType) ? new PacketEntityStrider(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()) : (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.BOAT) || EntityTypes.CHICKEN.equals(entityType) ? new PacketEntityTrackXRot(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), xRot) : (EntityTypes.FISHING_BOBBER.equals(entityType) ? new PacketEntityHook(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), data) : (EntityTypes.ENDER_DRAGON.equals(entityType) ? new PacketEntityEnderDragon(this.player, uuid, entityID, position.getX(), position.getY(), position.getZ()) : (EntityTypes.isTypeInstanceOf(entityType, EntityTypes.ABSTRACT_ARROW) || EntityTypes.FIREWORK_ROCKET.equals(entityType) || EntityTypes.BLOCK_DISPLAY.equals(entityType) || EntityTypes.TEXT_DISPLAY.equals(entityType) || EntityTypes.LIGHTNING_BOLT.equals(entityType) || EntityTypes.EXPERIENCE_BOTTLE.equals(entityType) || EntityTypes.EXPERIENCE_ORB.equals(entityType) || EntityTypes.EVOKER_FANGS.equals(entityType) ? new PacketEntityUnHittable(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()) : (EntityTypes.ARMOR_STAND.equals(entityType) ? new PacketEntityArmorStand(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ(), data) : (EntityTypes.PAINTING.equals(entityType) ? new PacketEntityPainting(this.player, uuid, position.x, position.y, position.z, Direction.values()[data]) : (EntityTypes.GUARDIAN.equals(entityType) ? new PacketEntityGuardian(this.player, uuid, entityType, position.x, position.y, position.z, false) : (EntityTypes.ELDER_GUARDIAN.equals(entityType) ? new PacketEntityGuardian(this.player, uuid, entityType, position.x, position.y, position.z, true) : new PacketEntity(this.player, uuid, entityType, position.getX(), position.getY(), position.getZ()))))))))))))))));
        this.entityMap.put(entityID, packetEntity);
        return packetEntity;
    }

    public PacketEntity getEntity(int entityID) {
        if (entityID == this.player.entityID) {
            return this.self;
        }
        return this.entityMap.get(entityID);
    }

    public TrackerData getTrackedEntity(int id) {
        if (id == this.player.entityID) {
            return this.selfTrackedEntity;
        }
        return this.serverPositionsMap.get(id);
    }

    public void updateEntityMetadata(int entityID, List<EntityData<?>> watchableObjects) {
        int index;
        EntityData<?> armorStandByte;
        Object gravityObject;
        EntityData<?> gravity;
        int id;
        EntityData<?> ageableObject;
        PacketEntity entity = this.player.compensatedEntities.getEntity(entityID);
        if (entity == null) {
            return;
        }
        if (entity.isAgeable && (ageableObject = WatchableIndexUtil.getIndex(watchableObjects, id = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8) ? 12 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4) ? 11 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2) ? 12 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4) ? 14 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5) ? 15 : 16)))))) != null) {
            Object value = ageableObject.getValue();
            if (value instanceof Boolean) {
                entity.isBaby = (Boolean)value;
            } else if (value instanceof Byte) {
                boolean bl = entity.isBaby = (Byte)value < 0;
            }
        }
        if (entity instanceof PacketEntitySizeable) {
            PacketEntitySizeable sizeable = (PacketEntitySizeable)entity;
            int id2 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8) ? 16 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4) ? 11 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2) ? 12 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4) ? 14 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5) ? 15 : 16))));
            EntityData<?> sizeObject = WatchableIndexUtil.getIndex(watchableObjects, id2);
            if (sizeObject != null) {
                Object value = sizeObject.getValue();
                if (value instanceof Integer) {
                    sizeable.size = (Integer)value;
                } else if (value instanceof Byte) {
                    sizeable.size = ((Byte)value).byteValue();
                }
            }
        }
        if (entity instanceof PacketEntityShulker) {
            EntityData<?> height;
            PacketEntityShulker shulker = (PacketEntityShulker)entity;
            int id3 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4) ? 11 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2) ? 12 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4) ? 14 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5) ? 15 : 16)));
            EntityData<?> shulkerAttached = WatchableIndexUtil.getIndex(watchableObjects, id3);
            if (shulkerAttached != null) {
                shulker.facing = BlockFace.valueOf(shulkerAttached.getValue().toString().toUpperCase());
            }
            if ((height = WatchableIndexUtil.getIndex(watchableObjects, id3 + 2)) != null) {
                if ((Byte)height.getValue() == 0) {
                    data = new ShulkerData(shulker, this.player.lastTransactionSent.get(), true);
                    this.player.compensatedWorld.openShulkerBoxes.remove(data);
                    this.player.compensatedWorld.openShulkerBoxes.add(data);
                } else {
                    data = new ShulkerData(shulker, this.player.lastTransactionSent.get(), false);
                    this.player.compensatedWorld.openShulkerBoxes.remove(data);
                    this.player.compensatedWorld.openShulkerBoxes.add(data);
                }
            }
        }
        if (entity instanceof PacketEntityRideable) {
            EntityData<?> pigSaddle;
            PacketEntityRideable rideable = (PacketEntityRideable)entity;
            int offset = 0;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
                if (entity.type == EntityTypes.PIG && (pigSaddle = WatchableIndexUtil.getIndex(watchableObjects, 16)) != null) {
                    rideable.hasSaddle = (Byte)pigSaddle.getValue() != 0;
                }
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
                offset = 5;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
                offset = 4;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
                offset = 2;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                offset = 1;
            }
            if (entity.type == EntityTypes.PIG) {
                EntityData<?> pigBoost;
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                    offset = 1;
                }
                if ((pigSaddle = WatchableIndexUtil.getIndex(watchableObjects, 17 - offset)) != null) {
                    rideable.hasSaddle = (Boolean)pigSaddle.getValue();
                }
                if ((pigBoost = WatchableIndexUtil.getIndex(watchableObjects, 18 - offset)) != null) {
                    rideable.boostTimeMax = (Integer)pigBoost.getValue();
                    rideable.currentBoostTime = 0;
                }
            } else if (entity instanceof PacketEntityStrider) {
                EntityData<?> striderSaddle;
                EntityData<?> striderShaking;
                EntityData<?> striderBoost = WatchableIndexUtil.getIndex(watchableObjects, 17 - offset);
                if (striderBoost != null) {
                    rideable.boostTimeMax = (Integer)striderBoost.getValue();
                    rideable.currentBoostTime = 0;
                }
                if ((striderShaking = WatchableIndexUtil.getIndex(watchableObjects, 18 - offset)) != null) {
                    ((PacketEntityStrider)rideable).isShaking = (Boolean)striderShaking.getValue();
                }
                if ((striderSaddle = WatchableIndexUtil.getIndex(watchableObjects, 19 - offset)) != null) {
                    rideable.hasSaddle = (Boolean)striderSaddle.getValue();
                }
            }
        }
        if (entity instanceof PacketEntityHorse) {
            PacketEntityHorse horse = (PacketEntityHorse)entity;
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9_4)) {
                int offset = 0;
                if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4)) {
                    offset = 5;
                } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
                    offset = 4;
                } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4)) {
                    offset = 2;
                } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                    offset = 1;
                }
                EntityData<?> horseByte = WatchableIndexUtil.getIndex(watchableObjects, 17 - offset);
                if (horseByte != null) {
                    byte info = (Byte)horseByte.getValue();
                    horse.isTame = (info & 2) != 0;
                    horse.hasSaddle = (info & 4) != 0;
                    boolean bl = horse.isRearing = (info & 0x20) != 0;
                }
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20) && entity instanceof PacketEntityCamel) {
                    PacketEntityCamel camel = (PacketEntityCamel)entity;
                    EntityData<?> entityData = WatchableIndexUtil.getIndex(watchableObjects, 18);
                    if (entityData != null) {
                        camel.setDashing((Boolean)entityData.getValue());
                        camel.setDashCooldown(camel.getDashCooldown() == 0 ? 55 : camel.getDashCooldown());
                    }
                }
            } else {
                EntityData<?> horseByte = WatchableIndexUtil.getIndex(watchableObjects, 16);
                if (horseByte != null) {
                    int info = (Integer)horseByte.getValue();
                    horse.isTame = (info & 2) != 0;
                    horse.hasSaddle = (info & 4) != 0;
                    boolean bl = horse.isRearing = (info & 0x40) != 0;
                }
            }
        }
        if (entity instanceof PacketEntityNautilus) {
            PacketEntityNautilus nautilus = (PacketEntityNautilus)entity;
            EntityData<?> entityData = WatchableIndexUtil.getIndex(watchableObjects, 19);
            if (entityData != null) {
                nautilus.setDashing((Boolean)entityData.getValue());
                nautilus.setDashCooldown(nautilus.getDashCooldown() == 0 ? 40 : nautilus.getDashCooldown());
            }
        }
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9_4) && (gravity = WatchableIndexUtil.getIndex(watchableObjects, 5)) != null && (gravityObject = gravity.getValue()) instanceof Boolean) {
            boolean bl = entity.hasGravity = (Boolean)gravityObject == false;
        }
        if (entity.type == EntityTypes.FIREWORK_ROCKET) {
            int offset = 0;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                offset = 2;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                offset = 1;
            }
            EntityData<?> fireworkWatchableObject = WatchableIndexUtil.getIndex(watchableObjects, 9 - offset);
            if (fireworkWatchableObject == null) {
                return;
            }
            if (fireworkWatchableObject.getValue() instanceof Integer) {
                int attachedEntityID = (Integer)fireworkWatchableObject.getValue();
                if (attachedEntityID == this.player.entityID) {
                    this.player.fireworks.addNewFirework(entityID);
                }
            } else {
                Optional attachedEntityID = (Optional)fireworkWatchableObject.getValue();
                if (attachedEntityID.isPresent() && ((Integer)attachedEntityID.get()).equals(this.player.entityID)) {
                    this.player.fireworks.addNewFirework(entityID);
                }
            }
        }
        if (entity instanceof PacketEntityHook) {
            PacketEntityHook hook = (PacketEntityHook)entity;
            int index2 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4) ? 5 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4) ? 6 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5) ? 7 : 8));
            EntityData<?> hookWatchableObject = WatchableIndexUtil.getIndex(watchableObjects, index2);
            if (hookWatchableObject == null) {
                return;
            }
            Integer attachedEntityID = (Integer)hookWatchableObject.getValue();
            hook.attached = attachedEntityID - 1;
        }
        if (entity instanceof PacketEntityArmorStand && (armorStandByte = WatchableIndexUtil.getIndex(watchableObjects, index = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_9_4) ? 10 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_13_2) ? 11 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_14_4) ? 13 : (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5) ? 14 : 15))))) != null) {
            byte info = (Byte)armorStandByte.getValue();
            entity.isBaby = (info & 1) != 0;
            boolean bl = ((PacketEntityArmorStand)entity).isMarker = (info & 0x10) != 0;
        }
        if (entity instanceof PacketEntityGuardian && PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_11)) {
            int isElderlyBitMask;
            int index3;
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
                index3 = 16;
                isElderlyBitMask = 4;
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_10)) {
                index3 = 11;
                isElderlyBitMask = 4;
            } else {
                index3 = 12;
                isElderlyBitMask = 4;
            }
            EntityData<?> guardianByte = WatchableIndexUtil.getIndex(watchableObjects, index3);
            if (guardianByte != null) {
                int info = (Integer)guardianByte.getValue();
                ((PacketEntityGuardian)entity).isElder = (info & isElderlyBitMask) != 0;
            }
        }
    }

    public void updateEntityEquipment(int entityId, List<Equipment> equipment) {
        PacketEntity entity = this.player.compensatedEntities.getEntity(entityId);
        if (entity == null || !entity.trackEntityEquipment) {
            return;
        }
        for (Equipment equipmentItem : equipment) {
            entity.setItemBySlot(equipmentItem.getSlot(), equipmentItem.getItem());
        }
    }
}

