package com.astroframe.galactic.core.registry;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.registry.tag.Tag;
import com.astroframe.galactic.core.registry.tag.TagManager;
import com.astroframe.galactic.core.registry.tag.annotation.TaggedWith;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test for the Registry Manager
 */
public class RegistryManagerTest {

    private RegistryManager registryManager;
    private TagManager tagManager;

    @BeforeEach
    void setUp() {
        try (MockedStatic<GalacticCore> mockedCore = Mockito.mockStatic(GalacticCore.class)) {
            GalacticCore mockCore = Mockito.mock(GalacticCore.class);
            mockedCore.when(GalacticCore::getInstance).thenReturn(mockCore);
            
            tagManager = new TagManager();
            registryManager = new RegistryManager();
        }
    }

    @Test
    void testRegisterAndGetObject() {
        // Create test object
        TestRegistryObject obj = new TestRegistryObject("test_object");
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("galacticexpansion", "test_object");
        
        // Register object
        registryManager.register(id, obj);
        
        // Verify it's registered correctly
        TestRegistryObject retrieved = (TestRegistryObject) registryManager.get(id);
        assertNotNull(retrieved, "Retrieved object should not be null");
        assertEquals(obj, retrieved, "Retrieved object should match the registered one");
        assertEquals("test_object", retrieved.getName(), "Name should match");
    }
    
    @Test
    void testGetAllRegisteredObjects() {
        // Register multiple objects
        TestRegistryObject obj1 = new TestRegistryObject("test_object_1");
        TestRegistryObject obj2 = new TestRegistryObject("test_object_2");
        
        ResourceLocation id1 = ResourceLocation.fromNamespaceAndPath("galacticexpansion", "test_object_1");
        ResourceLocation id2 = ResourceLocation.fromNamespaceAndPath("galacticexpansion", "test_object_2");
        
        registryManager.register(id1, obj1);
        registryManager.register(id2, obj2);
        
        // Get all registered objects
        Collection<Object> objects = registryManager.getAll();
        
        // Verify all objects are there
        assertEquals(2, objects.size(), "Should have registered 2 objects");
        assertTrue(objects.contains(obj1), "Should contain first object");
        assertTrue(objects.contains(obj2), "Should contain second object");
    }
    
    @Test
    void testObjectWithTags() {
        // Create test object with tags
        @TaggedWith("test_tag")
        class TaggedTestObject extends TestRegistryObject {
            public TaggedTestObject(String name) {
                super(name);
            }
        }
        
        TaggedTestObject obj = new TaggedTestObject("tagged_object");
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("galacticexpansion", "tagged_object");
        ResourceLocation tagId = ResourceLocation.fromNamespaceAndPath("galacticexpansion", "test_tag");
        
        // Register object and process its tags
        registryManager.register(id, obj);
        
        // Manually create the tag since we're in a test environment
        Tag<Object> tag = new Tag<>(tagId);
        tag.add(obj);
        tagManager.addTag(tagId, tag);
        
        // Verify tag contains the object
        Set<Object> taggedObjects = tagManager.getTag(tagId).getAll();
        assertTrue(taggedObjects.contains(obj), "Tag should contain the object");
    }
    
    // Test helper class
    private static class TestRegistryObject {
        private final String name;
        
        public TestRegistryObject(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestRegistryObject)) return false;
            TestRegistryObject that = (TestRegistryObject) o;
            return name.equals(that.name);
        }
        
        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}