/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ChangePageClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.CopyToClipboardClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.CustomClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.OpenFileClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.OpenUrlClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.RunCommandClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ShowDialogClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.StaticClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.SuggestCommandClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.TwitchUserInfoClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ClickEventActions {
    private static final VersionedRegistry<ClickEventAction<?>> REGISTRY = new VersionedRegistry("click_event_action");
    public static final ClickEventAction<OpenUrlClickEvent> OPEN_URL = ClickEventActions.define("open_url", true, OpenUrlClickEvent::decode, OpenUrlClickEvent::encode);
    public static final ClickEventAction<OpenFileClickEvent> OPEN_FILE = ClickEventActions.define("open_file", false, OpenFileClickEvent::decode, OpenFileClickEvent::encode);
    public static final ClickEventAction<RunCommandClickEvent> RUN_COMMAND = ClickEventActions.define("run_command", true, RunCommandClickEvent::decode, RunCommandClickEvent::encode);
    @ApiStatus.Obsolete
    public static final ClickEventAction<TwitchUserInfoClickEvent> TWITCH_USER_INFO = ClickEventActions.define("twitch_user_info", false, TwitchUserInfoClickEvent::decode, TwitchUserInfoClickEvent::encode);
    public static final ClickEventAction<SuggestCommandClickEvent> SUGGEST_COMMAND = ClickEventActions.define("suggest_command", true, SuggestCommandClickEvent::decode, SuggestCommandClickEvent::encode);
    public static final ClickEventAction<ChangePageClickEvent> CHANGE_PAGE = ClickEventActions.define("change_page", true, ChangePageClickEvent::decode, ChangePageClickEvent::encode);
    public static final ClickEventAction<CopyToClipboardClickEvent> COPY_TO_CLIPBOARD = ClickEventActions.define("copy_to_clipboard", true, CopyToClipboardClickEvent::decode, CopyToClipboardClickEvent::encode);
    public static final ClickEventAction<ShowDialogClickEvent> SHOW_DIALOG = ClickEventActions.define("show_dialog", true, ShowDialogClickEvent::decode, ShowDialogClickEvent::encode);
    public static final ClickEventAction<CustomClickEvent> CUSTOM = ClickEventActions.define("custom", true, CustomClickEvent::decode, CustomClickEvent::encode);

    private ClickEventActions() {
    }

    @ApiStatus.Internal
    public static <T extends ClickEvent> ClickEventAction<T> define(String name, boolean allowFromServer, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        return REGISTRY.define(name, data -> new StaticClickEventAction((TypesBuilderData)data, allowFromServer, decoder, encoder));
    }

    public static VersionedRegistry<ClickEventAction<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

