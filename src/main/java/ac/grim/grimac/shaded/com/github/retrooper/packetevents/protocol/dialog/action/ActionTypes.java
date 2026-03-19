/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.Action;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.ActionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.DynamicCustomAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.DynamicRunCommandAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.StaticActionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ActionTypes {
    private static final VersionedRegistry<ActionType<?>> REGISTRY = new VersionedRegistry("dialog_action_type");
    public static final ActionType<StaticAction> OPEN_URL = ActionTypes.define("open_url", StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> RUN_COMMAND = ActionTypes.define("run_command", StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> SUGGEST_COMMAND = ActionTypes.define("suggest_command", StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> SHOW_DIALOG = ActionTypes.define("show_dialog", StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> CHANGE_PAGE = ActionTypes.define("change_page", StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> COPY_TO_CLIPBOARD = ActionTypes.define("copy_to_clipboard", StaticAction::decode, StaticAction::encode);
    public static final ActionType<StaticAction> CUSTOM = ActionTypes.define("custom", StaticAction::decode, StaticAction::encode);
    public static final ActionType<DynamicRunCommandAction> DYNAMIC_RUN_COMMAND = ActionTypes.define("dynamic/run_command", DynamicRunCommandAction::decode, DynamicRunCommandAction::encode);
    public static final ActionType<DynamicCustomAction> DYNAMIC_CUSTOM = ActionTypes.define("dynamic/custom", DynamicCustomAction::decode, DynamicCustomAction::encode);

    private ActionTypes() {
    }

    @ApiStatus.Internal
    public static <T extends Action> ActionType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticActionType((TypesBuilderData)data, decoder, encoder));
    }

    public static VersionedRegistry<ActionType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

