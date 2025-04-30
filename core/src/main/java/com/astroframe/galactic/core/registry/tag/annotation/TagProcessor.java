package com.astroframe.galactic.core.registry.tag.annotation;

import com.astroframe.galactic.core.registry.tag.Tag;
import com.astroframe.galactic.core.registry.tag.TagManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for processing TaggedWith annotations and adding objects to tags.
 */
public class TagProcessor {
    
    private static final Logger LOGGER = Logger.getLogger(TagProcessor.class.getName());
    
    /**
     * Process a class for TaggedWith annotations and add the annotated objects to tags.
     * 
     * @param clazz The class to process
     * @return The number of objects processed
     */
    public static int process(Class<?> clazz) {
        int count = 0;
        count += processFields(clazz);
        count += processMethods(clazz);
        return count;
    }
    
    /**
     * Process multiple classes for TaggedWith annotations.
     * 
     * @param classes The classes to process
     * @return The total number of objects processed
     */
    public static int processAll(Class<?>... classes) {
        int count = 0;
        for (Class<?> clazz : classes) {
            count += process(clazz);
        }
        return count;
    }
    
    private static int processFields(Class<?> clazz) {
        int count = 0;
        List<Field> fields = getAllFields(clazz);
        
        for (Field field : fields) {
            TaggedWith annotation = field.getAnnotation(TaggedWith.class);
            if (annotation == null) {
                continue;
            }
            
            if (!Modifier.isStatic(field.getModifiers())) {
                LOGGER.warning("Skipping non-static field " + field.getName() + " in " + clazz.getName());
                continue;
            }
            
            try {
                field.setAccessible(true);
                Object value = field.get(null);
                if (value == null) {
                    LOGGER.warning("Skipping null field " + field.getName() + " in " + clazz.getName());
                    continue;
                }
                
                addToTags(value, annotation);
                count++;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to process field " + field.getName() + " in " + clazz.getName(), e);
            }
        }
        
        return count;
    }
    
    private static int processMethods(Class<?> clazz) {
        int count = 0;
        List<Method> methods = getAllMethods(clazz);
        
        for (Method method : methods) {
            TaggedWith annotation = method.getAnnotation(TaggedWith.class);
            if (annotation == null) {
                continue;
            }
            
            if (!Modifier.isStatic(method.getModifiers())) {
                LOGGER.warning("Skipping non-static method " + method.getName() + " in " + clazz.getName());
                continue;
            }
            
            if (method.getParameterCount() > 0) {
                LOGGER.warning("Skipping method with parameters " + method.getName() + " in " + clazz.getName());
                continue;
            }
            
            if (method.getReturnType() == void.class) {
                LOGGER.warning("Skipping void method " + method.getName() + " in " + clazz.getName());
                continue;
            }
            
            try {
                method.setAccessible(true);
                Object value = method.invoke(null);
                if (value == null) {
                    LOGGER.warning("Skipping method " + method.getName() + " in " + clazz.getName() + " that returned null");
                    continue;
                }
                
                addToTags(value, annotation);
                count++;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to process method " + method.getName() + " in " + clazz.getName(), e);
            }
        }
        
        return count;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> void addToTags(Object value, TaggedWith annotation) {
        String typeKey = annotation.typeKey();
        String[] tagIds = annotation.value();
        
        for (String tagId : tagIds) {
            Tag<T> tag = TagManager.getInstance().getOrCreateTag(typeKey, tagId);
            tag.add((T) value);
        }
    }
    
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        
        // Get all fields including inherited ones
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        
        return fields;
    }
    
    private static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        
        // Get all methods including inherited ones
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            methods.addAll(Arrays.asList(current.getDeclaredMethods()));
            current = current.getSuperclass();
        }
        
        return methods;
    }
}