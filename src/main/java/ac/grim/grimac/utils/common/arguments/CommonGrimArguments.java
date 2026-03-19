/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.common.arguments;

import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.utils.common.arguments.ArgumentUtils;
import ac.grim.grimac.utils.common.arguments.SystemArgument;
import ac.grim.grimac.utils.common.arguments.SystemArgumentFactory;

public class CommonGrimArguments {
    private static final SystemArgumentFactory FACTORY = SystemArgumentFactory.Builder.of("Grim").optionModifier(builder -> builder.key("Grim" + builder.options().getKey())).supportEnv().build();
    public static final SystemArgument<Boolean> KICK_ON_TRANSACTION_ERRORS = FACTORY.create(ArgumentUtils.string("KickOnTransactionTaskErrors", false));
    public static final SystemArgument<String> API_URL = FACTORY.create(ArgumentUtils.string("APIUrl", "https://api.grim.ac/v1/server/"));
    public static final SystemArgument<String> PASTE_URL = FACTORY.create(ArgumentUtils.string("PasteUrl", "https://paste.grim.ac/"));
    public static final SystemArgument<Platform> PLATFORM_OVERRIDE = FACTORY.create(ArgumentUtils.platform("PlatformOverride"));
    public static final SystemArgument<Boolean> USE_CHAT_FAST_BYPASS = FACTORY.create(ArgumentUtils.string("ChatFastBypass", true));
}

