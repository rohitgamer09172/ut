/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class BukkitMessagePlaceHolderManager
implements MessagePlaceHolderManager {
    public static final boolean hasPlaceholderAPI = Reflection.getClassByNameWithoutException("me.clip.placeholderapi.PlaceholderAPI") != null;

    @Override
    @NotNull
    public String replacePlaceholders(@Nullable PlatformPlayer player, @NotNull String string) {
        Player player2;
        if (!hasPlaceholderAPI) {
            return string;
        }
        if (player instanceof BukkitPlatformPlayer) {
            BukkitPlatformPlayer bukkitPlatformPlayer = (BukkitPlatformPlayer)player;
            player2 = bukkitPlatformPlayer.getBukkitPlayer();
        } else {
            player2 = null;
        }
        return PlaceholderAPI.setPlaceholders(player2, (String)string);
    }
}

