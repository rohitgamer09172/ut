/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPistonExtendEvent
 *  org.bukkit.event.block.BlockPistonRetractEvent
 */
package ac.grim.grimac.platform.bukkit.events;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.bukkit.utils.convert.BukkitConversionUtils;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.PistonData;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonEvent
implements Listener {
    private final Material SLIME_BLOCK = Material.getMaterial((String)"SLIME_BLOCK");
    private final Material HONEY_BLOCK = Material.getMaterial((String)"HONEY_BLOCK");
    private static final double MAX_HORIZONTAL_DISTANCE = 24.0;
    private static final double MAX_VERTICAL_DISTANCE = 64.0;

    private static boolean isCloseEnough(Vector3i vectorA, Vector3d vectorB) {
        return Math.abs((double)vectorA.getX() - vectorB.getX()) <= 24.0 && Math.abs((double)vectorA.getY() - vectorB.getY()) <= 64.0 && Math.abs((double)vectorA.getZ() - vectorB.getZ()) <= 24.0;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPistonPushEvent(BlockPistonExtendEvent event) {
        boolean hasSlimeBlock = false;
        boolean hasHoneyBlock = false;
        ArrayList<SimpleCollisionBox> boxes = new ArrayList<SimpleCollisionBox>();
        for (Block block : event.getBlocks()) {
            boxes.add(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true).offset(block.getX(), block.getY(), block.getZ()));
            boxes.add(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true).offset(block.getX() + event.getDirection().getModX(), block.getY() + event.getDirection().getModY(), block.getZ() + event.getDirection().getModZ()));
            if (block.getType() == this.SLIME_BLOCK) {
                hasSlimeBlock = true;
            }
            if (block.getType() != this.HONEY_BLOCK) continue;
            hasHoneyBlock = true;
        }
        Block piston = event.getBlock();
        boxes.add(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true).offset(piston.getX() + event.getDirection().getModX(), piston.getY() + event.getDirection().getModY(), piston.getZ() + event.getDirection().getModZ()));
        int chunkX = event.getBlock().getX() >> 4;
        int chunkZ = event.getBlock().getZ() >> 4;
        BlockFace blockFace = BukkitConversionUtils.fromBukkitFace(event.getDirection());
        Vector3i sourcePos = new Vector3i(piston.getX(), piston.getY(), piston.getZ());
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            if (!PistonEvent.isCloseEnough(sourcePos, player.compensatedEntities.self.trackedServerPosition.getPos()) || !player.compensatedWorld.isChunkLoaded(chunkX, chunkZ)) continue;
            int lastTrans = player.lastTransactionSent.get();
            PistonData data = new PistonData(blockFace, boxes, lastTrans, true, hasSlimeBlock, hasHoneyBlock);
            player.latencyUtils.addRealTimeTaskAsync(lastTrans, () -> player.compensatedWorld.activePistons.add(data));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPistonRetractEvent(BlockPistonRetractEvent event) {
        boolean hasSlimeBlock = false;
        boolean hasHoneyBlock = false;
        ArrayList<SimpleCollisionBox> boxes = new ArrayList<SimpleCollisionBox>();
        BlockFace face = BukkitConversionUtils.fromBukkitFace(event.getDirection());
        if (event.getBlocks().isEmpty()) {
            Block piston = event.getBlock();
            boxes.add(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true).offset(piston.getX() + face.getModX(), piston.getY() + face.getModY(), piston.getZ() + face.getModZ()));
        }
        for (Block block : event.getBlocks()) {
            boxes.add(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true).offset(block.getX(), block.getY(), block.getZ()));
            boxes.add(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true).offset(block.getX() + face.getModX(), block.getY() + face.getModY(), block.getZ() + face.getModZ()));
            if (block.getType() == this.SLIME_BLOCK) {
                hasSlimeBlock = true;
            }
            if (block.getType() != this.HONEY_BLOCK) continue;
            hasHoneyBlock = true;
        }
        int chunkX = event.getBlock().getX() >> 4;
        int chunkZ = event.getBlock().getZ() >> 4;
        Vector3i sourcePos = new Vector3i(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            if (!PistonEvent.isCloseEnough(sourcePos, player.compensatedEntities.self.trackedServerPosition.getPos()) || !player.compensatedWorld.isChunkLoaded(chunkX, chunkZ)) continue;
            int lastTrans = player.lastTransactionSent.get();
            PistonData data = new PistonData(face, boxes, lastTrans, false, hasSlimeBlock, hasHoneyBlock);
            player.latencyUtils.addRealTimeTaskAsync(lastTrans, () -> player.compensatedWorld.activePistons.add(data));
        }
    }
}

