/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.ComponentPredicateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates.IComponentPredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticComponentPredicateType<T extends IComponentPredicate>
extends AbstractMappedEntity
implements ComponentPredicateType<T> {
    private final PacketWrapper.Reader<T> reader;
    private final PacketWrapper.Writer<T> writer;

    public StaticComponentPredicateType(PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        this(null, reader, writer);
    }

    @ApiStatus.Internal
    public StaticComponentPredicateType(@Nullable TypesBuilderData data, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        super(data);
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public T read(PacketWrapper<?> wrapper) {
        return (T)((IComponentPredicate)this.reader.apply(wrapper));
    }

    @Override
    public void write(PacketWrapper<?> wrapper, T predicate) {
        this.writer.accept(wrapper, predicate);
    }
}

