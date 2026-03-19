/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data.attribute;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.latency.CompensatedEntities;
import ac.grim.grimac.utils.math.GrimMath;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ValuedAttribute {
    private static final Function<Double, Double> DEFAULT_GET_REWRITE = Function.identity();
    private final Attribute attribute;
    private final double min;
    private final double max;
    private final double defaultValue;
    private WrapperPlayServerUpdateAttributes.Property lastProperty;
    private double value;
    private BiFunction<Double, Double, Double> setRewriter;
    private Function<Double, Double> getRewriter;

    private ValuedAttribute(Attribute attribute, double defaultValue, double min, double max) {
        if (defaultValue < min || defaultValue > max) {
            throw new IllegalArgumentException("Default value must be between min and max!");
        }
        this.attribute = attribute;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.getRewriter = DEFAULT_GET_REWRITE;
    }

    public static ValuedAttribute ranged(Attribute attribute, double defaultValue, double min, double max) {
        return new ValuedAttribute(attribute, defaultValue, min, max);
    }

    public ValuedAttribute withSetRewriter(BiFunction<Double, Double, Double> rewriteFunction) {
        this.setRewriter = rewriteFunction;
        return this;
    }

    public ValuedAttribute requiredVersion(GrimPlayer player, ClientVersion requiredVersion) {
        this.withSetRewriter((oldValue, newValue) -> {
            if (player.getClientVersion().isOlderThan(requiredVersion)) {
                return oldValue;
            }
            return newValue;
        });
        return this;
    }

    public ValuedAttribute withGetRewriter(Function<Double, Double> getRewriteFunction) {
        this.getRewriter = getRewriteFunction;
        return this;
    }

    public Attribute attribute() {
        return this.attribute;
    }

    public void reset() {
        this.value = this.defaultValue;
        this.lastProperty = null;
    }

    public double get() {
        return this.getRewriter.apply(this.value);
    }

    public void override(double value) {
        this.value = value;
    }

    @Deprecated
    public Optional<WrapperPlayServerUpdateAttributes.Property> property() {
        return Optional.ofNullable(this.lastProperty);
    }

    public void recalculate() {
        this.with(this.lastProperty);
    }

    public double with(WrapperPlayServerUpdateAttributes.Property property) {
        double baseValue = property.getValue();
        double additionSum = 0.0;
        double multiplyBaseSum = 0.0;
        double multiplyTotalProduct = 1.0;
        List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers = property.getModifiers();
        modifiers.removeIf(modifier -> modifier.getUUID().equals(CompensatedEntities.SPRINTING_MODIFIER_UUID) || modifier.getName().getKey().equals("sprinting"));
        for (WrapperPlayServerUpdateAttributes.PropertyModifier modifier2 : modifiers) {
            switch (modifier2.getOperation()) {
                case ADDITION: {
                    additionSum += modifier2.getAmount();
                    break;
                }
                case MULTIPLY_BASE: {
                    multiplyBaseSum += modifier2.getAmount();
                    break;
                }
                case MULTIPLY_TOTAL: {
                    multiplyTotalProduct *= 1.0 + modifier2.getAmount();
                }
            }
        }
        double newValue = GrimMath.clamp((baseValue + additionSum) * (1.0 + multiplyBaseSum) * multiplyTotalProduct, this.min, this.max);
        if (this.setRewriter != null) {
            newValue = this.setRewriter.apply(this.value, newValue);
        }
        if (newValue < this.min || newValue > this.max) {
            throw new IllegalArgumentException("New value must be between min and max!");
        }
        this.lastProperty = property;
        this.value = newValue;
        return this.value;
    }
}

