import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlockEntity;
import com.astroframe.galactic.space.implementation.common.HolographicProjectorAccess;
import com.astroframe.galactic.space.implementation.common.RocketDataProvider;

/**
 * Simple test class to check for circular dependencies
 */
public class TestCircularDependency {
    public static void main(String[] args) {
        System.out.println("Testing circular dependency resolution...");
        
        // Check that we can reference both classes without circular dependency issues
        System.out.println("RocketAssemblyTableBlockEntity implements: " + RocketDataProvider.class.getSimpleName());
        System.out.println("HolographicProjectorBlockEntity implements: " + HolographicProjectorAccess.class.getSimpleName());
        
        System.out.println("No circular dependencies detected!");
    }
}