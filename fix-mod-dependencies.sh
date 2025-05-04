#!/bin/bash

# This is a very targeted script to fix the specific dependency issue
# in the all-in-one JAR without trying to process all files

echo "Fixing module dependencies in all-in-one JAR..."

# Create temp directory for extraction
mkdir -p temp-fix

# Extract just the META-INF directory from the JAR
echo "Extracting META-INF from JAR..."
cd temp-fix
mkdir -p META-INF
jar xf ../fixed-jars/galacticexpansion_all-in-one-0.1.0.jar META-INF/
cd ..

# Create a completely fresh mods.toml file
echo "Creating new mods.toml file..."
cat > temp-fix/META-INF/mods.toml << EOF
# This is the fixed mods.toml file for the all-in-one JAR
# All module dependencies have been consolidated

modLoader="javafml"
loaderVersion="[47,)"
license="All Rights Reserved"
issueTrackerURL="https://github.com/AstroFrame/GalacticExpansion/issues"

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

# Dependencies are managed here - no module dependencies needed
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

# Update the JAR with the new mods.toml
echo "Updating JAR with new mods.toml..."
jar uf fixed-jars/galacticexpansion_all-in-one-0.1.0.jar -C temp-fix META-INF/mods.toml

# Verify the update
if [ $? -eq 0 ]; then
  echo "✅ Fixed mods.toml has been added to the JAR successfully!"
  
  # Update the fixed-jars zip
  echo "Updating fixed-jars package..."
  cd fixed-jars
  zip -u ../galactic-expansion-fixed-v0.1.0-neoforge-1.21.5.zip galacticexpansion_all-in-one-0.1.0.jar
  cd ..
  
  echo "✅ Fix complete! The all-in-one JAR should now work without module dependency issues."
else
  echo "❌ Error: Failed to update the JAR!"
  exit 1
fi

# Clean up temp files
echo "Cleaning up temporary files..."
rm -rf temp-fix

echo "Module dependency fix complete!"