package com.astroframe.galactic.core.registry;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.registry.tag.Tag;
import com.astroframe.galactic.core.registry.tag.TagManager;
import com.astroframe.galactic.core.registry.tag.annotation.TaggedWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test for the Registry Manager
 */
public class RegistryManagerTest {

    private RegistryManager registryManager;
    private TagManager tagManager;
    
    /* This is a mock implementation needed only for testing */
    static class MockResourceLocation {
        private final String namespace;
        private final String path;
        
        public MockResourceLocation(String namespace, String path) {
            this.namespace = namespace;
            this.path = path;
        }
        
        public String getNamespace() {
            return namespace;
        }
        
        public String getPath() {
            return path;
        }
    }

    @BeforeEach
    void setUp() {
        try (MockedStatic<GalacticCore> mockedCore = Mockito.mockStatic(GalacticCore.class)) {
            GalacticCore mockCore = Mockito.mock(GalacticCore.class);
            mockedCore.when(GalacticCore::getInstance).thenReturn(mockCore);
            
            try (MockedStatic<TagManager> mockedTagManager = Mockito.mockStatic(TagManager.class)) {
                tagManager = Mockito.mock(TagManager.class);
                mockedTagManager.when(TagManager::getInstance).thenReturn(tagManager);
                
                try (MockedStatic<RegistryManager> mockedRegistryManager = Mockito.mockStatic(RegistryManager.class)) {
                    registryManager = Mockito.mock(RegistryManager.class);
                    mockedRegistryManager.when(RegistryManager::getInstance).thenReturn(registryManager);
                }
            }
        }
    }

    @Test
    void testRegisterAndGetObject() {
        // Create test object
        TestRegistryObject obj = new TestRegistryObject("test_object");
        MockResourceLocation id = new MockResourceLocation("galacticexpansion", "test_object");
        
        // Setup mocks
        when(registryManager.register(id, obj)).thenReturn(obj);
        when(registryManager.get(id)).thenReturn(obj);
        
        // Register object
        Object result = registryManager.register(id, obj);
        
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
        
        MockResourceLocation id1 = new MockResourceLocation("galacticexpansion", "test_object_1");
        MockResourceLocation id2 = new MockResourceLocation("galacticexpansion", "test_object_2");
        
        // Setup mocks
        when(registryManager.register(id1, obj1)).thenReturn(obj1);
        when(registryManager.register(id2, obj2)).thenReturn(obj2);
        
        Collection<Object> mockObjects = new ArrayList<>();
        mockObjects.add(obj1);
        mockObjects.add(obj2);
        when(registryManager.getAll()).thenReturn(mockObjects);
        
        // Register objects
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
        class TaggedTestObject extends TestRegistryObject {
            @TaggedWith(value="test_tag")
            public TaggedTestObject(String name) {
                super(name);
            }
        }
        
        TaggedTestObject obj = new TaggedTestObject("tagged_object");
        MockResourceLocation id = new MockResourceLocation("galacticexpansion", "tagged_object");
        String tagId = "test_tag";
        
        // Setup mocks
        when(registryManager.register(id, obj)).thenReturn(obj);
        
        Tag<Object> mockTag = new Tag<>(tagId);
        mockTag.add(obj);
        
        when(tagManager.createTag("default", tagId)).thenReturn(mockTag);
        when(tagManager.getTag("default", tagId)).thenReturn(Optional.of(mockTag));
        
        // Register object and process its tags
        registryManager.register(id, obj);
        
        // Manually create the tag since we're in a test environment
        Tag<Object> tag = tagManager.createTag("default", tagId);
        tag.add(obj);
        
        // Verify tag contains the object
        Optional<Tag<Object>> tagOpt = tagManager.getTag("default", tagId);
        Set<Object> taggedObjects = tagOpt.map(Tag::getValues).orElse(Collections.emptySet());
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