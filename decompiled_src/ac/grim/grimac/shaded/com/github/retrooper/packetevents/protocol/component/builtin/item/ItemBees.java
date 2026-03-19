/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TypedEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ItemBees {
    private List<BeeEntry> bees;

    public ItemBees(List<BeeEntry> bees) {
        this.bees = bees;
    }

    public static ItemBees read(PacketWrapper<?> wrapper) {
        List<BeeEntry> bees = wrapper.readList(BeeEntry::read);
        return new ItemBees(bees);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBees bees) {
        wrapper.writeList(bees.bees, BeeEntry::write);
    }

    public void addBee(BeeEntry bee) {
        this.bees.add(bee);
    }

    public List<BeeEntry> getBees() {
        return this.bees;
    }

    public void setBees(List<BeeEntry> bees) {
        this.bees = bees;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemBees)) {
            return false;
        }
        ItemBees itemBees = (ItemBees)obj;
        return this.bees.equals(itemBees.bees);
    }

    public int hashCode() {
        return Objects.hashCode(this.bees);
    }

    public static class BeeEntry {
        private TypedEntityData entityData;
        private int ticksInHive;
        private int minTicksInHive;

        @Deprecated
        public BeeEntry(NBTCompound entityData, int ticksInHive, int minTicksInHive) {
            this(new TypedEntityData(entityData), ticksInHive, minTicksInHive);
        }

        public BeeEntry(TypedEntityData entityData, int ticksInHive, int minTicksInHive) {
            this.entityData = entityData;
            this.ticksInHive = ticksInHive;
            this.minTicksInHive = minTicksInHive;
        }

        public static BeeEntry read(PacketWrapper<?> wrapper) {
            TypedEntityData entityData = TypedEntityData.read(wrapper);
            int ticksInHive = wrapper.readVarInt();
            int minTicksInHive = wrapper.readVarInt();
            return new BeeEntry(entityData, ticksInHive, minTicksInHive);
        }

        public static void write(PacketWrapper<?> wrapper, BeeEntry bee) {
            TypedEntityData.write(wrapper, bee.entityData);
            wrapper.writeVarInt(bee.ticksInHive);
            wrapper.writeVarInt(bee.minTicksInHive);
        }

        public TypedEntityData getTypedEntityData() {
            return this.entityData;
        }

        public void setTypedEntityData(TypedEntityData entityData) {
            this.entityData = entityData;
        }

        public NBTCompound getEntityData() {
            return this.entityData.getCompound();
        }

        public void setEntityData(NBTCompound entityData) {
            this.entityData = new TypedEntityData(entityData);
        }

        public int getTicksInHive() {
            return this.ticksInHive;
        }

        public void setTicksInHive(int ticksInHive) {
            this.ticksInHive = ticksInHive;
        }

        public int getMinTicksInHive() {
            return this.minTicksInHive;
        }

        public void setMinTicksInHive(int minTicksInHive) {
            this.minTicksInHive = minTicksInHive;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof BeeEntry)) {
                return false;
            }
            BeeEntry beeEntry = (BeeEntry)obj;
            if (this.ticksInHive != beeEntry.ticksInHive) {
                return false;
            }
            if (this.minTicksInHive != beeEntry.minTicksInHive) {
                return false;
            }
            return this.entityData.equals(beeEntry.entityData);
        }

        public int hashCode() {
            return Objects.hash(this.entityData, this.ticksInHive, this.minTicksInHive);
        }
    }
}

