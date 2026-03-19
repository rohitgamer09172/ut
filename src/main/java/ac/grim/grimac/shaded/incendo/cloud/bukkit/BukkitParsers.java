/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitParserParameters;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.CloudBukkitCapabilities;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.annotation.specifier.AllowEmptySelection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.annotation.specifier.DefaultNamespace;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.annotation.specifier.RequireExplicitNamespace;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.MultipleEntitySelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.MultiplePlayerSelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.BlockPredicateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.EnchantmentParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.ItemStackParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.ItemStackPredicateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.MaterialParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.NamespacedKeyParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.PlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.WorldParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.Location2DParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.MultipleEntitySelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SingleEntitySelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SinglePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserParameters;
import java.lang.reflect.Method;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(status=API.Status.INTERNAL)
public final class BukkitParsers {
    private BukkitParsers() {
    }

    public static <C> void register(CommandManager<C> manager) {
        manager.parserRegistry().registerParser(WorldParser.worldParser()).registerParser(MaterialParser.materialParser()).registerParser(PlayerParser.playerParser()).registerParser(OfflinePlayerParser.offlinePlayerParser()).registerParser(EnchantmentParser.enchantmentParser()).registerParser(LocationParser.locationParser()).registerParser(Location2DParser.location2DParser()).registerParser(ItemStackParser.itemStackParser()).registerParser(SingleEntitySelectorParser.singleEntitySelectorParser()).registerParser(SinglePlayerSelectorParser.singlePlayerSelectorParser());
        manager.parserRegistry().registerAnnotationMapper(AllowEmptySelection.class, (annotation, type) -> ParserParameters.single(BukkitParserParameters.ALLOW_EMPTY_SELECTOR_RESULT, annotation.value()));
        manager.parserRegistry().registerParserSupplier(TypeToken.get(MultipleEntitySelector.class), parserParameters -> new MultipleEntitySelectorParser(parserParameters.get(BukkitParserParameters.ALLOW_EMPTY_SELECTOR_RESULT, true)));
        manager.parserRegistry().registerParserSupplier(TypeToken.get(MultiplePlayerSelector.class), parserParameters -> new MultiplePlayerSelectorParser(parserParameters.get(BukkitParserParameters.ALLOW_EMPTY_SELECTOR_RESULT, true)));
        if (CraftBukkitReflection.classExists("org.bukkit.NamespacedKey")) {
            BukkitParsers.registerParserSupplierFor(manager, NamespacedKeyParser.class);
            manager.parserRegistry().registerAnnotationMapper(RequireExplicitNamespace.class, (annotation, type) -> ParserParameters.single(BukkitParserParameters.REQUIRE_EXPLICIT_NAMESPACE, true));
            manager.parserRegistry().registerAnnotationMapper(DefaultNamespace.class, (annotation, type) -> ParserParameters.single(BukkitParserParameters.DEFAULT_NAMESPACE, annotation.value()));
        }
        if (manager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            BukkitParsers.registerParserSupplierFor(manager, ItemStackPredicateParser.class);
            BukkitParsers.registerParserSupplierFor(manager, BlockPredicateParser.class);
        }
    }

    private static void registerParserSupplierFor(CommandManager<?> manager, @NonNull Class<?> argumentClass) {
        try {
            Method registerParserSuppliers = argumentClass.getDeclaredMethod("registerParserSupplier", CommandManager.class);
            registerParserSuppliers.setAccessible(true);
            registerParserSuppliers.invoke(null, manager);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}

