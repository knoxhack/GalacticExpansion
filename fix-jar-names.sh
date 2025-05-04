#!/bin/bash

# This script fixes the JAR file naming problem 
# The crash log indicates Minecraft is looking for files like "power_galacticpower-0.1.0.jar"
# But our package script creates files like "galacticpower-0.1.0.jar"

echo "Creating fixed JAR files for Minecraft NeoForge 1.21.5..."

# Create a temporary directory for the fixed JAR files
mkdir -p fixed-jars

# Remove any old JAR files in the fixed-jars directory
rm -f fixed-jars/*.jar

# Define all modules
MODULES=(
  "core"
  "biotech"
  "energy"
  "machinery"
  "power"
  "space"
  "robotics"
  "construction"
  "utilities"
  "vehicles"
  "weaponry"
)

# Fix each module JAR name
for module in "${MODULES[@]}"; do
  source_jar="./${module}/build/libs/galactic${module}-0.1.0.jar"
  target_jar="./fixed-jars/${module}_galactic${module}-0.1.0.jar"
  
  if [ -f "$source_jar" ]; then
    echo "  - Fixing: $source_jar -> $target_jar"
    cp "$source_jar" "$target_jar"
  else
    echo "  - Warning: $source_jar not found"
  fi
done

# Also include the main JAR file if it exists
main_jar="./build/libs/GalacticExpansion-0.1.0.jar"
if [ -f "$main_jar" ]; then
  echo "  - Keeping main JAR: $main_jar"
  cp "$main_jar" fixed-jars/
else
  echo "  - Main JAR not found: $main_jar"
fi

# Count how many JAR files we fixed
jar_count=$(ls -1 fixed-jars/*.jar 2>/dev/null | wc -l)

if [ "$jar_count" -eq 0 ]; then
  echo "Error: No JAR files found to fix!"
  exit 1
fi

echo "Fixed $jar_count JAR files."

# Create a zip file with all the fixed JARs
echo "Creating fixed zip package..."
cd fixed-jars
zip -r ../galactic-expansion-fixed-v0.1.0-neoforge-1.21.5.zip *.jar
cd ..

echo "Fixed package created: galactic-expansion-fixed-v0.1.0-neoforge-1.21.5.zip"
echo "Package fix complete!"
