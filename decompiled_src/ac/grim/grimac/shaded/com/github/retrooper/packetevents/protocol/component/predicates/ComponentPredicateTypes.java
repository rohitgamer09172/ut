/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.ComponentPredicateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.IComponentPredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.NbtComponentPredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.StaticComponentPredicateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ComponentPredicateTypes {
    private static final VersionedRegistry<ComponentPredicateType<?>> REGISTRY = new VersionedRegistry("data_component_predicate_type");
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> DAMAGE = ComponentPredicateTypes.define("damage", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> ENCHANTMENTS = ComponentPredicateTypes.define("enchantments", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> STORED_ENCHANTMENTS = ComponentPredicateTypes.define("stored_enchantments", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> POTION_CONTENTS = ComponentPredicateTypes.define("potion_contents", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> CUSTOM_DATA = ComponentPredicateTypes.define("custom_data", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> CONTAINER = ComponentPredicateTypes.define("container", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> BUNDLE_CONTENTS = ComponentPredicateTypes.define("bundle_contents", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> FIREWORK_EXPLOSION = ComponentPredicateTypes.define("firework_explosion", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> FIREWORKS = ComponentPredicateTypes.define("fireworks", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> WRITABLE_BOOK_CONTENT = ComponentPredicateTypes.define("writable_book_content", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> WRITTEN_BOOK_CONTENT = ComponentPredicateTypes.define("written_book_content", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> ATTRIBUTE_MODIFIERS = ComponentPredicateTypes.define("attribute_modifiers", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> TRIM = ComponentPredicateTypes.define("trim", NbtComponentPredicate::read, NbtComponentPredicate::write);
    @ApiStatus.Experimental
    public static final ComponentPredicateType<NbtComponentPredicate> JUKEBOX_PLAYABLE = ComponentPredicateTypes.define("jukebox_playable", NbtComponentPredicate::read, NbtComponentPredicate::write);

    private ComponentPredicateTypes() {
    }

    @ApiStatus.Internal
    public static <T extends IComponentPredicate> ComponentPredicateType<T> define(String key, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(key, data -> new StaticComponentPredicateType((TypesBuilderData)data, reader, writer));
    }

    public static VersionedRegistry<ComponentPredicateType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

