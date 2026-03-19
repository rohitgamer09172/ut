/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemDamageType {
    private MaybeMappedEntity<DamageType> damageType;

    public ItemDamageType(DamageType damageType) {
        this(new MaybeMappedEntity<DamageType>(damageType));
    }

    public ItemDamageType(MaybeMappedEntity<DamageType> damageType) {
        this.damageType = damageType;
    }

    public static ItemDamageType read(PacketWrapper<?> wrapper) {
        MaybeMappedEntity<DamageType> damageType = MaybeMappedEntity.read(wrapper, DamageTypes.getRegistry(), DamageType::read);
        return new ItemDamageType(damageType);
    }

    public static void write(PacketWrapper<?> wrapper, ItemDamageType component) {
        MaybeMappedEntity.write(wrapper, component.damageType, DamageType::write);
    }

    public MaybeMappedEntity<DamageType> getDamageType() {
        return this.damageType;
    }

    public void setDamageType(DamageType damageType) {
        this.setDamageType(new MaybeMappedEntity<DamageType>(damageType));
    }

    public void setDamageType(MaybeMappedEntity<DamageType> damageType) {
        this.damageType = damageType;
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ItemDamageType that = (ItemDamageType)obj;
        return this.damageType.equals(that.damageType);
    }

    public int hashCode() {
        return Objects.hashCode(this.damageType);
    }
}

