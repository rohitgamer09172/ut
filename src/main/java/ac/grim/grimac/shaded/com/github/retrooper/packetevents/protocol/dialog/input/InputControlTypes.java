/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.BooleanInputControl;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.InputControlType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.NumberRangeInputControl;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.SingleOptionInputControl;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.StaticInputControlType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.TextInputControl;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InputControlTypes {
    private static final VersionedRegistry<InputControlType<?>> REGISTRY = new VersionedRegistry("input_control_type");
    public static final InputControlType<BooleanInputControl> BOOLEAN = InputControlTypes.define("boolean", BooleanInputControl::decode, BooleanInputControl::encode);
    public static final InputControlType<NumberRangeInputControl> NUMBER_RANGE = InputControlTypes.define("number_range", NumberRangeInputControl::decode, NumberRangeInputControl::encode);
    public static final InputControlType<SingleOptionInputControl> SINGLE_OPTION = InputControlTypes.define("single_option", SingleOptionInputControl::decode, SingleOptionInputControl::encode);
    public static final InputControlType<TextInputControl> TEXT = InputControlTypes.define("text", TextInputControl::decode, TextInputControl::encode);

    private InputControlTypes() {
    }

    public static VersionedRegistry<InputControlType<?>> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static <T extends InputControl> InputControlType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticInputControlType((TypesBuilderData)data, decoder, encoder));
    }

    static {
        REGISTRY.unloadMappings();
    }
}

