package com.astroframe.galactic.core.registry.tag.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for the repeatable TaggedWith annotation.
 * This is used automatically by the Java compiler when multiple TaggedWith
 * annotations are applied to the same element.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface TaggedWithContainer {
    
    /**
     * The contained TaggedWith annotations.
     * 
     * @return An array of TaggedWith annotations
     */
    TaggedWith[] value();
}