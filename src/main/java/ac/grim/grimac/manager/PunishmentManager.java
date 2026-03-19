/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.config.ConfigReloadable;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.events.packets.ProxyAlertMessenger;
import ac.grim.grimac.manager.ParsedCommand;
import ac.grim.grimac.manager.PunishGroup;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PunishmentManager
implements ConfigReloadable {
    private final GrimPlayer player;
    private final List<PunishGroup> groups = new ArrayList<PunishGroup>();
    private String experimentalSymbol = "*";
    private String alertString;
    private boolean testMode;
    private String proxyAlertString = "";

    public PunishmentManager(GrimPlayer player) {
        this.player = player;
    }

    @Override
    public void reload(ConfigManager config) {
        List<String> punish = config.getStringListElse("Punishments", new ArrayList<String>());
        this.experimentalSymbol = config.getStringElse("experimental-symbol", "*");
        this.alertString = config.getStringElse("alerts-format", "%prefix% &f%player% &bfailed &f%check_name% &f(x&c%vl%&f) &7%verbose%");
        this.testMode = config.getBooleanElse("test-mode", false);
        this.proxyAlertString = config.getStringElse("alerts-format-proxy", "%prefix% &f[&cproxy&f] &f%player% &bfailed &f%check_name% &f(x&c%vl%&f) &7%verbose%");
        try {
            this.groups.clear();
            for (AbstractCheck abstractCheck : this.player.checkManager.allChecks.values()) {
                abstractCheck.setEnabled(false);
            }
            for (Object object : punish) {
                LinkedHashMap map = (LinkedHashMap)object;
                List checks = map.getOrDefault("checks", new ArrayList());
                List commands = map.getOrDefault("commands", new ArrayList());
                int removeViolationsAfter = map.getOrDefault("remove-violations-after", 300);
                ArrayList<ParsedCommand> parsed = new ArrayList<ParsedCommand>();
                ArrayList<AbstractCheck> checksList = new ArrayList<AbstractCheck>();
                ArrayList<AbstractCheck> excluded = new ArrayList<AbstractCheck>();
                for (String command : checks) {
                    command = command.toLowerCase(Locale.ROOT);
                    boolean exclude = false;
                    if (command.startsWith("!")) {
                        exclude = true;
                        command = command.substring(1);
                    }
                    for (AbstractCheck check : this.player.checkManager.allChecks.values()) {
                        if (check.getCheckName() == null || !check.getCheckName().toLowerCase(Locale.ROOT).contains(command) && !check.getAlternativeName().toLowerCase(Locale.ROOT).contains(command)) continue;
                        if (exclude) {
                            excluded.add(check);
                            continue;
                        }
                        checksList.add(check);
                        check.setEnabled(true);
                    }
                    for (AbstractCheck check : excluded) {
                        checksList.remove(check);
                    }
                }
                for (String command : commands) {
                    String firstNum = command.substring(0, command.indexOf(":"));
                    String secondNum = command.substring(command.indexOf(":"), command.indexOf(" "));
                    int threshold = Integer.parseInt(firstNum);
                    int interval = Integer.parseInt(secondNum.substring(1));
                    String commandString = command.substring(command.indexOf(" ") + 1);
                    parsed.add(new ParsedCommand(threshold, interval, commandString));
                }
                this.groups.add(new PunishGroup(checksList, parsed, removeViolationsAfter * 1000));
            }
        }
        catch (Exception e) {
            LogUtil.error("Error while loading punishments.yml! This is likely your fault!", e);
        }
    }

    private String replaceAlertPlaceholders(String original, int vl, Check check, String verbose) {
        return MessageUtil.replacePlaceholders(this.player, original.replace("[alert]", this.alertString).replace("[proxy]", this.proxyAlertString).replace("%check_name%", check.getDisplayName()).replace("%experimental%", check.isExperimental() ? this.experimentalSymbol : "").replace("%vl%", Integer.toString(vl)).replace("%description%", check.getDescription())).replace("%verbose%", MiniMessage.miniMessage().escapeTags(verbose));
    }

    public boolean handleAlert(GrimPlayer player, String verbose, Check check) {
        boolean sentDebug = false;
        for (PunishGroup group : this.groups) {
            if (!group.checks.contains(check)) continue;
            int vl = this.getViolations(group, check);
            int violationCount = group.violations.size();
            for (ParsedCommand command : group.commands) {
                boolean inInterval;
                String cmd = this.replaceAlertPlaceholders(command.command, vl, check, verbose);
                Set<PlatformPlayer> verboseListeners = null;
                if (GrimAPI.INSTANCE.getAlertManager().hasVerboseListeners() && command.command.equals("[alert]")) {
                    sentDebug = true;
                    Component component = MessageUtil.miniMessage(cmd);
                    verboseListeners = GrimAPI.INSTANCE.getAlertManager().sendVerbose(component, null);
                }
                if (violationCount < command.threshold) continue;
                boolean bl = command.interval == 0 ? command.executeCount == 0 : (inInterval = violationCount % command.interval == 0);
                if (inInterval) {
                    CommandExecuteEvent executeEvent = new CommandExecuteEvent(player, check, verbose, cmd);
                    GrimAPI.INSTANCE.getEventBus().post(executeEvent);
                    if (executeEvent.isCancelled()) continue;
                    switch (command.command) {
                        case "[webhook]": {
                            GrimAPI.INSTANCE.getDiscordManager().sendAlert(player, verbose, check.getDisplayName(), vl);
                            break;
                        }
                        case "[log]": {
                            int vls = (int)group.violations.values().stream().filter(e -> e == check).count();
                            String verboseWithoutGl = verbose.replaceAll(" /gl .*", "");
                            GrimAPI.INSTANCE.getViolationDatabaseManager().logAlert(player, verboseWithoutGl, check.getDisplayName(), vls);
                            break;
                        }
                        case "[proxy]": {
                            ProxyAlertMessenger.sendPluginMessage(cmd);
                            break;
                        }
                        case "[alert]": {
                            sentDebug = true;
                            Component message = MessageUtil.miniMessage(cmd);
                            if (this.testMode) {
                                if (verboseListeners != null && !verboseListeners.contains(player.platformPlayer)) break;
                                player.sendMessage(message);
                                break;
                            }
                            GrimAPI.INSTANCE.getAlertManager().sendAlert(message, verboseListeners);
                            break;
                        }
                        default: {
                            GrimAPI.INSTANCE.getScheduler().getGlobalRegionScheduler().run(GrimAPI.INSTANCE.getGrimPlugin(), () -> GrimAPI.INSTANCE.getPlatformServer().dispatchCommand(GrimAPI.INSTANCE.getPlatformServer().getConsoleSender(), cmd));
                        }
                    }
                }
                ++command.executeCount;
            }
        }
        return sentDebug;
    }

    public void handleViolation(Check check) {
        for (PunishGroup group : this.groups) {
            if (!group.checks.contains(check)) continue;
            long currentTime = System.currentTimeMillis();
            group.violations.put(currentTime, check);
            group.violations.long2ObjectEntrySet().removeIf(time -> currentTime - time.getLongKey() > (long)group.removeViolationsAfter);
        }
    }

    private int getViolations(PunishGroup group, Check check) {
        int vl = 0;
        for (Check value : group.violations.values()) {
            if (value != check) continue;
            ++vl;
        }
        return vl;
    }
}

