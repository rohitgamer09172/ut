/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.BlockCommandSender
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.util.Vector
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandContextKeys;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.Location2D;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationCoordinate;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationCoordinateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationCoordinateType;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationParser;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Location2DParser<C>
implements ArgumentParser<C, Location2D>,
BlockingSuggestionProvider.Strings<C> {
    private final LocationCoordinateParser<C> locationCoordinateParser = new LocationCoordinateParser();

    @API(status=API.Status.STABLE, since="2.0.0")
    public static <C> @NonNull ParserDescriptor<C, Location2D> location2DParser() {
        return ParserDescriptor.of(new Location2DParser<C>(), Location2D.class);
    }

    @API(status=API.Status.STABLE, since="2.0.0")
    public static <C> @NonNull CommandComponent.Builder<C, Location2D> location2DComponent() {
        return CommandComponent.builder().parser(Location2DParser.location2DParser());
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull Location2D> parse(@NonNull CommandContext<@NonNull C> commandContext, @NonNull CommandInput commandInput) {
        if (commandInput.remainingTokens() < 2) {
            return ArgumentParseResult.failure(new LocationParser.LocationParseException(commandContext, LocationParser.LocationParseException.FailureReason.WRONG_FORMAT, commandInput.remainingInput()));
        }
        LocationCoordinate[] coordinates = new LocationCoordinate[2];
        for (int i = 0; i < 2; ++i) {
            if (commandInput.peekString().isEmpty()) {
                return ArgumentParseResult.failure(new LocationParser.LocationParseException(commandContext, LocationParser.LocationParseException.FailureReason.WRONG_FORMAT, commandInput.remainingInput()));
            }
            ArgumentParseResult<@NonNull LocationCoordinate> coordinate = this.locationCoordinateParser.parse(commandContext, commandInput);
            if (coordinate.failure().isPresent()) {
                return ArgumentParseResult.failure(coordinate.failure().get());
            }
            coordinates[i] = coordinate.parsedValue().orElseThrow(NullPointerException::new);
        }
        CommandSender bukkitSender = commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
        Location originalLocation = bukkitSender instanceof BlockCommandSender ? ((BlockCommandSender)bukkitSender).getBlock().getLocation() : (bukkitSender instanceof Entity ? ((Entity)bukkitSender).getLocation() : new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0));
        if (coordinates[0].type() == LocationCoordinateType.LOCAL && coordinates[1].type() != LocationCoordinateType.LOCAL) {
            return ArgumentParseResult.failure(new LocationParser.LocationParseException(commandContext, LocationParser.LocationParseException.FailureReason.MIXED_LOCAL_ABSOLUTE, ""));
        }
        if (coordinates[0].type() == LocationCoordinateType.ABSOLUTE) {
            originalLocation.setX(coordinates[0].coordinate());
        } else if (coordinates[0].type() == LocationCoordinateType.RELATIVE) {
            originalLocation.add(coordinates[0].coordinate(), 0.0, 0.0);
        }
        if (coordinates[1].type() == LocationCoordinateType.ABSOLUTE) {
            originalLocation.setZ(coordinates[1].coordinate());
        } else if (coordinates[1].type() == LocationCoordinateType.RELATIVE) {
            originalLocation.add(0.0, 0.0, coordinates[1].coordinate());
        } else {
            Vector declaredPos = new Vector(coordinates[0].coordinate(), 0.0, coordinates[1].coordinate());
            Location local = LocationParser.toLocalSpace(originalLocation, declaredPos);
            return ArgumentParseResult.success(Location2D.from(originalLocation.getWorld(), local.getX(), local.getZ()));
        }
        return ArgumentParseResult.success(Location2D.from(originalLocation.getWorld(), originalLocation.getX(), originalLocation.getZ()));
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> commandContext, @NonNull CommandInput input) {
        return LocationParser.getSuggestions(2, commandContext, input);
    }
}

