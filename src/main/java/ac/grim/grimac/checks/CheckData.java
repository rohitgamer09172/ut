/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface CheckData {
    public String name() default "UNKNOWN";

    public String alternativeName() default "UNKNOWN";

    public String configName() default "DEFAULT";

    public String description() default "No description provided";

    public double decay() default 0.05;

    public double setback() default 25.0;

    public boolean experimental() default false;
}

