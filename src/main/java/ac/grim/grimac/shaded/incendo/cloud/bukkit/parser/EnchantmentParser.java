/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.bukkit.NamespacedKey
 *  org.bukkit.enchantments.Enchantment
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import java.util.ArrayList;
import org.apiguardian.api.API;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class EnchantmentParser<C>
implements ArgumentParser<C, Enchantment>,
BlockingSuggestionProvider.Strings<C> {
    @API(status=API.Status.STABLE, since="2.0.0")
    public static <C> @NonNull ParserDescriptor<C, Enchantment> enchantmentParser() {
        return ParserDescriptor.of(new EnchantmentParser<C>(), Enchantment.class);
    }

    @API(status=API.Status.STABLE, since="2.0.0")
    public static <C> @NonNull CommandComponent.Builder<C, Enchantment> enchantmentComponent() {
        return CommandComponent.builder().parser(EnchantmentParser.enchantmentParser());
    }

    @Override
    public @NonNull ArgumentParseResult<Enchantment> parse(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
        NamespacedKey key;
        String input = commandInput.peekString();
        try {
            key = input.contains(":") ? new NamespacedKey(commandInput.readUntilAndSkip(':'), commandInput.readString()) : NamespacedKey.minecraft((String)commandInput.readString());
        }
        catch (Exception ex) {
            return ArgumentParseResult.failure(new EnchantmentParseException(input, commandContext));
        }
        Enchantment enchantment = Enchantment.getByKey((NamespacedKey)key);
        if (enchantment == null) {
            return ArgumentParseResult.failure(new EnchantmentParseException(input, commandContext));
        }
        return ArgumentParseResult.success(enchantment);
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> commandContext, @NonNull CommandInput input) {
        ArrayList<String> completions = new ArrayList<String>();
        for (Enchantment value : Enchantment.values()) {
            if (value.getKey().getNamespace().equals("minecraft")) {
                completions.add(value.getKey().getKey());
                continue;
            }
            completions.add(value.getKey().toString());
        }
        return completions;
    }

    public static final class EnchantmentParseException
    extends ParserException {
        private final String input;

        public EnchantmentParseException(@NonNull String input, @NonNull CommandContext<?> context) {
            super(EnchantmentParser.class, context, BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_ENCHANTMENT, CaptionVariable.of("input", input));
            this.input = input;
        }

        public @NonNull String input() {
            return this.input;
        }
    }
}

