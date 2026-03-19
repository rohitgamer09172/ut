/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  lombok.Generated
 */
package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;

public class GrimVersion
implements BuildableCommand {
    private static final AtomicReference<Component> updateMessage = new AtomicReference();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static long lastCheck;

    public static void checkForUpdatesAsync(Sender sender) {
        String current = GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
        sender.sendMessage((Component)((TextComponent.Builder)((TextComponent.Builder)Component.text().append(Component.text("Grim Version: ").color(NamedTextColor.GRAY))).append(Component.text(current).color(NamedTextColor.AQUA))).build());
        long now = System.currentTimeMillis();
        if (now - lastCheck < 60000L) {
            Component message = updateMessage.get();
            if (message != null) {
                sender.sendMessage(message);
            }
            return;
        }
        lastCheck = now;
        GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> GrimVersion.checkForUpdates(sender));
    }

    private static void checkForUpdates(Sender sender) {
        try {
            Component msg;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CommonGrimArguments.API_URL.value() + "updates")).GET().header("User-Agent", "GrimAC/" + GrimAPI.INSTANCE.getExternalAPI().getGrimVersion()).header("Content-Type", "application/json").timeout(Duration.of(5L, ChronoUnit.SECONDS)).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode < 200 || statusCode >= 300) {
                Component msg2 = updateMessage.get();
                sender.sendMessage(Objects.requireNonNullElseGet(msg2, () -> ((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append(MessageUtil.miniMessage("%prefix%"))).append(Component.text(" Failed to check latest GrimAC version. Update server responded with status code: ").color(NamedTextColor.YELLOW))).append(((TextComponent)Component.text(statusCode).color(GrimVersion.getColorForStatusCode(statusCode))).decorate(TextDecoration.BOLD))).build()));
                return;
            }
            JsonObject object = new JsonParser().parse(response.body()).getAsJsonObject();
            String downloadPage = GrimVersion.getJsonString(object, "download_page", "Unknown");
            String latest = GrimVersion.getJsonString(object, "latest_version", "Unknown");
            @Nullable String warning = GrimVersion.getJsonString(object, "warning", null);
            Status status = object.has("status") ? Status.getStatus(object.get("status").getAsString()) : Status.SemVer.getVersionStatus(GrimAPI.INSTANCE.getExternalAPI().getGrimVersion(), latest);
            switch (status.ordinal()) {
                default: {
                    throw new IncompatibleClassChangeError();
                }
                case 0: {
                    TextComponent textComponent = (TextComponent)Component.text("You are using a development version of GrimAC").color(NamedTextColor.LIGHT_PURPLE);
                    break;
                }
                case 1: {
                    TextComponent textComponent = (TextComponent)Component.text("You are using the latest version of GrimAC").color(NamedTextColor.GREEN);
                    break;
                }
                case 2: {
                    TextComponent textComponent = (TextComponent)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append(Component.text("New GrimAC version found!").color(NamedTextColor.AQUA))).append(Component.text(" Version ").color(NamedTextColor.GRAY))).append(((TextComponent)Component.text(latest).color(NamedTextColor.GRAY)).decorate(TextDecoration.ITALIC))).append(Component.text(" is available to be downloaded here: ").color(NamedTextColor.GRAY))).append(((TextComponent)((TextComponent)Component.text(downloadPage).color(NamedTextColor.GRAY)).decorate(TextDecoration.UNDERLINED)).clickEvent(ClickEvent.openUrl(downloadPage)))).build();
                    break;
                }
                case 3: {
                    TextComponent textComponent = msg = (TextComponent)Component.text("You are using an unknown GrimAC version.").color(NamedTextColor.RED);
                }
            }
            if (warning != null && !warning.isBlank()) {
                msg = msg.append((Component)((TextComponent.Builder)Component.text().append(Component.text(warning).color(NamedTextColor.RED))).build());
            }
            updateMessage.set(msg);
            sender.sendMessage(msg);
        }
        catch (Exception e) {
            sender.sendMessage(Component.text("Failed to check latest version.").color(NamedTextColor.RED));
            LogUtil.error("Failed to check latest GrimAC version.", e);
        }
    }

    private static String getJsonString(JsonObject object, String key, String defaultValue) {
        return object.has(key) ? object.get(key).getAsString() : defaultValue;
    }

    private static NamedTextColor getColorForStatusCode(int code) {
        if (code >= 500) {
            return NamedTextColor.RED;
        }
        if (code >= 400) {
            return NamedTextColor.RED;
        }
        if (code >= 300) {
            return NamedTextColor.YELLOW;
        }
        if (code >= 200) {
            return NamedTextColor.GREEN;
        }
        return NamedTextColor.GRAY;
    }

    @Override
    public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
        commandManager.command(commandManager.commandBuilder("grim", "grimac").literal("version", new String[0]).permission("grim.version").handler(this::handleVersion));
    }

    private void handleVersion(@NotNull CommandContext<Sender> context) {
        Sender sender = context.sender();
        GrimVersion.checkForUpdatesAsync(sender);
    }

    private static enum Status {
        AHEAD("ahead"),
        UPDATED("updated"),
        OUTDATED("outdated"),
        UNKNOWN("unknown");

        private final String id;

        public static Status getStatus(String id) {
            for (Status status : Status.values()) {
                if (!status.id.equals(id)) continue;
                return status;
            }
            return UNKNOWN;
        }

        @Generated
        private Status(String id) {
            this.id = id;
        }

        private static class SemVer {
            private SemVer() {
            }

            public static Status getVersionStatus(String current, String latest) {
                try {
                    int cmp = SemVer.compareSemver(current, latest);
                    if (cmp == 0) {
                        return UPDATED;
                    }
                    if (cmp < 0) {
                        return OUTDATED;
                    }
                    return AHEAD;
                }
                catch (Exception exception) {
                    return UNKNOWN;
                }
            }

            public static String normalizeCoreVersion(String version) {
                String trimmed = version.trim();
                String[] dashParts = trimmed.split("-");
                String[] plusParts = dashParts[0].split("\\+");
                return plusParts[0];
            }

            public static int[] parseVersion(String version) {
                int patch;
                String core = SemVer.normalizeCoreVersion(version);
                if (core.isEmpty()) {
                    return null;
                }
                String[] parts = core.split("\\.");
                if (parts.length < 1) {
                    return null;
                }
                int major = SemVer.parseInt(parts[0]);
                int minor = parts.length > 1 ? SemVer.parseInt(parts[1]) : 0;
                int n = patch = parts.length > 2 ? SemVer.parseInt(parts[2]) : 0;
                if (major < 0 || minor < 0 || patch < 0) {
                    return null;
                }
                return new int[]{major, minor, patch};
            }

            private static int parseInt(String str) {
                try {
                    return Integer.parseInt(str);
                }
                catch (NumberFormatException e) {
                    return -1;
                }
            }

            public static int compareSemver(String a, String b) {
                int[] pa = SemVer.parseVersion(a);
                int[] pb = SemVer.parseVersion(b);
                if (pa == null || pb == null) {
                    return 0;
                }
                for (int i = 0; i < 3; ++i) {
                    if (pa[i] < pb[i]) {
                        return -1;
                    }
                    if (pa[i] <= pb[i]) continue;
                    return 1;
                }
                return 0;
            }
        }
    }
}

