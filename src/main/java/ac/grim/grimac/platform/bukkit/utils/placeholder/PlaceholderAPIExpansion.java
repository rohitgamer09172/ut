/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.platform.bukkit.utils.placeholder;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPIExpansion
extends PlaceholderExpansion {
    @NotNull
    public String getIdentifier() {
        return "grim";
    }

    @NotNull
    public String getAuthor() {
        return String.join((CharSequence)", ", GrimAPI.INSTANCE.getGrimPlugin().getDescription().getAuthors());
    }

    @NotNull
    public String getVersion() {
        return GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
    }

    public boolean persist() {
        return true;
    }

    @NotNull
    public List<String> getPlaceholders() {
        Set<String> staticReplacements = GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements().keySet();
        Set<String> variableReplacements = GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements().keySet();
        ArrayList<String> placeholders = new ArrayList<String>(staticReplacements.size() + variableReplacements.size());
        for (String s : staticReplacements) {
            placeholders.add((String)(s.equals("%grim_version%") ? s : "%grim_" + s.replaceAll("%", "") + "%"));
        }
        for (String s : variableReplacements) {
            placeholders.add((String)(s.equals("%player%") ? "%grim_player%" : "%grim_player_" + s.replaceAll("%", "") + "%"));
        }
        return placeholders;
    }

    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        for (Map.Entry<String, String> entry : GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements().entrySet()) {
            String key = entry.getKey().equals("%grim_version%") ? "version" : entry.getKey().replaceAll("%", "");
            if (!params.equalsIgnoreCase(key)) continue;
            return entry.getValue();
        }
        if (offlinePlayer instanceof Player) {
            Player player = (Player)offlinePlayer;
            GrimPlayer grimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(player.getUniqueId());
            if (grimPlayer == null) {
                return null;
            }
            for (Map.Entry<String, Function<GrimUser, String>> entry : GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements().entrySet()) {
                Object key = entry.getKey().equals("%player%") ? "player" : "player_" + entry.getKey().replaceAll("%", "");
                if (!params.equalsIgnoreCase((String)key)) continue;
                return entry.getValue().apply(grimPlayer);
            }
        }
        return null;
    }
}

