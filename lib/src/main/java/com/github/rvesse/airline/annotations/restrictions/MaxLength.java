package com.github.rvesse.airline.annotations.restrictions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface MaxLength {

    /**
     * Maximum allowed length
     * 
     * @return Maximum length
     */
    public int length() default Integer.MAX_VALUE;
}
