/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.util.RGBLike;

public enum DyeColor implements RGBLike
{
    WHITE(new Color(0xF9FFFE), new Color(0xFFFFFF), new Color(0xF0F0F0), 8),
    ORANGE(new Color(16351261), new Color(16738335), new Color(15435844), 15),
    MAGENTA(new Color(13061821), new Color(0xFF00FF), new Color(12801229), 16),
    LIGHT_BLUE(new Color(3847130), new Color(10141901), new Color(6719955), 17),
    YELLOW(new Color(16701501), new Color(0xFFFF00), new Color(14602026), 18),
    LIME(new Color(8439583), new Color(0xBFFF00), new Color(4312372), 19),
    PINK(new Color(15961002), new Color(16738740), new Color(14188952), 20),
    GRAY(new Color(4673362), new Color(0x808080), new Color(0x434343), 21),
    LIGHT_GRAY(new Color(0x9D9D97), new Color(0xD3D3D3), new Color(0xABABAB), 22),
    CYAN(new Color(1481884), new Color(65535), new Color(2651799), 23),
    PURPLE(new Color(8991416), new Color(10494192), new Color(8073150), 24),
    BLUE(new Color(3949738), new Color(255), new Color(2437522), 25),
    BROWN(new Color(8606770), new Color(9127187), new Color(5320730), 26),
    GREEN(new Color(6192150), new Color(65280), new Color(3887386), 27),
    RED(new Color(11546150), new Color(0xFF0000), new Color(11743532), 28),
    BLACK(new Color(0x1D1D21), new Color(0), new Color(0x1E1B1B), 29);

    private static final DyeColor[] COLORS;
    private final Color textureDiffuseColor;
    private final Color textColor;
    private final Color fireworkColor;
    private final int mapColorId;

    private DyeColor(@NotNull Color textureDiffuseColor, Color textColor, Color fireworkColor, int mapColorId) {
        this.textureDiffuseColor = textureDiffuseColor;
        this.textColor = textColor;
        this.fireworkColor = fireworkColor;
        this.mapColorId = mapColorId;
    }

    public static DyeColor read(PacketWrapper<?> wrapper) {
        return (DyeColor)wrapper.readEnum(COLORS);
    }

    public static void write(PacketWrapper<?> wrapper, DyeColor color) {
        wrapper.writeEnum(color);
    }

    @NotNull
    public Color color() {
        return this.textureDiffuseColor;
    }

    @NotNull
    public Color textColor() {
        return this.textColor;
    }

    @NotNull
    public Color fireworkColor() {
        return this.fireworkColor;
    }

    @Override
    public int red() {
        return this.textureDiffuseColor.red();
    }

    @Override
    public int green() {
        return this.textureDiffuseColor.green();
    }

    @Override
    public int blue() {
        return this.textureDiffuseColor.blue();
    }

    public int mapColorId() {
        return this.mapColorId;
    }

    static {
        COLORS = DyeColor.values();
    }
}

