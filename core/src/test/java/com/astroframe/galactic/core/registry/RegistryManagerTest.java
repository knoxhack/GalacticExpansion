package com.astroframe.galactic.core.registry;

import com.astroframe.galactic.core.registry.tag.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Registry functionality (without NeoForge dependencies)
 */
public class RegistryManagerTest {

    /**
     * Basic test to check if a simple registry works correctly
     */
    @Test
    void testRegistry() {
        // Create a simple registry
        Registry<TestObject> registry = new Registry<>("test_registry");
        
        // Register some objects
        TestObject obj1 = new TestObject("Object 1");
        TestObject obj2 = new TestObject("Object 2");
        
        registry.register("testmod", "object1", obj1);
        registry.register("testmod", "object2", obj2);
        
        // Verify we can get the objects back
        assertEquals(obj1, registry.get("testmod:object1").orElse(null));
        assertEquals(obj2, registry.get("testmod:object2").orElse(null));
        assertEquals(2, registry.getValues().size());
        
        // Test contains check
        assertTrue(registry.contains("testmod:object1"));
        assertFalse(registry.contains("testmod:doesnotexist"));
    }
    
    /**
     * Test for the Tag class to verify it works correctly
     */
    @Test
    void testTag() {
        // Create a tag
        Tag<String> tag = new Tag<>("test_tag");
        
        // Add values
        tag.add("value1");
        tag.add("value2");
        tag.add("value3");
        
        // Verify values are there
        assertEquals(3, tag.getValues().size());
        assertTrue(tag.contains("value1"));
        assertTrue(tag.contains("value2"));
        assertTrue(tag.contains("value3"));
        assertFalse(tag.contains("value4"));
        
        // Remove a value
        tag.remove("value2");
        
        // Verify removal worked
        assertEquals(2, tag.getValues().size());
        assertTrue(tag.contains("value1"));
        assertFalse(tag.contains("value2"));
        assertTrue(tag.contains("value3"));
        
        // Test ID
        assertEquals("test_tag", tag.getId());
    }
    
    /**
     * Simple test class for registry testing
     */
    private static class TestObject {
        private final String name;
        
        public TestObject(String name) {
            this.name = name;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestObject)) return false;
            TestObject that = (TestObject) o;
            return name.equals(that.name);
        }
        
        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}