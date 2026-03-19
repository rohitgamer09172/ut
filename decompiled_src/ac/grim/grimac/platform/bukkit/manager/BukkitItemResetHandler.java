/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.EquipmentSlot
 */
package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.bukkit.utils.reflection.PaperUtils;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class BukkitItemResetHandler
implements ItemResetHandler {
    private static final Consumer<Player> resetItemUsage;
    private static final Predicate<Player> isUsingItem;
    private static final Function<Player, InteractionHand> getItemUsageHand;

    @Override
    public void resetItemUsage(@Nullable PlatformPlayer player) {
        if (player != null) {
            resetItemUsage.accept((Player)player.getNative());
        }
    }

    @Override
    @Nullable
    public InteractionHand getItemUsageHand(@Nullable PlatformPlayer player) {
        return player == null ? null : getItemUsageHand.apply((Player)player.getNative());
    }

    @Override
    public boolean isUsingItem(@Nullable PlatformPlayer player) {
        return player != null && isUsingItem.test((Player)player.getNative());
    }

    static {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        boolean legacy = version.isOlderThanOrEquals(ServerVersion.V_1_8_8);
        try {
            Method setLivingEntityFlag;
            Method method;
            String nmsPackage;
            Class<?> cls;
            Method getHandle_ = null;
            if (PaperUtils.PAPER && version.isNewerThanOrEquals(ServerVersion.V_1_20_5) && (cls = ReflectionUtils.getClass("org.bukkit.craftbukkit.entity.CraftLivingEntity")) != null) {
                getHandle_ = cls.getMethod("getHandle", new Class[0]);
            }
            boolean obfuscated = getHandle_ == null;
            String string = nmsPackage = obfuscated ? Bukkit.getServer().getClass().getPackageName().split("\\.")[3] : null;
            if (obfuscated) {
                String clazzName = legacy ? "CraftHumanEntity" : "CraftLivingEntity";
                getHandle_ = Class.forName("org.bukkit.craftbukkit." + nmsPackage + ".entity." + clazzName).getMethod("getHandle", new Class[0]);
            }
            Method getHandle = getHandle_;
            Class<?> clazz = getHandle_.getReturnType();
            if (version.isNewerThanOrEquals(ServerVersion.V_1_10)) {
                isUsingItem = HumanEntity::isHandRaised;
            } else {
                method = clazz.getMethod(switch (Objects.requireNonNull(nmsPackage)) {
                    case "v1_8_R3" -> "bS";
                    case "v1_9_R1" -> "cs";
                    case "v1_9_R2" -> "ct";
                    default -> throw new IllegalStateException("You are using an unsupported server version! (" + version.getReleaseName() + ")");
                }, new Class[0]);
                isUsingItem = player -> {
                    try {
                        return (Boolean)method.invoke(getHandle.invoke(player, new Object[0]), new Object[0]);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
            if (legacy) {
                getItemUsageHand = player -> isUsingItem.test((Player)player) ? InteractionHand.MAIN_HAND : null;
            } else if (PaperUtils.PAPER && version.isNewerThanOrEquals(ServerVersion.V_1_16_5)) {
                getItemUsageHand = player -> player.isHandRaised() ? (player.getHandRaised() == EquipmentSlot.OFF_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND) : null;
            } else {
                method = clazz.getMethod(switch (Objects.requireNonNull(nmsPackage)) {
                    case "v1_9_R1" -> "ct";
                    case "v1_9_R2" -> "cu";
                    case "v1_10_R1" -> "cy";
                    case "v1_11_R1" -> "cz";
                    case "v1_12_R1" -> "cH";
                    case "v1_13_R1", "v1_13_R2", "v1_14_R1" -> "cU";
                    case "v1_15_R1", "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1" -> "getRaisedHand";
                    case "v1_18_R1" -> "eM";
                    case "v1_18_R2" -> "eN";
                    case "v1_19_R1" -> "eU";
                    case "v1_19_R2" -> "fa";
                    case "v1_19_R3" -> "ff";
                    case "v1_20_R1" -> "fj";
                    case "v1_20_R2" -> "fn";
                    case "v1_20_R3" -> "fo";
                    case "v1_20_R4" -> "fw";
                    case "v1_21_R1" -> "fs";
                    case "v1_21_R2", "v1_21_R3", "v1_21_R4" -> "fA";
                    case "v1_21_R5" -> "fH";
                    case "v1_21_R6" -> "fP";
                    default -> throw new IllegalStateException("You are using an unsupported server version! (" + version.getReleaseName() + ")");
                }, new Class[0]);
                getItemUsageHand = player -> {
                    try {
                        return isUsingItem.test((Player)player) ? (((Enum)method.invoke(getHandle.invoke(player, new Object[0]), new Object[0])).ordinal() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND) : null;
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
            if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                String name = obfuscated ? "c" : "setLivingEntityFlag";
                setLivingEntityFlag = clazz.getDeclaredMethod(name, Integer.TYPE, Boolean.TYPE);
                setLivingEntityFlag.setAccessible(true);
            } else {
                setLivingEntityFlag = null;
            }
            if (PaperUtils.PAPER && version.isNewerThan(ServerVersion.V_1_17)) {
                resetItemUsage = setLivingEntityFlag == null ? LivingEntity::clearActiveItem : player -> {
                    try {
                        setLivingEntityFlag.invoke(getHandle.invoke(player, new Object[0]), 1, false);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    player.clearActiveItem();
                };
            } else {
                Method method2 = clazz.getMethod(switch (Objects.requireNonNull(nmsPackage)) {
                    case "v1_8_R3" -> "bV";
                    case "v1_9_R1" -> "cz";
                    case "v1_9_R2" -> "cA";
                    case "v1_10_R1" -> "cE";
                    case "v1_11_R1" -> "cF";
                    case "v1_12_R1" -> "cN";
                    case "v1_13_R1", "v1_13_R2" -> "da";
                    case "v1_14_R1" -> "dp";
                    case "v1_15_R1" -> "dH";
                    case "v1_16_R1", "v1_16_R2", "v1_16_R3", "v1_17_R1" -> "clearActiveItem";
                    case "v1_18_R1" -> "eR";
                    case "v1_18_R2" -> "eS";
                    case "v1_19_R1" -> "eZ";
                    case "v1_19_R2" -> "ff";
                    case "v1_19_R3" -> "fk";
                    case "v1_20_R1" -> "fo";
                    case "v1_20_R2" -> "fs";
                    case "v1_20_R3" -> "ft";
                    case "v1_20_R4" -> "fB";
                    case "v1_21_R1" -> "fx";
                    case "v1_21_R2", "v1_21_R3", "v1_21_R4" -> "fF";
                    case "v1_21_R5" -> "fM";
                    case "v1_21_R6" -> "fU";
                    default -> throw new IllegalStateException("You are using an unsupported server version! (" + version.getReleaseName() + ")");
                }, new Class[0]);
                resetItemUsage = legacy ? player -> {
                    try {
                        method2.invoke(getHandle.invoke(player, new Object[0]), new Object[0]);
                        if (isUsingItem.test((Player)player)) {
                            player.updateInventory();
                        }
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                } : (setLivingEntityFlag == null ? player -> {
                    try {
                        method2.invoke(getHandle.invoke(player, new Object[0]), new Object[0]);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                } : player -> {
                    try {
                        Object handle = getHandle.invoke(player, new Object[0]);
                        setLivingEntityFlag.invoke(handle, 1, false);
                        method2.invoke(handle, new Object[0]);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        catch (Throwable t) {
            RuntimeException e;
            throw t instanceof RuntimeException ? (e = (RuntimeException)t) : new RuntimeException(t);
        }
    }
}

