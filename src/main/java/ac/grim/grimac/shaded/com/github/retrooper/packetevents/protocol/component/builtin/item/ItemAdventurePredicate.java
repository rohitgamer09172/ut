/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.ComponentMatchers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemAdventurePredicate {
    private List<BlockPredicate> predicates;
    @ApiStatus.Obsolete
    private boolean showInTooltip;

    public ItemAdventurePredicate(List<BlockPredicate> predicates) {
        this(predicates, true);
    }

    @ApiStatus.Obsolete
    public ItemAdventurePredicate(List<BlockPredicate> predicates, boolean showInTooltip) {
        this.predicates = predicates;
        this.showInTooltip = showInTooltip;
    }

    public static ItemAdventurePredicate read(PacketWrapper<?> wrapper) {
        List<BlockPredicate> predicates = wrapper.readList(BlockPredicate::read);
        boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
        return new ItemAdventurePredicate(predicates, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemAdventurePredicate predicate) {
        wrapper.writeList(predicate.predicates, BlockPredicate::write);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeBoolean(predicate.showInTooltip);
        }
    }

    public void addPredicate(BlockPredicate predicate) {
        this.predicates.add(predicate);
    }

    public List<BlockPredicate> getPredicates() {
        return this.predicates;
    }

    public void setPredicates(List<BlockPredicate> predicates) {
        this.predicates = predicates;
    }

    @ApiStatus.Obsolete
    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    @ApiStatus.Obsolete
    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemAdventurePredicate)) {
            return false;
        }
        ItemAdventurePredicate that = (ItemAdventurePredicate)obj;
        if (this.showInTooltip != that.showInTooltip) {
            return false;
        }
        return this.predicates.equals(that.predicates);
    }

    public int hashCode() {
        return Objects.hash(this.predicates, this.showInTooltip);
    }

    public static class RangedValueMatcher
    implements ValueMatcher {
        @Nullable
        private String minValue;
        @Nullable
        private String maxValue;

        public RangedValueMatcher(@Nullable String minValue, @Nullable String maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public static RangedValueMatcher read(PacketWrapper<?> wrapper) {
            String minValue = (String)wrapper.readOptional(PacketWrapper::readString);
            String maxValue = (String)wrapper.readOptional(PacketWrapper::readString);
            return new RangedValueMatcher(minValue, maxValue);
        }

        public static void write(PacketWrapper<?> wrapper, RangedValueMatcher matcher) {
            wrapper.writeOptional(matcher.minValue, PacketWrapper::writeString);
            wrapper.writeOptional(matcher.maxValue, PacketWrapper::writeString);
        }

        @Nullable
        public String getMinValue() {
            return this.minValue;
        }

        public void setMinValue(@Nullable String minValue) {
            this.minValue = minValue;
        }

        @Nullable
        public String getMaxValue() {
            return this.maxValue;
        }

        public void setMaxValue(@Nullable String maxValue) {
            this.maxValue = maxValue;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RangedValueMatcher)) {
                return false;
            }
            RangedValueMatcher that = (RangedValueMatcher)obj;
            if (!Objects.equals(this.minValue, that.minValue)) {
                return false;
            }
            return Objects.equals(this.maxValue, that.maxValue);
        }

        public int hashCode() {
            return Objects.hash(this.minValue, this.maxValue);
        }
    }

    public static class ExactValueMatcher
    implements ValueMatcher {
        private String value;

        public ExactValueMatcher(String value) {
            this.value = value;
        }

        public static ExactValueMatcher read(PacketWrapper<?> wrapper) {
            return new ExactValueMatcher(wrapper.readString());
        }

        public static void write(PacketWrapper<?> wrapper, ExactValueMatcher matcher) {
            wrapper.writeString(matcher.value);
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ExactValueMatcher)) {
                return false;
            }
            ExactValueMatcher that = (ExactValueMatcher)obj;
            return this.value.equals(that.value);
        }

        public int hashCode() {
            return Objects.hashCode(this.value);
        }
    }

    public static interface ValueMatcher {
        public static ValueMatcher read(PacketWrapper<?> wrapper) {
            if (wrapper.readBoolean()) {
                return ExactValueMatcher.read(wrapper);
            }
            return RangedValueMatcher.read(wrapper);
        }

        public static void write(PacketWrapper<?> wrapper, ValueMatcher matcher) {
            if (matcher instanceof ExactValueMatcher) {
                wrapper.writeBoolean(true);
                ExactValueMatcher.write(wrapper, (ExactValueMatcher)matcher);
            } else if (matcher instanceof RangedValueMatcher) {
                wrapper.writeBoolean(false);
                RangedValueMatcher.write(wrapper, (RangedValueMatcher)matcher);
            } else {
                throw new IllegalArgumentException("Illegal matcher implementation: " + matcher);
            }
        }
    }

    public static class PropertyMatcher {
        private String name;
        private ValueMatcher matcher;

        public PropertyMatcher(String name, ValueMatcher matcher) {
            this.name = name;
            this.matcher = matcher;
        }

        public static PropertyMatcher read(PacketWrapper<?> wrapper) {
            String name = wrapper.readString();
            ValueMatcher matcher = ValueMatcher.read(wrapper);
            return new PropertyMatcher(name, matcher);
        }

        public static void write(PacketWrapper<?> wrapper, PropertyMatcher matcher) {
            wrapper.writeString(matcher.name);
            ValueMatcher.write(wrapper, matcher.matcher);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ValueMatcher getMatcher() {
            return this.matcher;
        }

        public void setMatcher(ValueMatcher matcher) {
            this.matcher = matcher;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PropertyMatcher)) {
                return false;
            }
            PropertyMatcher that = (PropertyMatcher)obj;
            if (!this.name.equals(that.name)) {
                return false;
            }
            return this.matcher.equals(that.matcher);
        }

        public int hashCode() {
            return Objects.hash(this.name, this.matcher);
        }
    }

    public static class BlockPredicate {
        @Nullable
        private MappedEntitySet<StateType.Mapped> blocks;
        @Nullable
        private List<PropertyMatcher> properties;
        @Nullable
        private NBTCompound nbt;
        private ComponentMatchers matchers;

        public BlockPredicate(@Nullable MappedEntitySet<StateType.Mapped> blocks, @Nullable List<PropertyMatcher> properties, @Nullable NBTCompound nbt) {
            this(blocks, properties, nbt, new ComponentMatchers());
        }

        public BlockPredicate(@Nullable MappedEntitySet<StateType.Mapped> blocks, @Nullable List<PropertyMatcher> properties, @Nullable NBTCompound nbt, ComponentMatchers matchers) {
            this.blocks = blocks;
            this.properties = properties;
            this.nbt = nbt;
            this.matchers = matchers;
        }

        public static BlockPredicate read(PacketWrapper<?> wrapper) {
            MappedEntitySet blocks = (MappedEntitySet)wrapper.readOptional(ew -> MappedEntitySet.read(ew, StateTypes::getMappedById));
            List properties = (List)wrapper.readOptional(ew -> wrapper.readList(PropertyMatcher::read));
            NBTCompound nbt = (NBTCompound)wrapper.readOptional(PacketWrapper::readNBT);
            ComponentMatchers matchers = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? ComponentMatchers.read(wrapper) : new ComponentMatchers();
            return new BlockPredicate(blocks, properties, nbt, matchers);
        }

        public static void write(PacketWrapper<?> wrapper, BlockPredicate predicate) {
            wrapper.writeOptional(predicate.blocks, MappedEntitySet::write);
            wrapper.writeOptional(predicate.properties, (ew, val) -> ew.writeList(val, PropertyMatcher::write));
            wrapper.writeOptional(predicate.nbt, PacketWrapper::writeNBT);
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                ComponentMatchers.write(wrapper, predicate.matchers);
            }
        }

        @Nullable
        public MappedEntitySet<StateType.Mapped> getBlocks() {
            return this.blocks;
        }

        public void setBlocks(@Nullable MappedEntitySet<StateType.Mapped> blocks) {
            this.blocks = blocks;
        }

        public void addProperty(PropertyMatcher propertyMatcher) {
            if (this.properties == null) {
                this.properties = new ArrayList<PropertyMatcher>(4);
            }
            this.properties.add(propertyMatcher);
        }

        @Nullable
        public List<PropertyMatcher> getProperties() {
            return this.properties;
        }

        public void setProperties(@Nullable List<PropertyMatcher> properties) {
            this.properties = properties;
        }

        @Nullable
        public NBTCompound getNbt() {
            return this.nbt;
        }

        public void setNbt(@Nullable NBTCompound nbt) {
            this.nbt = nbt;
        }

        public ComponentMatchers getMatchers() {
            return this.matchers;
        }

        public void setMatchers(ComponentMatchers matchers) {
            this.matchers = matchers;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof BlockPredicate)) {
                return false;
            }
            BlockPredicate that = (BlockPredicate)obj;
            if (!Objects.equals(this.blocks, that.blocks)) {
                return false;
            }
            if (!Objects.equals(this.properties, that.properties)) {
                return false;
            }
            if (!Objects.equals(this.nbt, that.nbt)) {
                return false;
            }
            return this.matchers.equals(that.matchers);
        }

        public int hashCode() {
            return Objects.hash(this.blocks, this.properties, this.nbt, this.matchers);
        }
    }
}

