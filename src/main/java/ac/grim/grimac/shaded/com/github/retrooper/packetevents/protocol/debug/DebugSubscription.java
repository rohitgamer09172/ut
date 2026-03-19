/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.DebugSubscriptions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface DebugSubscription<T>
extends MappedEntity {
    public T read(PacketWrapper<?> var1);

    public void write(PacketWrapper<?> var1, T var2);

    public static final class Update<T> {
        private final DebugSubscription<T> subscription;
        private final @Nullable T value;

        public Update(DebugSubscription<T> subscription, @Nullable T value) {
            this.subscription = subscription;
            this.value = value;
        }

        public static Update<?> read(PacketWrapper<?> wrapper) {
            DebugSubscription<?> subscription = wrapper.readMappedEntity(DebugSubscriptions.getRegistry());
            Object value = wrapper.readOptional(subscription::read);
            return new Update(subscription, value);
        }

        public static <T> void write(PacketWrapper<?> wrapper, Update<T> event) {
            wrapper.writeMappedEntity(event.subscription);
            wrapper.writeOptional(event.value, event.subscription::write);
        }

        public DebugSubscription<T> getSubscription() {
            return this.subscription;
        }

        public @Nullable T getValue() {
            return this.value;
        }
    }

    public static final class Event<T> {
        private final DebugSubscription<T> subscription;
        private final T value;

        public Event(DebugSubscription<T> subscription, T value) {
            this.subscription = subscription;
            this.value = value;
        }

        public static Event<?> read(PacketWrapper<?> wrapper) {
            DebugSubscription<?> subscription = wrapper.readMappedEntity(DebugSubscriptions.getRegistry());
            Object value = subscription.read(wrapper);
            return new Event(subscription, value);
        }

        public static <T> void write(PacketWrapper<?> wrapper, Event<T> event) {
            wrapper.writeMappedEntity(event.subscription);
            event.subscription.write(wrapper, event.value);
        }

        public DebugSubscription<T> getSubscription() {
            return this.subscription;
        }

        public T getValue() {
            return this.value;
        }
    }
}

