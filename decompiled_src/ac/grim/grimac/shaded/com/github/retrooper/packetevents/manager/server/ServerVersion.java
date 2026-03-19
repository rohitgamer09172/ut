/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.VersionComparison;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public enum ServerVersion {
    V_1_7_2(4),
    V_1_7_4(4),
    V_1_7_5(4),
    V_1_7_6(5),
    V_1_7_7(5),
    V_1_7_8(5),
    V_1_7_9(5),
    V_1_7_10(5),
    V_1_8(47),
    V_1_8_3(47),
    V_1_8_8(47),
    V_1_9(107),
    V_1_9_1(108),
    V_1_9_2(109),
    V_1_9_4(110),
    V_1_10(210),
    V_1_10_1(210),
    V_1_10_2(210),
    V_1_11(315),
    V_1_11_2(316),
    V_1_12(335),
    V_1_12_1(338),
    V_1_12_2(340),
    V_1_13(393),
    V_1_13_1(401),
    V_1_13_2(404),
    V_1_14(477),
    V_1_14_1(480),
    V_1_14_2(485),
    V_1_14_3(490),
    V_1_14_4(498),
    V_1_15(573),
    V_1_15_1(575),
    V_1_15_2(578),
    V_1_16(735),
    V_1_16_1(736),
    V_1_16_2(751),
    V_1_16_3(753),
    V_1_16_4(754),
    V_1_16_5(754),
    V_1_17(755),
    V_1_17_1(756),
    V_1_18(757),
    V_1_18_1(757),
    V_1_18_2(758),
    V_1_19(759),
    V_1_19_1(760),
    V_1_19_2(760),
    V_1_19_3(761),
    V_1_19_4(762),
    V_1_20(763),
    V_1_20_1(763),
    V_1_20_2(764),
    V_1_20_3(765),
    V_1_20_4(765),
    V_1_20_5(766),
    V_1_20_6(766),
    V_1_21(767),
    V_1_21_1(767),
    V_1_21_2(768),
    V_1_21_3(768),
    V_1_21_4(769),
    V_1_21_5(770),
    V_1_21_6(771),
    V_1_21_7(772),
    V_1_21_8(772),
    V_1_21_9(773),
    V_1_21_10(773),
    V_1_21_11(774),
    ERROR(-1, true);

    private static final ServerVersion[] VALUES;
    private static final ServerVersion[] REVERSED_VALUES;
    private final int protocolVersion;
    private final String name;
    private ClientVersion toClientVersion;

    private ServerVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.name = this.name().substring(2).replace("_", ".");
    }

    private ServerVersion(int protocolVersion, boolean isNotRelease) {
        this.protocolVersion = protocolVersion;
        this.name = isNotRelease ? this.name() : this.name().substring(2).replace("_", ".");
    }

    public static ServerVersion[] reversedValues() {
        return REVERSED_VALUES;
    }

    public static ServerVersion getLatest() {
        return REVERSED_VALUES[1];
    }

    public static ServerVersion getOldest() {
        return VALUES[0];
    }

    @Deprecated
    public static ServerVersion getById(int protocolVersion) {
        for (ServerVersion version : VALUES) {
            if (version.protocolVersion != protocolVersion) continue;
            return version;
        }
        return null;
    }

    public ClientVersion toClientVersion() {
        if (this.toClientVersion == null) {
            this.toClientVersion = ClientVersion.getById(this.protocolVersion);
        }
        return this.toClientVersion;
    }

    public String getReleaseName() {
        return this.name;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public boolean isNewerThan(ServerVersion target) {
        return this.ordinal() > target.ordinal();
    }

    public boolean isOlderThan(ServerVersion target) {
        return this.ordinal() < target.ordinal();
    }

    public boolean isNewerThanOrEquals(ServerVersion target) {
        return this.ordinal() >= target.ordinal();
    }

    public boolean isOlderThanOrEquals(ServerVersion target) {
        return this.ordinal() <= target.ordinal();
    }

    public boolean is(@NotNull VersionComparison comparison, @NotNull ServerVersion targetVersion) {
        switch (comparison) {
            case EQUALS: {
                return this.protocolVersion == targetVersion.protocolVersion;
            }
            case NEWER_THAN: {
                return this.isNewerThan(targetVersion);
            }
            case NEWER_THAN_OR_EQUALS: {
                return this.isNewerThanOrEquals(targetVersion);
            }
            case OLDER_THAN: {
                return this.isOlderThan(targetVersion);
            }
            case OLDER_THAN_OR_EQUALS: {
                return this.isOlderThanOrEquals(targetVersion);
            }
        }
        return false;
    }

    static {
        VALUES = ServerVersion.values();
        REVERSED_VALUES = ServerVersion.values();
        int i = 0;
        int j = REVERSED_VALUES.length - 1;
        while (j > i) {
            ServerVersion tmp = REVERSED_VALUES[j];
            ServerVersion.REVERSED_VALUES[j--] = REVERSED_VALUES[i];
            ServerVersion.REVERSED_VALUES[i++] = tmp;
        }
    }
}

