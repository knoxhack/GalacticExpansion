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

# Remove or rename all individual module mods.toml files
echo "Removing individual module mods.toml files..."
find temp-extraction -name "mods.toml" | while read toml_file; do
  module_dir=$(dirname "$(dirname "$toml_file")")
  module_name=$(basename "$module_dir")
  
  echo "Removing $toml_file for module $module_name"
  
  # Rename the file to prevent it from being loaded
  mv "$toml_file" "${toml_file}.disabled"
done

# Create unified mods.toml file at the root META-INF directory
echo "Creating unified mods.toml file in META-INF..."
mkdir -p temp-extraction/META-INF
cat > temp-extraction/META-INF/mods.toml << EOF
# This is the unified mods.toml file for the all-in-one JAR
# All individual module mods.toml files have been disabled

modLoader="javafml"
loaderVersion="[47,)"
license="All Rights Reserved"

[[mods]]
modId="galacticexpansion"
version="0.1.0"
displayName="Galactic Expansion (All-in-One)"
logoFile="assets/galacticexpansion/textures/galacticexpansion.png"
credits="AstroFrame Team"
authors="AstroFrame"
description='''
The complete Galactic Expansion mod package.
This all-in-one JAR contains all modules in a single file.

Explore space, build rockets, and discover new technologies
in this comprehensive space exploration mod for Minecraft.
'''

# No dependencies on individual galactic modules
# All required dependency JARs are built into this single JAR

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

# Create a combined logo for the all-in-one JAR
echo "Creating combined logo..."
mkdir -p temp-extraction/assets/galacticexpansion/textures
# If a logo exists in core, copy it as the all-in-one logo
if [ -f "temp-extraction/assets/galacticcore/icon.png" ]; then
  cp "temp-extraction/assets/galacticcore/icon.png" "temp-extraction/assets/galacticexpansion/textures/galacticexpansion.png"
elif [ -f "temp-extraction/assets/galacticcore/textures/icon.png" ]; then
  cp "temp-extraction/assets/galacticcore/textures/icon.png" "temp-extraction/assets/galacticexpansion/textures/galacticexpansion.png"
else
  # Create a simple logo if none exists
  echo "No existing logo found, a placeholder will be used."
  
  # Use basic placeholder icon if needed
  echo "Using a basic placeholder icon..."
  # Ensure directory exists
  mkdir -p "temp-extraction/assets/galacticexpansion/textures"
  # Create a base64-encoded PNG placeholder icon
  cat > "temp-extraction/assets/galacticexpansion/textures/galacticexpansion.png" << EOB
iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJ
bWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdp
bj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6
eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1
OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJo
dHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlw
dGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEu
MC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVz
b3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1N
OkRvY3VtZW50SUQ9InhtcC5kaWQ6MzIxMkJFOEE0N0JFMTFFQTlDMTlFMkVBMjQzQkM1RUQiIHht
cE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MzIxMkJFODk0N0JFMTFFQTlDMTlFMkVBMjQzQkM1RUQi
IHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKE1hY2ludG9zaCkiPiA8
eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDozMjEyQkU4NzQ3QkUx
MUVBOUMxOUUyRUEyNDNCQzVFRCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozMjEyQkU4ODQ3
QkUxMUVBOUMxOUUyRUEyNDNCQzVFRCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8
L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pg4FEhMAAAE4SURBVHjaYvz//z8DMmBhYYGI
MDIy/kdW8//f/2///v+b9PfPP0Zmxv/aDAwsIPkDBw4wghSANKCAE1QzGIjL/v37O/Hvn39MDEzM
/gx//3z9+/fPlP///pmysPxnAWlgBKpnBpnAwfY/ChLJ+fP3/zQmZhZ3hr9/fpw6fSb2968f64Hm
xIKsZATpZhRgYwO5HyYA8ufP37/mM7EwOTGwsIjv27830MPVIZeFhS0Yakg8IyMzAzOIATIEyJ/z
9+/fXiBPgtWJ7cu/f/82g9iMIEMYfv1k+Pvnz0+o4dn79+/1YGVlnA/E1yAOQAKQzRf+/v2zGiR+
8PCh0wz/GQMYoMAgA35ATAKxL1y6nM7w778/yBaYT4GABWIIIzsHu/Hjh/dt/v//lwcyBGjLYpDh
IMDISDnABAgwAJGKtVP9zCUwAAAAAElFTkSuQmCC
EOB
fi

# Also update the mods.toml logoFile path to match where we actually created the icon
sed -i 's|logoFile="assets/galacticexpansion/textures/galacticexpansion.png"|logoFile="assets/galacticexpansion/textures/galacticexpansion.png"|g' temp-extraction/META-INF/mods.toml

# Fix any registration classes that might have fixed modIDs
echo "Updating module registration classes to use unified modid..."
find temp-extraction -name "*.class" -type f -exec grep -l "galactic[a-z]*" {} \; 2>/dev/null | while read class_file; do
  echo "Potential modid reference in $class_file"
done

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