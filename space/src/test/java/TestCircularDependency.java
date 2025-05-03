import com.astroframe.galactic.space.implementation.common.RocketDataProvider;

/**
 * Simple test class for basic verification
 */
public class TestCircularDependency {
    public static void main(String[] args) {
        System.out.println("Testing class availability...");
        
        // Check that RocketDataProvider interface is accessible
        Class<?> providerClass = RocketDataProvider.class;
        System.out.println("Successfully loaded: " + providerClass.getSimpleName());
        
        // Report the methods available on the interface
        System.out.println("Methods:");
        java.lang.reflect.Method[] methods = providerClass.getDeclaredMethods();
        for (java.lang.reflect.Method method : methods) {
            System.out.println("  - " + method.getName());
        }
        
        System.out.println("Test passed!");
    }
}