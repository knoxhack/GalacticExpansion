#!/bin/bash

# This script creates an all-in-one JAR containing all modules
# This is a more convenient distribution format for users who don't want to manage multiple JARs

echo "Creating all-in-one JAR for Galactic Expansion mod..."

# Create output directories
mkdir -p combined-jar
mkdir -p temp-extraction

# Create base directory for META-INF
mkdir -p temp-extraction/META-INF

# First, gather all JAR files from the modules
module_jars=()
echo "Finding module JARs..."
for module in core biotech energy machinery power space robotics construction utilities vehicles weaponry; do
  jar_path="./${module}/build/libs/galactic${module}-0.1.0.jar"
  if [ -f "$jar_path" ]; then
    echo "  - Found: $jar_path"
    module_jars+=("$jar_path")
  else
    echo "  - Warning: $jar_path not found"
  fi
done

if [ ${#module_jars[@]} -eq 0 ]; then
  echo "Error: No module JARs found! Run the build first."
  exit 1
fi

echo "Found ${#module_jars[@]} module JARs."

# Extract all JARs to the temp directory
echo "Extracting module contents..."
for jar in "${module_jars[@]}"; do
  echo "  - Extracting: $jar"
  (cd temp-extraction && jar xf "../$jar")
done

# Create special merged manifest
echo "Creating merged manifest..."
cat > temp-extraction/META-INF/MANIFEST.MF << EOF
Manifest-Version: 1.0
Implementation-Title: Galactic Expansion (All-in-One)
Implementation-Version: 0.1.0
Specification-Vendor: AstroFrame
Implementation-Vendor: AstroFrame
FMLModType: LIBRARY
EOF

# Create the combined JAR
echo "Creating all-in-one JAR file..."
(cd temp-extraction && jar cf ../combined-jar/GalacticExpansion-all-in-one-0.1.0.jar .)

# Verify the JAR was created
if [ -f "combined-jar/GalacticExpansion-all-in-one-0.1.0.jar" ]; then
  jar_size=$(du -h "combined-jar/GalacticExpansion-all-in-one-0.1.0.jar" | cut -f1)
  echo "All-in-one JAR created successfully: combined-jar/GalacticExpansion-all-in-one-0.1.0.jar (${jar_size})"
  
  # Create a special all-in-one fixed name JAR for Minecraft
  echo "Creating Minecraft-compatible version..."
  cp "combined-jar/GalacticExpansion-all-in-one-0.1.0.jar" "fixed-jars/galacticexpansion_all-in-one-0.1.0.jar"
  echo "Minecraft-compatible version created: fixed-jars/galacticexpansion_all-in-one-0.1.0.jar"
  
  # Update the fixed-jars zip
  echo "Updating fixed-jars package..."
  cd fixed-jars
  zip -u ../galactic-expansion-fixed-v0.1.0-neoforge-1.21.5.zip galacticexpansion_all-in-one-0.1.0.jar
  cd ..
else
  echo "Error: Failed to create all-in-one JAR!"
  exit 1
fi

# Clean up temp files
echo "Cleaning up temporary files..."
rm -rf temp-extraction

echo "All-in-one JAR packaging complete!"