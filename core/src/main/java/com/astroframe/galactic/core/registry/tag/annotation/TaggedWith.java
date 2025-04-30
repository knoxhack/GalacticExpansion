package com.astroframe.galactic.core.registry.tag.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking fields or methods that should be automatically added to tags.
 * This can be used in conjunction with the Register annotation to both register an object
 * and add it to one or more tags.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface TaggedWith {
    
    /**
     * The type key for the tag.
     * This should match the tag type when retrieving the tag.
     * 
     * @return The tag type key
     */
    String typeKey();
    
    /**
     * The ID of the specific tag to add the object to.
     * 
     * @return The tag ID
     */
    String tagId() default "";
    
    /**
     * The IDs of the tags to add the object to.
     * Tags will be created if they don't exist.
     * 
     * @return An array of tag IDs
     */
    String[] value() default {};
}