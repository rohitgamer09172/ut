/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

public class EntityTypeTags {
    public static final EntityTag CAN_FLOAT_WHILE_RIDDEN = new EntityTag(EntityTypes.HORSE, EntityTypes.ZOMBIE_HORSE, EntityTypes.MULE, EntityTypes.DONKEY, EntityTypes.CAMEL, EntityTypes.CAMEL_HUSK);

    public record EntityTag(EntityType[] tags) {
        public boolean anyOf(EntityType tested) {
            for (EntityType type : this.tags) {
                if (!tested.isInstanceOf(type)) continue;
                return true;
            }
            return false;
        }
    }
}

