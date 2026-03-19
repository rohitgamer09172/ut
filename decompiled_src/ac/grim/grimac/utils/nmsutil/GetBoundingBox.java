/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.nmsutil.BoundingBoxSize;
import lombok.Generated;

public final class GetBoundingBox {
    public static SimpleCollisionBox getCollisionBoxForPlayer(@NotNull GrimPlayer player, double centerX, double centerY, double centerZ) {
        if (player.inVehicle()) {
            return GetBoundingBox.getPacketEntityBoundingBox(player, centerX, centerY, centerZ, player.compensatedEntities.self.getRiding());
        }
        return GetBoundingBox.getPlayerBoundingBox(player, centerX, centerY, centerZ);
    }

    @NotNull
    public static SimpleCollisionBox getPacketEntityBoundingBox(GrimPlayer player, double centerX, double minY, double centerZ, PacketEntity entity) {
        float width = BoundingBoxSize.getWidth(player, entity);
        float height = BoundingBoxSize.getHeight(player, entity);
        return GetBoundingBox.getBoundingBoxFromPosAndSize(entity, centerX, minY, centerZ, width, height);
    }

    @NotNull
    public static SimpleCollisionBox getPlayerBoundingBox(@NotNull GrimPlayer player, double centerX, double minY, double centerZ) {
        float width = player.pose.width;
        float height = player.pose.height;
        return GetBoundingBox.getBoundingBoxFromPosAndSize(player, centerX, minY, centerZ, width, height);
    }

    @NotNull
    public static SimpleCollisionBox getBoundingBoxFromPosAndSize(@NotNull GrimPlayer player, double centerX, double minY, double centerZ, float width, float height) {
        return GetBoundingBox.getBoundingBoxFromPosAndSize(player.compensatedEntities.self, centerX, minY, centerZ, width, height);
    }

    @NotNull
    public static SimpleCollisionBox getBoundingBoxFromPosAndSize(@NotNull PacketEntity entity, double centerX, double minY, double centerZ, float width, float height) {
        float scale = (float)entity.getAttributeValue(Attributes.SCALE);
        return GetBoundingBox.getBoundingBoxFromPosAndSizeRaw(centerX, minY, centerZ, width * scale, height * scale);
    }

    @Contract(value="_, _, _, _, _ -> new")
    @NotNull
    public static SimpleCollisionBox getBoundingBoxFromPosAndSizeRaw(double centerX, double minY, double centerZ, float width, float height) {
        double minX = centerX - (double)(width / 2.0f);
        double maxX = centerX + (double)(width / 2.0f);
        double maxY = minY + (double)height;
        double minZ = centerZ - (double)(width / 2.0f);
        double maxZ = centerZ + (double)(width / 2.0f);
        return new SimpleCollisionBox(Math.min(minX, maxX), Math.min(minY, maxY), Math.min(minZ, maxZ), Math.max(minX, maxX), Math.max(minY, maxY), Math.max(minZ, maxZ), false);
    }

    public static double @NotNull [] getEntityDimensions(GrimPlayer player, @NotNull PacketEntity entity) {
        float scale = (float)entity.getAttributeValue(Attributes.SCALE);
        float width = BoundingBoxSize.getWidth(player, entity) * scale;
        float height = BoundingBoxSize.getHeight(player, entity) * scale;
        return new double[]{width, height, width};
    }

    public static void expandBoundingBoxByEntityDimensions(@NotNull SimpleCollisionBox box, GrimPlayer player, PacketEntity entity) {
        double[] dimensions = GetBoundingBox.getEntityDimensions(player, entity);
        double halfWidth = dimensions[0] / 2.0;
        double height = dimensions[1];
        double halfDepth = dimensions[2] / 2.0;
        double minX = box.minX - halfWidth;
        double minY = box.minY;
        double minZ = box.minZ - halfDepth;
        double maxX = box.maxX + halfWidth;
        double maxY = box.maxY + height;
        double maxZ = box.maxZ + halfDepth;
        box.minX = Math.min(minX, maxX);
        box.minY = Math.min(minY, maxY);
        box.minZ = Math.min(minZ, maxZ);
        box.maxX = Math.max(minX, maxX);
        box.maxY = Math.max(minY, maxY);
        box.maxZ = Math.max(minZ, maxZ);
    }

    @Generated
    private GetBoundingBox() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

