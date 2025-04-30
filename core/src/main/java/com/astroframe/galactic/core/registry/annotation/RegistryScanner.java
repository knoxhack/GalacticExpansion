package com.astroframe.galactic.core.registry.annotation;

import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.RegistryManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for scanning classes and registering annotated fields and methods.
 */
public class RegistryScanner {
    
    private static final Logger LOGGER = Logger.getLogger(RegistryScanner.class.getName());
    
    private final String defaultDomain;
    private final Map<Class<?>, String> typeToRegistryMap = new HashMap<>();
    
    /**
     * Create a new registry scanner with the specified default domain.
     * 
     * @param defaultDomain The default domain to use if not specified in annotations
     */
    public RegistryScanner(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }
    
    /**
     * Map a class type to a registry name.
     * This is used when the registry name is not specified in the annotation.
     * 
     * @param <T> The type of objects to map
     * @param type The class of objects to map
     * @param registryName The registry name to map to
     * @return This scanner for method chaining
     */
    public <T> RegistryScanner mapTypeToRegistry(Class<T> type, String registryName) {
        typeToRegistryMap.put(type, registryName);
        return this;
    }
    
    /**
     * Scan a class for annotated fields and methods and register them.
     * 
     * @param clazz The class to scan
     * @return The number of objects registered
     */
    public int scan(Class<?> clazz) {
        int count = 0;
        count += scanFields(clazz);
        count += scanMethods(clazz);
        return count;
    }
    
    /**
     * Scan multiple classes for annotated fields and methods and register them.
     * 
     * @param classes The classes to scan
     * @return The total number of objects registered
     */
    public int scanAll(Class<?>... classes) {
        int count = 0;
        for (Class<?> clazz : classes) {
            count += scan(clazz);
        }
        return count;
    }
    
    /**
     * Scan a package for classes and register annotated fields and methods.
     * 
     * @param packageName The package name to scan
     * @return The total number of objects registered
     */
    public int scanPackage(String packageName) {
        // Implementing package scanning requires additional libraries or complex reflection
        // This is a placeholder for future implementation
        LOGGER.warning("Package scanning is not yet implemented");
        return 0;
    }
    
    private int scanFields(Class<?> clazz) {
        int count = 0;
        List<Field> fields = getAllFields(clazz);
        
        for (Field field : fields) {
            Register annotation = field.getAnnotation(Register.class);
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
                
                registerObject(value, annotation, field.getName(), value.getClass());
                count++;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to register field " + field.getName() + " in " + clazz.getName(), e);
            }
        }
        
        return count;
    }
    
    private int scanMethods(Class<?> clazz) {
        int count = 0;
        List<Method> methods = getAllMethods(clazz);
        
        for (Method method : methods) {
            Register annotation = method.getAnnotation(Register.class);
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
                
                registerObject(value, annotation, method.getName(), value.getClass());
                count++;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to register method " + method.getName() + " in " + clazz.getName(), e);
            }
        }
        
        return count;
    }
    
    @SuppressWarnings("unchecked")
    private <T> void registerObject(Object value, Register annotation, String defaultPath, Class<?> sourceType) {
        String registryName = annotation.registry();
        if (registryName.isEmpty()) {
            registryName = determineRegistryName(value.getClass());
            if (registryName == null) {
                LOGGER.warning("Could not determine registry for " + value.getClass().getName());
                return;
            }
        }
        
        Registry<?> baseRegistry = RegistryManager.getInstance()
                .getRegistry(registryName)
                .orElseGet(() -> RegistryManager.getInstance().createRegistry(registryName));
        
        // We know the registry is compatible with the value type, but Java generics require a cast
        @SuppressWarnings("rawtypes")
        Registry rawRegistry = baseRegistry;
        
        String domain = annotation.domain().isEmpty() ? defaultDomain : annotation.domain();
        String path = annotation.path().isEmpty() ? defaultPath : annotation.path();
        
        try {
            rawRegistry.register(domain, path, value);
        } catch (IllegalArgumentException e) {
            if (annotation.skipIfExists()) {
                LOGGER.fine("Skipping registration of " + domain + ":" + path + " as it already exists");
            } else {
                throw e;
            }
        }
    }
    
    private String determineRegistryName(Class<?> type) {
        for (Map.Entry<Class<?>, String> entry : typeToRegistryMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        
        // Get all fields including inherited ones
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        
        return fields;
    }
    
    private List<Method> getAllMethods(Class<?> clazz) {
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