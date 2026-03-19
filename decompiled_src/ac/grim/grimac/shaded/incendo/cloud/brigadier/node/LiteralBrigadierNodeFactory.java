/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Command
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.ArgumentBuilder
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.tree.LiteralCommandNode
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.node;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierSetting;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.ArgumentTypeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.ArgumentMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.BrigadierNodeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.ImmutableArgumentMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionPredicate;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.BrigadierSuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.CloudDelegatingSuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.SuggestionsType;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.MappedArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.aggregate.AggregateParser;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionFactory;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(status=API.Status.STABLE, since="2.0.0")
public final class LiteralBrigadierNodeFactory<C, S>
implements BrigadierNodeFactory<C, S, LiteralCommandNode<S>> {
    private final CloudBrigadierManager<C, S> cloudBrigadierManager;
    private final CommandManager<C> commandManager;
    private final BrigadierSuggestionFactory<C, S> brigadierSuggestionFactory;

    public LiteralBrigadierNodeFactory(@NonNull CloudBrigadierManager<C, S> cloudBrigadierManager, @NonNull CommandManager<C> commandManager, @NonNull SuggestionFactory<C, ? extends TooltipSuggestion> suggestionFactory) {
        this.cloudBrigadierManager = cloudBrigadierManager;
        this.commandManager = commandManager;
        this.brigadierSuggestionFactory = new BrigadierSuggestionFactory<C, S>(cloudBrigadierManager, commandManager, suggestionFactory);
    }

    @Override
    public @NonNull LiteralCommandNode<S> createNode(@NonNull String label, @NonNull CommandNode<C> cloudCommand, @NonNull com.mojang.brigadier.Command<S> executor, @NonNull BrigadierPermissionChecker<C> permissionChecker) {
        LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)LiteralArgumentBuilder.literal((String)label).requires(this.requirement(cloudCommand, permissionChecker));
        this.updateExecutes((ArgumentBuilder<S, ?>)literalArgumentBuilder, cloudCommand, executor);
        LiteralCommandNode constructedRoot = literalArgumentBuilder.build();
        for (CommandNode<C> child : cloudCommand.children()) {
            constructedRoot.addChild(this.constructCommandNode(child, permissionChecker, executor).build());
        }
        return constructedRoot;
    }

    private @NonNull BrigadierPermissionPredicate<C, S> requirement(@NonNull CommandNode<C> cloudCommand, @NonNull BrigadierPermissionChecker<C> permissionChecker) {
        return new BrigadierPermissionPredicate<C, S>(this.cloudBrigadierManager.senderMapper(), permissionChecker, cloudCommand);
    }

    @Override
    public @NonNull LiteralCommandNode<S> createNode(@NonNull String label, @NonNull Command<C> cloudCommand, @NonNull com.mojang.brigadier.Command<S> executor, @NonNull BrigadierPermissionChecker<C> permissionChecker) {
        CommandNode<C> node = this.commandManager.commandTree().getNamedNode(cloudCommand.rootComponent().name());
        Objects.requireNonNull(node, "node");
        return this.createNode(label, node, executor, permissionChecker);
    }

    @Override
    public @NonNull LiteralCommandNode<S> createNode(@NonNull String label, @NonNull Command<C> cloudCommand, @NonNull com.mojang.brigadier.Command<S> executor) {
        return this.createNode(label, cloudCommand, executor, (C sender, Permission permission) -> this.commandManager.testPermission(sender, permission).allowed());
    }

    private @NonNull ArgumentBuilder<S, ?> constructCommandNode(@NonNull CommandNode<C> root, @NonNull BrigadierPermissionChecker<C> permissionChecker, @NonNull com.mojang.brigadier.Command<S> executor) {
        if (root.component().parser() instanceof AggregateParser) {
            AggregateParser aggregateParser = (AggregateParser)root.component().parser();
            return this.constructAggregateNode(aggregateParser, root, permissionChecker, executor);
        }
        ArgumentBuilder<S, ?> argumentBuilder = root.component().type() == CommandComponent.ComponentType.LITERAL ? this.createLiteralArgumentBuilder(root.component(), root, permissionChecker) : this.createVariableArgumentBuilder(root.component(), root, permissionChecker);
        this.updateExecutes(argumentBuilder, root, executor);
        for (CommandNode<C> node : root.children()) {
            argumentBuilder.then(this.constructCommandNode(node, permissionChecker, executor));
        }
        return argumentBuilder;
    }

    private @NonNull ArgumentBuilder<S, ?> createLiteralArgumentBuilder(@NonNull CommandComponent<C> component, @NonNull CommandNode<C> root, @NonNull BrigadierPermissionChecker<C> permissionChecker) {
        return LiteralArgumentBuilder.literal((String)component.name()).requires(this.requirement(root, permissionChecker));
    }

    private @NonNull ArgumentBuilder<S, ?> createVariableArgumentBuilder(@NonNull CommandComponent<C> component, @NonNull CommandNode<C> root, @NonNull BrigadierPermissionChecker<C> permissionChecker) {
        ArgumentMapping<S> argumentMapping = this.getArgument(component.valueType(), component.parser());
        SuggestionProvider<S> provider = argumentMapping.suggestionsType() == SuggestionsType.CLOUD_SUGGESTIONS ? new CloudDelegatingSuggestionProvider<C, S>(this.brigadierSuggestionFactory, root) : argumentMapping.suggestionProvider();
        return RequiredArgumentBuilder.argument((String)component.name(), argumentMapping.argumentType()).suggests(provider).requires(this.requirement(root, permissionChecker));
    }

    private @NonNull ArgumentBuilder<S, ?> constructAggregateNode(@NonNull AggregateParser<C, ?> aggregateParser, @NonNull CommandNode<C> root, @NonNull BrigadierPermissionChecker<C> permissionChecker, @NonNull com.mojang.brigadier.Command<S> executor) {
        Iterator<CommandComponent<C>> components = aggregateParser.components().iterator();
        ArrayList<Object> argumentBuilders = new ArrayList<Object>();
        while (components.hasNext()) {
            CommandComponent<C> component = components.next();
            ArgumentBuilder<S, ?> fragmentBuilder = this.createVariableArgumentBuilder(component, root, permissionChecker);
            if (this.cloudBrigadierManager.settings().get(BrigadierSetting.FORCE_EXECUTABLE)) {
                fragmentBuilder.executes(executor);
            }
            argumentBuilders.add(fragmentBuilder);
        }
        ArgumentBuilder tail = (ArgumentBuilder)argumentBuilders.get(argumentBuilders.size() - 1);
        for (CommandNode commandNode : root.children()) {
            tail.then(this.constructCommandNode(commandNode, permissionChecker, executor));
        }
        this.updateExecutes(tail, root, executor);
        for (int i = argumentBuilders.size() - 1; i > 0; --i) {
            ((ArgumentBuilder)argumentBuilders.get(i - 1)).then((ArgumentBuilder)argumentBuilders.get(i));
        }
        return (ArgumentBuilder)argumentBuilders.get(0);
    }

    private <K extends ArgumentParser<C, ?>> @NonNull ArgumentMapping<S> getArgument(@NonNull TypeToken<?> valueType, @NonNull K argumentParser) {
        if (argumentParser instanceof MappedArgumentParser) {
            return this.getArgument(valueType, ((MappedArgumentParser)argumentParser).baseParser());
        }
        BrigadierMapping<C, ?, S> mapping = this.cloudBrigadierManager.mappings().mapping(argumentParser.getClass());
        if (mapping == null || mapping.mapper() == null) {
            return this.getDefaultMapping(valueType);
        }
        SuggestionProvider<S> suggestionProvider = mapping.makeSuggestionProvider(argumentParser);
        if (suggestionProvider == BrigadierMapping.delegateSuggestions()) {
            return ImmutableArgumentMapping.builder().argumentType(mapping.mapper().apply(argumentParser)).suggestionsType(SuggestionsType.CLOUD_SUGGESTIONS).build();
        }
        return ImmutableArgumentMapping.builder().argumentType(mapping.mapper().apply(argumentParser)).suggestionProvider(suggestionProvider).build();
    }

    private @NonNull ArgumentMapping<S> getDefaultMapping(@NonNull TypeToken<?> type) {
        ArgumentType<?> argumentType;
        ArgumentTypeFactory<?> argumentTypeSupplier = this.cloudBrigadierManager.defaultArgumentTypeFactories().get(GenericTypeReflector.erase(type.getType()));
        if (argumentTypeSupplier != null && (argumentType = argumentTypeSupplier.create()) != null) {
            return ImmutableArgumentMapping.builder().argumentType(argumentType).build();
        }
        return ImmutableArgumentMapping.builder().argumentType((ArgumentType<?>)StringArgumentType.word()).suggestionsType(SuggestionsType.CLOUD_SUGGESTIONS).build();
    }

    private void updateExecutes(@NonNull ArgumentBuilder<S, ?> builder, @NonNull CommandNode<C> node, @NonNull com.mojang.brigadier.Command<S> executor) {
        if (this.cloudBrigadierManager.settings().get(BrigadierSetting.FORCE_EXECUTABLE) || node.isLeaf() || node.component().optional() || node.command() != null || node.children().stream().map(CommandNode::component).filter(Objects::nonNull).anyMatch(CommandComponent::optional)) {
            builder.executes(executor);
        }
    }
}

