/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.inventory.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.utils.inventory.Inventory;
import ac.grim.grimac.utils.inventory.inventory.AbstractContainerMenu;
import ac.grim.grimac.utils.inventory.inventory.BasicInventoryMenu;
import ac.grim.grimac.utils.inventory.inventory.DispenserMenu;
import ac.grim.grimac.utils.inventory.inventory.HopperMenu;
import ac.grim.grimac.utils.inventory.inventory.NotImplementedMenu;
import lombok.Generated;

public enum MenuType {
    GENERIC_9x1(0),
    GENERIC_9x2(1),
    GENERIC_9x3(2),
    GENERIC_9x4(3),
    GENERIC_9x5(4),
    GENERIC_9x6(5),
    GENERIC_3x3(6),
    CRAFTER_3x3(7),
    ANVIL(8),
    BEACON(9),
    BLAST_FURNACE(10),
    BREWING_STAND(11),
    CRAFTING(12),
    ENCHANTMENT(13),
    FURNACE(14),
    GRINDSTONE(15),
    HOPPER(16),
    LECTERN(17),
    LOOM(18),
    MERCHANT(19),
    SHULKER_BOX(20),
    SMITHING(21),
    SMOKER(22),
    CARTOGRAPHY_TABLE(23),
    STONECUTTER(24),
    UNKNOWN(-1);

    private static final MenuType[] MENU_BY_ID_ARRAY;
    private final int id;

    public static MenuType getMenuType(int id) {
        if (id < 0) {
            return UNKNOWN;
        }
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        if (version.isOlderThan(ServerVersion.V_1_20_3) && id >= 7) {
            ++id;
        }
        if (id >= MENU_BY_ID_ARRAY.length) {
            return UNKNOWN;
        }
        return MENU_BY_ID_ARRAY[id];
    }

    public static AbstractContainerMenu getMenuFromID(GrimPlayer player, Inventory playerInventory, MenuType type) {
        return switch (type.ordinal()) {
            case 0, 1, 2, 3, 4, 5 -> new BasicInventoryMenu(player, playerInventory, type.getId() + 1);
            case 20 -> new BasicInventoryMenu(player, playerInventory, 3);
            case 6 -> new DispenserMenu(player, playerInventory);
            case 16 -> new HopperMenu(player, playerInventory);
            default -> new NotImplementedMenu(player, playerInventory);
        };
    }

    public static AbstractContainerMenu getMenuFromString(GrimPlayer player, Inventory inventory, String legacyType, int slots, int horse) {
        return switch (legacyType) {
            case "minecraft:chest", "minecraft:container" -> new BasicInventoryMenu(player, inventory, slots / 9);
            case "minecraft:dispenser", "minecraft:dropper" -> new DispenserMenu(player, inventory);
            case "minecraft:hopper" -> new HopperMenu(player, inventory);
            case "minecraft:shulker_box" -> new BasicInventoryMenu(player, inventory, 3);
            default -> new NotImplementedMenu(player, inventory);
        };
    }

    @Generated
    private MenuType(int id) {
        this.id = id;
    }

    @Generated
    public int getId() {
        return this.id;
    }

    static {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        MenuType[] menuTypes = MenuType.values();
        int menuIdLimit = version.isOlderThan(ServerVersion.V_1_20_3) ? 23 : menuTypes.length - 1;
        MENU_BY_ID_ARRAY = new MenuType[menuIdLimit];
        System.arraycopy(menuTypes, 0, MENU_BY_ID_ARRAY, 0, menuIdLimit);
    }
}

