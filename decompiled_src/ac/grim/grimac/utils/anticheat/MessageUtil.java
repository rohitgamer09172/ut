/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextReplacementConfig;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.utils.data.webhook.discord.CompiledDiscordTemplate;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;

public final class MessageUtil {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-ORX]");
    private static final Pattern HEX_PATTERN = Pattern.compile("([&\u00a7]#[A-Fa-f0-9]{6})|([&\u00a7]x([&\u00a7][A-Fa-f0-9]){6})");
    private static final char PLACEHOLDER_ESCAPE_CHAR = '\uffff';
    private static final Pattern UNIFIED_PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9_]+)%");

    @NotNull
    public static String toUnlabledString(@Nullable Vector3i vec) {
        return vec == null ? "null" : vec.x + ", " + vec.y + ", " + vec.z;
    }

    @NotNull
    public static String toUnlabledString(@Nullable Vector3f vec) {
        return vec == null ? "null" : vec.x + ", " + vec.y + ", " + vec.z;
    }

    @Contract(value="_, null, _ -> null; _, !null, _ -> !null")
    @Nullable
    public static String replacePlaceholders(@Nullable GrimPlayer player, @Nullable String string, boolean removeFormatting) {
        return MessageUtil.replacePlaceholders(player, player == null ? null : player.platformPlayer, string, removeFormatting);
    }

    @Contract(value="_, null -> null; _, !null -> !null")
    @Nullable
    public static String replacePlaceholders(@Nullable GrimPlayer player, @Nullable String string) {
        return MessageUtil.replacePlaceholders(player, player == null ? null : player.platformPlayer, string, false);
    }

    @Contract(value="_, null -> null; _, !null -> !null")
    @Nullable
    public static String replacePlaceholders(@Nullable Sender sender, @Nullable String string) {
        return MessageUtil.replacePlaceholders(sender != null ? sender.getPlatformPlayer() : null, string);
    }

    @Contract(value="_, null -> null; _, !null -> !null")
    @Nullable
    public static String replacePlaceholders(@Nullable PlatformPlayer player, @Nullable String string) {
        return MessageUtil.replacePlaceholders(player == null ? null : GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(player.getUniqueId()), player, string, false);
    }

    @Contract(value="_, _, null, _ -> null; _, _, !null, _ -> !null")
    @Nullable
    private static String replacePlaceholders(@Nullable GrimPlayer grimPlayer, @Nullable PlatformPlayer platformPlayer, @Nullable String string, boolean removeFormatting) {
        if (string == null) {
            return null;
        }
        if (string.indexOf(37) == -1) {
            return string;
        }
        Matcher matcher = UNIFIED_PLACEHOLDER_PATTERN.matcher(string);
        if (!matcher.find()) {
            return GrimAPI.INSTANCE.getMessagePlaceHolderManager().replacePlaceholders(platformPlayer, string);
        }
        Map<String, String> staticReplacements = GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements();
        Map<String, Function<GrimUser, String>> variableReplacements = GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements();
        StringBuilder sb = new StringBuilder(string.length() + 32);
        do {
            Function<GrimUser, String> func;
            String keyWithPercent = matcher.group(0);
            String value = null;
            String staticValue = staticReplacements.get(keyWithPercent);
            if (staticValue != null) {
                value = staticValue;
            } else if (grimPlayer != null && (func = variableReplacements.get(keyWithPercent)) != null) {
                value = func.apply(grimPlayer);
            }
            if (value == null) {
                value = keyWithPercent;
            } else if (removeFormatting) {
                value = CompiledDiscordTemplate.escapeMarkdown(value);
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        } while (matcher.find());
        matcher.appendTail(sb);
        String grimReplaced = sb.toString();
        return GrimAPI.INSTANCE.getMessagePlaceHolderManager().replacePlaceholders(platformPlayer, grimReplaced).replace('\uffff', '%');
    }

    @NotNull
    public static Component replacePlaceholders(@NotNull GrimPlayer player, @NotNull Component component) {
        TextReplacementConfig safeReplacement = (TextReplacementConfig)TextReplacementConfig.builder().match("%[a-zA-Z0-9_]+%").replacement(placeholder -> Component.text(MessageUtil.replacePlaceholders(player, placeholder.content()))).build();
        return component.replaceText(safeReplacement);
    }

    @NotNull
    public static Component miniMessage(@NotNull String string) {
        string = string.replace("%prefix%", GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("prefix", "&bGrim &8\u00bb"));
        Matcher matcher = HEX_PATTERN.matcher(string);
        StringBuilder sb = new StringBuilder(string.length());
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<#" + matcher.group(0).replaceAll("[&\u00a7#x]", "") + ">");
        }
        string = matcher.appendTail(sb).toString();
        string = MessageUtil.translateAlternateColorCodes('&', string).replace("\u00a70", "<!b><!i><!u><!st><!obf><black>").replace("\u00a71", "<!b><!i><!u><!st><!obf><dark_blue>").replace("\u00a72", "<!b><!i><!u><!st><!obf><dark_green>").replace("\u00a73", "<!b><!i><!u><!st><!obf><dark_aqua>").replace("\u00a74", "<!b><!i><!u><!st><!obf><dark_red>").replace("\u00a75", "<!b><!i><!u><!st><!obf><dark_purple>").replace("\u00a76", "<!b><!i><!u><!st><!obf><gold>").replace("\u00a77", "<!b><!i><!u><!st><!obf><gray>").replace("\u00a78", "<!b><!i><!u><!st><!obf><dark_gray>").replace("\u00a79", "<!b><!i><!u><!st><!obf><blue>").replace("\u00a7a", "<!b><!i><!u><!st><!obf><green>").replace("\u00a7b", "<!b><!i><!u><!st><!obf><aqua>").replace("\u00a7c", "<!b><!i><!u><!st><!obf><red>").replace("\u00a7d", "<!b><!i><!u><!st><!obf><light_purple>").replace("\u00a7e", "<!b><!i><!u><!st><!obf><yellow>").replace("\u00a7f", "<!b><!i><!u><!st><!obf><white>").replace("\u00a7r", "<reset>").replace("\u00a7k", "<obfuscated>").replace("\u00a7l", "<bold>").replace("\u00a7m", "<strikethrough>").replace("\u00a7n", "<underlined>").replace("\u00a7o", "<italic>");
        return MiniMessage.miniMessage().deserialize(string).compact();
    }

    public static Component getParsedComponent(Sender sender, String key, String fallbackText) {
        String message = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse(key, fallbackText);
        message = MessageUtil.replacePlaceholders(sender, message);
        return MessageUtil.miniMessage(message);
    }

    @Contract(value="_, _ -> new")
    @NotNull
    public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] != altColorChar || "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) <= -1) continue;
            b[i] = 167;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
        }
        return new String(b);
    }

    @Contract(value="!null -> !null; null -> null")
    @Nullable
    public static String stripColor(@Nullable String input) {
        return input == null ? null : STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    @Generated
    private MessageUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

