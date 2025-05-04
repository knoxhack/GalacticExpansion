#!/bin/bash

# This is a simplified version of the all-in-one JAR packager
# that focuses on fixing the module dependency issues

echo "Creating fixed all-in-one JAR for Galactic Expansion mod..."

# Create output directories
mkdir -p fixed-jars
mkdir -p temp-fix

# Extract the existing all-in-one JAR
echo "Extracting existing JAR..."
cd temp-fix
jar xf ../fixed-jars/galacticexpansion_all-in-one-0.1.0.jar
cd ..

# Remove the problematic files
echo "Removing problematic files..."
find temp-fix -name "*ModProvider*.class" -delete
find temp-fix -name "mods.toml" -not -path "*/META-INF/*" -exec mv {} {}.disabled \;

# Fix the JSON files to use the unified ModID
echo "Fixing all resource references to use the unified ModID..."
for pat in galacticcore galacticpower galacticspace galacticmachinery galacticbiotech \
           galacticenergy galacticconstruction galacticrobotics galacticutilities \
           galacticvehicles galacticweaponry; do
    echo "  - Replacing $pat with galacticexpansion in JSON files"
    find temp-fix -name "*.json" -exec sed -i "s/$pat/galacticexpansion/g" {} \; 2>/dev/null
done

# Also handle the hyphenated versions
for pat in galactic-core galactic-power galactic-space galactic-machinery galactic-biotech \
           galactic-energy galactic-construction galactic-robotics galactic-utilities \
           galactic-vehicles galactic-weaponry; do
    echo "  - Replacing $pat with galacticexpansion in JSON files"
    find temp-fix -name "*.json" -exec sed -i "s/$pat/galacticexpansion/g" {} \; 2>/dev/null
done

# Create a fresh unified mods.toml file
echo "Creating unified mods.toml file..."
cat > temp-fix/META-INF/mods.toml << EOF
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

# Update the manifest
echo "Updating manifest..."
cat > temp-fix/META-INF/MANIFEST.MF << EOF
Manifest-Version: 1.0
Implementation-Title: Galactic Expansion (All-in-One)
Implementation-Version: 0.1.0
Specification-Vendor: AstroFrame
Implementation-Vendor: AstroFrame
Implementation-Timestamp: $(date -u +"%Y-%m-%dT%H:%M:%SZ")
FMLModType: MOD
EOF

# Repackage the JAR
echo "Creating fixed JAR file..."
(cd temp-fix && jar cf ../fixed-jars/galacticexpansion_all-in-one-0.1.0-fixed.jar .)

# Verify the JAR was created
if [ -f "fixed-jars/galacticexpansion_all-in-one-0.1.0-fixed.jar" ]; then
  jar_size=$(du -h "fixed-jars/galacticexpansion_all-in-one-0.1.0-fixed.jar" | cut -f1)
  echo "Fixed all-in-one JAR created successfully: fixed-jars/galacticexpansion_all-in-one-0.1.0-fixed.jar (${jar_size})"
  
  # Rename the original JAR for backup
  mv "fixed-jars/galacticexpansion_all-in-one-0.1.0.jar" "fixed-jars/galacticexpansion_all-in-one-0.1.0.jar.bak"
  
  # Move the fixed JAR to the expected name
  mv "fixed-jars/galacticexpansion_all-in-one-0.1.0-fixed.jar" "fixed-jars/galacticexpansion_all-in-one-0.1.0.jar"
  
  # Update the fixed-jars zip
  echo "Updating fixed-jars package..."
  cd fixed-jars
  zip -u ../galactic-expansion-fixed-v0.1.0-neoforge-1.21.5.zip galacticexpansion_all-in-one-0.1.0.jar
  cd ..
  
  echo "✅ Fix complete! The all-in-one JAR should now work without module dependency issues."
else
  echo "❌ Error: Failed to create fixed all-in-one JAR!"
  exit 1
fi

# Clean up temp files
echo "Cleaning up temporary files..."
rm -rf temp-fix

echo "All-in-one JAR fix complete!"