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

# Fix module dependencies in all mods.toml files
echo "Fixing module dependencies in mods.toml files..."
find temp-extraction -name "mods.toml" | while read toml_file; do
  # Create a backup
  cp "$toml_file" "${toml_file}.bak"
  module_name=$(basename "$(dirname "$(dirname "$toml_file")")")
  
  echo "Processing $toml_file for module $module_name"
  
  # Comment out all [[dependencies.X]] sections for galactic modules
  # We're doing this by adding '#' at the beginning of the dependency lines
  sed -i 's/\[\[dependencies\.\(galactic[a-z]*\)\]\]/#[[dependencies.\1]]/' "$toml_file"
  sed -i 's/modId *= *"\(galactic[a-z]*\)"/#modId = "\1"/' "$toml_file"
  sed -i 's/mandatory *= *\(true\|false\)/#mandatory = \1/' "$toml_file"
  sed -i 's/versionRange *= *"[^"]*"/#versionRange = "0.1.0"/' "$toml_file"
  sed -i 's/ordering *= *"\(NONE\|BEFORE\|AFTER\)"/#ordering = "\1"/' "$toml_file"
  sed -i 's/side *= *"\(BOTH\|CLIENT\|SERVER\)"/#side = "\1"/' "$toml_file"
  
  # Add a note explaining why dependencies are commented out
  sed -i '1 i\# Inter-module dependencies are disabled in the all-in-one JAR\n# All modules are included in this JAR so dependencies are satisfied internally\n' "$toml_file"
  
  echo "Fixed dependencies in $toml_file"
done

# Create a combined logo for the all-in-one JAR
echo "Creating combined logo..."
mkdir -p temp-extraction/assets/galacticexpansion/textures
# If a logo exists in core, copy it as the all-in-one logo
if [ -f "temp-extraction/assets/galacticcore/icon.png" ]; then
  cp "temp-extraction/assets/galacticcore/icon.png" "temp-extraction/assets/galacticexpansion/galacticexpansion.png"
elif [ -f "temp-extraction/assets/galacticcore/textures/icon.png" ]; then
  cp "temp-extraction/assets/galacticcore/textures/icon.png" "temp-extraction/assets/galacticexpansion/galacticexpansion.png"
else
  # Create a simple logo if none exists
  echo "No existing logo found, a placeholder will be used."
fi

# Create special merged manifest
echo "Creating merged manifest..."
cat > temp-extraction/META-INF/MANIFEST.MF << EOF
Manifest-Version: 1.0
Implementation-Title: Galactic Expansion (All-in-One)
Implementation-Version: 0.1.0
Specification-Vendor: AstroFrame
Implementation-Vendor: AstroFrame
Implementation-Timestamp: $(date -u +"%Y-%m-%dT%H:%M:%SZ")
FMLModType: MOD
EOF

# Create special mods.toml file for the all-in-one mod
echo "Creating unified mods.toml file..."
cat > temp-extraction/META-INF/mods.toml << EOF
# This file contains the unified modid for the all-in-one JAR
# It replaces the individual module mods.toml files
modLoader="javafml"
loaderVersion="[47,)"
license="All Rights Reserved"

[[mods]]
modId="galacticexpansion"
version="0.1.0"
displayName="Galactic Expansion (All-in-One)"
logoFile="galacticexpansion.png"
credits="AstroFrame Team"
authors="AstroFrame"
description='''
The complete Galactic Expansion mod package.
This all-in-one JAR contains all modules in a single file.

Explore space, build rockets, and discover new technologies
in this comprehensive space exploration mod for Minecraft.
'''

[[dependencies.galacticexpansion]]
modId="neoforge"
mandatory=true
versionRange="[21.5,)"
ordering="NONE"
side="BOTH"

[[dependencies.galacticexpansion]]
modId="minecraft"
mandatory=true
versionRange="[1.21.1,1.22)"
ordering="NONE"
side="BOTH"
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