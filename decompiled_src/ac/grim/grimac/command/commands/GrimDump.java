/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.command.commands.GrimLog;
import ac.grim.grimac.platform.api.PlatformPlugin;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.description.Description;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.PropertiesUtil;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Properties;

public class GrimDump
implements BuildableCommand {
    private static final boolean PAPER = ReflectionUtils.hasClass("com.destroystokyo.paper.PaperConfig") || ReflectionUtils.hasClass("io.papermc.paper.configuration.Configuration");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String link = null;

    @Override
    public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
        commandManager.command(commandManager.commandBuilder("grim", "grimac").literal("dump", Description.of("Generate a debug dump"), new String[0]).permission("grim.dump").handler(this::handleDump));
    }

    private void handleDump(@NotNull CommandContext<Sender> context) {
        Sender sender = context.sender();
        if (this.link != null) {
            sender.sendMessage(MessageUtil.miniMessage(GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("upload-log", "%prefix% &fUploaded debug to: %url%").replace("%url%", this.link)));
            return;
        }
        GrimLog.sendLogAsync(sender, this.generateDump(), string -> {
            this.link = string;
        }, "text/yaml");
    }

    public static JsonObject getDumpInfo() {
        JsonObject base = new JsonObject();
        base.addProperty("type", "dump");
        base.addProperty("timestamp", (Number)System.currentTimeMillis());
        JsonObject versions = new JsonObject();
        base.add("versions", (JsonElement)versions);
        versions.addProperty("grim", GrimAPI.INSTANCE.getExternalAPI().getGrimVersion());
        versions.addProperty("packetevents", PacketEvents.getAPI().getVersion().toString());
        versions.addProperty("server", PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
        versions.addProperty("implementation", GrimAPI.INSTANCE.getPlatformServer().getPlatformImplementationString());
        JsonObject states = new JsonObject();
        base.add("states", (JsonElement)states);
        if (GrimAPI.INSTANCE.isInitialized()) {
            states.addProperty("platform", GrimAPI.INSTANCE.getPlatform().toString());
        }
        if (ViaVersionUtil.isAvailable) {
            states.addProperty("has_viaversion", Boolean.valueOf(true));
        }
        if (PAPER) {
            states.addProperty("has_paper", Boolean.valueOf(true));
        }
        JsonObject settings = new JsonObject();
        if (GrimAPI.INSTANCE.getAlertManager().hasConsoleVerboseEnabled()) {
            settings.addProperty("console_verbose", Boolean.valueOf(true));
        }
        if (!GrimAPI.INSTANCE.getAlertManager().hasConsoleAlertsEnabled()) {
            settings.addProperty("console_alerts", Boolean.valueOf(false));
        }
        if (settings.size() > 0) {
            states.add("settings", (JsonElement)settings);
        }
        JsonObject system = new JsonObject();
        base.add("system", (JsonElement)system);
        system.addProperty("os_name", System.getProperty("os.name"));
        system.addProperty("java_version", System.getProperty("java.version"));
        system.addProperty("user_language", System.getProperty("user.language"));
        base.add("build", (JsonElement)GrimDump.getBuildInfo());
        JsonArray plugins = new JsonArray();
        base.add("plugins", (JsonElement)plugins);
        for (PlatformPlugin plugin : GrimAPI.INSTANCE.getPluginManager().getPlugins()) {
            JsonObject pluginJson = new JsonObject();
            pluginJson.addProperty("enabled", Boolean.valueOf(plugin.isEnabled()));
            pluginJson.addProperty("name", plugin.getName());
            pluginJson.addProperty("version", plugin.getVersion());
            plugins.add((JsonElement)pluginJson);
        }
        return base;
    }

    private static JsonObject getBuildInfo() {
        JsonObject object = new JsonObject();
        try {
            Properties properties = PropertiesUtil.readProperties(GrimAPI.INSTANCE.getClass(), "grimac.properties");
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                object.addProperty(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return object;
    }

    private String generateDump() {
        JsonObject base = GrimDump.getDumpInfo();
        return this.gson.toJson((JsonElement)base);
    }
}

