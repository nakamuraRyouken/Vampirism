package de.teamlapen.lib.lib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DefaultBoolean {
    boolean alternateValue() default false;

    String comment() default "";

    /**
     * Whether this default value has an alternate or not.
     * Used if two alternative default configurations shall provided. (E.g. Realism mode)
     * Necessary because the default for the alternative value cannot be null
     *
     * @return
     */
    boolean hasAlternate() default false;

    String name() default "";

    boolean value();
}