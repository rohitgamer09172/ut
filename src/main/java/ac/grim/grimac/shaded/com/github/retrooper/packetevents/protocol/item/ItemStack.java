/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemStack {
    public static final ItemStack EMPTY = ItemStack.builder().nbt(new NBTCompound()).build();
    private final ClientVersion version;
    private final IRegistryHolder registryHolder;
    private final ItemType type;
    private int amount;
    @ApiStatus.Obsolete
    private @Nullable NBTCompound nbt;
    private @Nullable PatchableComponentMap components;
    @ApiStatus.Obsolete
    private int legacyData;

    private ItemStack(ItemType type, int amount, @Nullable NBTCompound nbt, @Nullable PatchableComponentMap components, int legacyData, ClientVersion version, IRegistryHolder registryHolder) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
        this.components = components;
        this.legacyData = legacyData;
        this.version = version;
        this.registryHolder = registryHolder;
    }

    public static ItemStack decode(NBT nbt, PacketWrapper<?> wrapper) {
        return ItemStack.decode(nbt, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static ItemStack decode(NBT nbt, ClientVersion version) {
        if (nbt instanceof NBTString) {
            ResourceLocation itemName = new ResourceLocation(((NBTString)nbt).getValue());
            return ItemStack.builder().type(ItemTypes.getByName(itemName.toString())).build();
        }
        NBTCompound compound = (NBTCompound)nbt;
        Builder builder = ItemStack.builder();
        ResourceLocation itemName = Optional.ofNullable(compound.getStringTagValueOrNull("id")).map(Optional::of).orElseGet(() -> Optional.ofNullable(compound.getStringTagValueOrNull("item"))).map(ResourceLocation::new).orElseThrow(() -> new IllegalArgumentException("No item type specified: " + compound.getTags().keySet()));
        builder.type(ItemTypes.getByName(itemName.toString()));
        builder.nbt(compound.getCompoundTagOrNull("tag"));
        Optional.ofNullable(compound.getNumberTagOrNull("Count")).map(Optional::of).orElseGet(() -> Optional.ofNullable(compound.getNumberTagOrNull("count"))).map(NBTNumber::getAsInt).ifPresent(builder::amount);
        return builder.build();
    }

    public static NBT encode(PacketWrapper<?> wrapper, ItemStack itemStack) {
        return ItemStack.encodeForParticle(itemStack, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static NBT encodeForParticle(ItemStack itemStack, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            boolean simple;
            boolean bl = simple = itemStack.isEmpty() || itemStack.components == null || itemStack.components.getPatches().isEmpty();
            if (simple) {
                return new NBTString(itemStack.type.getName().toString());
            }
        }
        NBTCompound compound = new NBTCompound();
        compound.setTag("id", new NBTString(itemStack.type.getName().toString()));
        if (version.isOlderThan(ClientVersion.V_1_20_5)) {
            compound.setTag("Count", new NBTInt(itemStack.getAmount()));
            if (itemStack.nbt != null) {
                compound.setTag("tag", itemStack.nbt);
            }
        }
        return compound;
    }

    public int getMaxStackSize() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.getComponentOr(ComponentTypes.MAX_STACK_SIZE, 1);
        }
        return this.getType().getMaxAmount();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
    }

    public boolean isDamageableItem() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.hasComponent(ComponentTypes.MAX_DAMAGE) && !this.hasComponent(ComponentTypes.UNBREAKABLE_MODERN) && this.hasComponent(ComponentTypes.DAMAGE);
        }
        return !this.isEmpty() && this.getMaxDamage() > 0 && (this.nbt == null || !this.nbt.getBoolean("Unbreakable"));
    }

    public boolean isDamaged() {
        return this.isDamageableItem() && this.getDamageValue() > 0;
    }

    public int getDamageValue() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            int value = this.getComponentOr(ComponentTypes.DAMAGE, 0);
            return MathUtil.clamp(value, 0, this.getMaxDamage());
        }
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            NBTNumber damage = this.nbt != null ? this.nbt.getNumberTagOrNull("Damage") : null;
            return damage == null ? 0 : damage.getAsInt();
        }
        return Math.max(0, this.legacyData);
    }

    public void setDamageValue(int damage) {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            this.setComponent(ComponentTypes.DAMAGE, MathUtil.clamp(damage, 0, this.getMaxDamage()));
        } else if (this.version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            this.getOrCreateTag().setTag("Damage", new NBTInt(Math.max(0, damage)));
        } else {
            this.legacyData = Math.max(0, damage);
        }
    }

    public int getMaxDamage() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.getComponentOr(ComponentTypes.MAX_DAMAGE, 0);
        }
        return this.getType().getMaxDurability();
    }

    public NBTCompound getOrCreateTag() {
        if (this.nbt == null) {
            this.nbt = new NBTCompound();
        }
        return this.nbt;
    }

    public ItemType getType() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
            return this.isEmpty() ? ItemTypes.AIR : this.type;
        }
        return this.type;
    }

    public int getAmount() {
        if (this.version.isNewerThanOrEquals(ClientVersion.V_1_11)) {
            return this.isEmpty() ? 0 : this.amount;
        }
        return this.amount;
    }

    public void shrink(int amount) {
        this.amount -= amount;
    }

    public void grow(int amount) {
        this.amount += amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack split(int toTake) {
        int i = Math.min(toTake, this.getAmount());
        ItemStack itemstack = this.copy();
        itemstack.setAmount(i);
        this.shrink(i);
        return itemstack;
    }

    public ItemStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        }
        return new ItemStack(this.type, this.amount, this.nbt == null ? null : this.nbt.copy(), this.components == null ? null : this.components.copy(), this.legacyData, this.version, this.registryHolder);
    }

    public @Nullable NBTCompound getNBT() {
        return this.nbt;
    }

    public void setNBT(NBTCompound nbt) {
        this.nbt = nbt;
    }

    public <T> T getComponentOr(ComponentType<T> type, T otherValue) {
        if (this.hasComponentPatches()) {
            return this.getComponents().getOr(type, otherValue);
        }
        return this.getType().getComponents().getOr(type, otherValue);
    }

    public <T> Optional<T> getComponent(ComponentType<T> type) {
        if (this.hasComponentPatches()) {
            return this.getComponents().getOptional(type);
        }
        return this.getType().getComponents().getOptional(type);
    }

    public <T> void setComponent(ComponentType<T> type, T value) {
        this.getComponents().set(type, value);
    }

    public <T> void unsetComponent(ComponentType<T> type) {
        this.getComponents().unset(type);
    }

    public <T> void setComponent(ComponentType<T> type, Optional<T> value) {
        this.getComponents().set(type, value);
    }

    public boolean hasComponent(ComponentType<?> type) {
        if (this.hasComponentPatches()) {
            return this.getComponents().has(type);
        }
        return this.getType().getComponents().has(type);
    }

    public boolean hasComponentPatches() {
        return this.components != null && !this.components.getPatches().isEmpty();
    }

    public PatchableComponentMap getComponents() {
        if (this.components == null) {
            this.components = new PatchableComponentMap(this.type.getComponents(), new HashMap(4));
        }
        return this.components;
    }

    public void setComponents(@Nullable PatchableComponentMap components) {
        this.components = components;
    }

    public int getLegacyData() {
        return this.legacyData;
    }

    public void setLegacyData(int legacyData) {
        this.legacyData = legacyData;
    }

    public boolean isEnchantable() {
        return this.isEnchantable(this.version);
    }

    @Deprecated
    public boolean isEnchantable(ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return this.hasComponent(ComponentTypes.ENCHANTABLE) && !this.isEnchanted(version);
        }
        if (this.type == ItemTypes.BOOK) {
            return this.getAmount() == 1;
        }
        if (this.type == ItemTypes.ENCHANTED_BOOK) {
            return false;
        }
        return this.getMaxStackSize() == 1 && this.canBeDepleted() && !this.isEnchanted(version);
    }

    public boolean isEnchanted() {
        return this.isEnchanted(this.version);
    }

    @Deprecated
    public boolean isEnchanted(ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return !this.getComponentOr(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty() || !this.getComponentOr(ComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
        }
        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> enchantments = this.nbt.getCompoundListTagOrNull(tagName);
            return enchantments != null && !enchantments.getTags().isEmpty();
        }
        return false;
    }

    public List<Enchantment> getEnchantments() {
        return this.getEnchantments(this.version);
    }

    @Deprecated
    public List<Enchantment> getEnchantments(ClientVersion version) {
        String tagName;
        NBTList<NBTCompound> nbtList;
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            ItemEnchantments enchantmentsComp = this.getComponentOr(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments storedEnchantmentsComp = this.getComponentOr(ComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            ArrayList<Enchantment> enchantmentsList = new ArrayList<Enchantment>(enchantmentsComp.getEnchantmentCount() + storedEnchantmentsComp.getEnchantmentCount());
            for (Map.Entry<EnchantmentType, Integer> enchantment : enchantmentsComp) {
                enchantmentsList.add(new Enchantment(enchantment.getKey(), enchantment.getValue()));
            }
            for (Map.Entry<EnchantmentType, Integer> enchantment : storedEnchantmentsComp) {
                enchantmentsList.add(new Enchantment(enchantment.getKey(), enchantment.getValue()));
            }
            return enchantmentsList;
        }
        if (this.nbt != null && (nbtList = this.nbt.getCompoundListTagOrNull(tagName = this.getEnchantmentsTagName(version))) != null) {
            List<NBTCompound> compounds = nbtList.getTags();
            ArrayList<Enchantment> enchantments = new ArrayList<Enchantment>(compounds.size());
            for (NBTCompound compound : compounds) {
                NBTNumber levelTag;
                EnchantmentType type = ItemStack.getEnchantmentTypeFromTag(compound, version);
                if (type == null || (levelTag = compound.getNumberTagOrNull("lvl")) == null) continue;
                int level = levelTag.getAsInt();
                Enchantment enchantment = Enchantment.builder().type(type).level(level).build();
                enchantments.add(enchantment);
            }
            return enchantments;
        }
        return new ArrayList<Enchantment>(0);
    }

    public int getEnchantmentLevel(EnchantmentType enchantment) {
        return this.getEnchantmentLevel(enchantment, this.version);
    }

    @Deprecated
    public int getEnchantmentLevel(EnchantmentType enchantment, ClientVersion version) {
        String tagName;
        NBTList<NBTCompound> nbtList;
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            int level;
            ItemEnchantments enchantmentsComp = this.getComponentOr(ComponentTypes.ENCHANTMENTS, ItemEnchantments.EMPTY);
            if (!enchantmentsComp.isEmpty() && (level = enchantmentsComp.getEnchantmentLevel(enchantment)) > 0) {
                return level;
            }
            ItemEnchantments storedEnchantmentsComp = this.getComponentOr(ComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            if (!storedEnchantmentsComp.isEmpty()) {
                return storedEnchantmentsComp.getEnchantmentLevel(enchantment);
            }
            return 0;
        }
        if (this.nbt != null && (nbtList = this.nbt.getCompoundListTagOrNull(tagName = this.getEnchantmentsTagName(version))) != null) {
            for (NBTCompound base : nbtList.getTags()) {
                EnchantmentType type = ItemStack.getEnchantmentTypeFromTag(base, version);
                if (!Objects.equals(type, enchantment)) continue;
                NBTNumber nbtLevel = base.getNumberTagOrNull("lvl");
                return nbtLevel != null ? nbtLevel.getAsInt() : 0;
            }
        }
        return 0;
    }

    private static @Nullable EnchantmentType getEnchantmentTypeFromTag(NBTCompound tag, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            String id = tag.getStringTagValueOrNull("id");
            return EnchantmentTypes.getByName(id);
        }
        NBTShort idTag = tag.getTagOfTypeOrNull("id", NBTShort.class);
        return idTag != null ? EnchantmentTypes.getById(version, idTag.getAsInt()) : null;
    }

    public void setEnchantments(List<Enchantment> enchantments) {
        this.setEnchantments(enchantments, this.version);
    }

    @Deprecated
    public void setEnchantments(List<Enchantment> enchantments, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            HashMap<EnchantmentType, Integer> enchantmentsMap = new HashMap<EnchantmentType, Integer>(enchantments.size());
            for (Enchantment enchantment : enchantments) {
                enchantmentsMap.put(enchantment.getType(), enchantment.getLevel());
            }
            ComponentType<ItemEnchantments> componentType = this.hasComponent(ComponentTypes.STORED_ENCHANTMENTS) ? ComponentTypes.STORED_ENCHANTMENTS : ComponentTypes.ENCHANTMENTS;
            Optional<ItemEnchantments> prevEnchantments = this.getComponent(componentType);
            boolean showInTooltip = prevEnchantments.map(ItemEnchantments::isShowInTooltip).orElse(true);
            this.setComponent(componentType, new ItemEnchantments(enchantmentsMap, showInTooltip));
        } else {
            String tagName = this.getEnchantmentsTagName(version);
            if (enchantments.isEmpty()) {
                if (this.nbt != null && this.nbt.getTagOrNull(tagName) != null) {
                    this.nbt.removeTag(tagName);
                }
            } else {
                ArrayList<NBTCompound> list = new ArrayList<NBTCompound>();
                for (Enchantment enchantment : enchantments) {
                    NBTCompound compound = new NBTCompound();
                    if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
                        compound.setTag("id", new NBTString(enchantment.getType().getName().toString()));
                    } else {
                        compound.setTag("id", new NBTShort((short)enchantment.getType().getId(version)));
                    }
                    compound.setTag("lvl", new NBTShort((short)enchantment.getLevel()));
                    list.add(compound);
                }
                this.getOrCreateTag().setTag(tagName, new NBTList<NBTCompound>(NBTType.COMPOUND, list));
            }
        }
    }

    @Deprecated
    public String getEnchantmentsTagName(ClientVersion version) {
        String tagName;
        String string = tagName = version.isNewerThanOrEquals(ClientVersion.V_1_13) ? "Enchantments" : "ench";
        if (this.type == ItemTypes.ENCHANTED_BOOK) {
            tagName = "StoredEnchantments";
        }
        return tagName;
    }

    public boolean canBeDepleted() {
        return this.getMaxDamage() > 0;
    }

    public boolean is(ItemType type) {
        return this.getType() == type;
    }

    public static boolean isSameItemSameTags(ItemStack stack, ItemStack otherStack) {
        return ItemStack.isSameItemSameComponents(stack, otherStack);
    }

    public static boolean isSameItemSameComponents(ItemStack stack, ItemStack otherStack) {
        if (stack.version != otherStack.version) {
            throw new IllegalArgumentException("Can't compare two ItemStacks across versions: " + (Object)((Object)stack.version) + " != " + (Object)((Object)otherStack.version));
        }
        if (stack.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return stack.is(otherStack.getType()) && (stack.isEmpty() && otherStack.isEmpty() || stack.getComponents().equals(otherStack.getComponents()));
        }
        return stack.is(otherStack.getType()) && (stack.isEmpty() && otherStack.isEmpty() || Objects.equals(stack.nbt, otherStack.nbt));
    }

    public static boolean tagMatches(@Nullable ItemStack stack, @Nullable ItemStack otherStack) {
        if (stack == otherStack) {
            return true;
        }
        if (stack == null) {
            return otherStack.isEmpty();
        }
        if (otherStack == null) {
            return stack.isEmpty();
        }
        if (stack.version != otherStack.version) {
            throw new IllegalArgumentException("Can't compare two ItemStacks across versions: " + (Object)((Object)stack.version) + " != " + (Object)((Object)otherStack.version));
        }
        if (stack.version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            return stack.getComponents().equals(otherStack.getComponents());
        }
        return Objects.equals(stack.nbt, otherStack.nbt);
    }

    public boolean isEmpty() {
        boolean baseEmpty;
        boolean bl = baseEmpty = this.type == ItemTypes.AIR || this.amount <= 0;
        if (this.version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            return baseEmpty || this.legacyData < Short.MIN_VALUE || this.legacyData > 65536;
        }
        return baseEmpty;
    }

    public ClientVersion getVersion() {
        return this.version;
    }

    public IRegistryHolder getRegistryHolder() {
        return this.registryHolder;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack)obj;
            return this.type.equals(itemStack.type) && this.amount == itemStack.amount && Objects.equals(this.nbt, itemStack.nbt) && Objects.equals(this.components, itemStack.components) && this.legacyData == itemStack.legacyData;
        }
        return false;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "ItemStack[EMPTY]";
        }
        return "ItemStack[" + this.getAmount() + "x/" + this.getMaxStackSize() + "x " + this.type.getName() + (this.nbt != null ? ", nbt tag names=" + this.nbt.getTagNames() : "") + (this.legacyData != -1 ? ", legacy data=" + this.legacyData : "") + (this.components != null ? ", components=" + this.components.getPatches() : "") + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        private IRegistryHolder registryHolder = GlobalRegistryHolder.INSTANCE;
        private ItemType type = ItemTypes.AIR;
        private int amount = 1;
        private @Nullable NBTCompound nbt = null;
        private @Nullable PatchableComponentMap components = null;
        private int legacyData = -1;

        public Builder type(ItemType type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder nbt(NBTCompound nbt) {
            this.nbt = nbt;
            return this;
        }

        public Builder nbt(String key, NBT tag) {
            if (this.nbt == null) {
                this.nbt = new NBTCompound();
            }
            this.nbt.setTag(key, tag);
            return this;
        }

        public Builder components(@Nullable PatchableComponentMap components) {
            this.components = components;
            return this;
        }

        public <T> Builder component(ComponentType<T> type, @Nullable T value) {
            if (this.components == null) {
                this.components = new PatchableComponentMap(this.type.getComponents(this.version));
            }
            this.components.set(type, value);
            return this;
        }

        public Builder legacyData(int legacyData) {
            this.legacyData = legacyData;
            return this;
        }

        public Builder user(User user) {
            return this.version(user.getPacketVersion()).registryHolder(user);
        }

        public Builder wrapper(PacketWrapper<?> wrapper) {
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            return this.version(version).registryHolder(wrapper.getRegistryHolder());
        }

        public Builder version(ClientVersion version) {
            this.version = version;
            return this;
        }

        public Builder registryHolder(IRegistryHolder registryHolder) {
            this.registryHolder = registryHolder;
            return this;
        }

        public ItemStack build() {
            return new ItemStack(this.type, this.amount, this.nbt, this.components, this.legacyData, this.version, this.registryHolder);
        }
    }
}

