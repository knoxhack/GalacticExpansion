package com.astroframe.galactic.core.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking fields or methods that should be automatically registered.
 * This can be used on static fields or methods that return objects that should be registered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Register {
    
    /**
     * The registry name where the object should be registered.
     * If empty, the registry will be determined based on the object type.
     * 
     * @return The registry name
     */
    String registry() default "";
    
    /**
     * The domain to use for registration.
     * If empty, the mod ID will be used.
     * 
     * @return The registration domain
     */
    String domain() default "";
    
    /**
     * The path to use for registration.
     * If empty, the field or method name will be used.
     * 
     * @return The registration path
     */
    String path() default "";
    
    /**
     * Whether to skip registration if an entry with the same ID already exists.
     * 
     * @return true to skip if already registered, false to throw an exception
     */
    boolean skipIfExists() default false;
}