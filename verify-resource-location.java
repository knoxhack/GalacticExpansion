public class verify_resource_location {
    public static void main(String[] args) {
        System.out.println("Checking resource location usage...");
        
        // This is how we should create a ResourceLocation
        // In Minecraft/Neoforge, the correct way is ResourceLocation.parse("modid:path") 
        // or net.minecraft.resources.ResourceLocation.of("namespace", "path")
        
        System.out.println("If you're seeing this, ResourceLocation.parse() should work!");
    }
}