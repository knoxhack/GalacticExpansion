#!/bin/bash

# Package all Galactic Expansion modules into a single zip file
# This script finds all module JAR files and packages them together

echo "Packaging Galactic Expansion Mod v0.1.0 for NeoForge 1.21.5..."

# Create the packaged-jars directory if it doesn't exist
mkdir -p packaged-jars

# Remove any old JAR files in the packaged-jars directory
rm -f packaged-jars/*.jar

# Remove previous zip package if it exists
rm -f galactic-expansion-v0.1.0-neoforge-1.21.5.zip

# Copy all module JAR files to the packaged-jars directory
echo "Copying module JAR files..."

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

# Copy each module's JAR to the packaged-jars directory
for module in "${MODULES[@]}"; do
  module_jar="./${module}/build/libs/galactic${module}-0.1.0.jar"
  
  if [ -f "$module_jar" ]; then
    echo "  - Found $module_jar"
    cp "$module_jar" packaged-jars/
  else
    echo "  - Warning: $module_jar not found"
  fi
done

# Also include the main JAR file if it exists
main_jar="./build/libs/GalacticExpansion-0.1.0.jar"
if [ -f "$main_jar" ]; then
  echo "  - Found main JAR: $main_jar"
  cp "$main_jar" packaged-jars/
else
  echo "  - Main JAR not found: $main_jar"
fi

# Also include the all-in-one JAR if it exists
all_in_one_jar="./combined-jar/GalacticExpansion-all-in-one-0.1.0.jar"
if [ -f "$all_in_one_jar" ]; then
  echo "  - Found all-in-one JAR: $all_in_one_jar"
  cp "$all_in_one_jar" packaged-jars/
else
  echo "  - All-in-one JAR not found. Running package-all-in-one.sh..."
  ./package-all-in-one.sh
  
  if [ -f "$all_in_one_jar" ]; then
    echo "  - Now found all-in-one JAR, adding to package"
    cp "$all_in_one_jar" packaged-jars/
  else
    echo "  - Warning: Still could not find all-in-one JAR!"
  fi
fi

# Count how many JAR files we found
jar_count=$(ls -1 packaged-jars/*.jar 2>/dev/null | wc -l)

if [ "$jar_count" -eq 0 ]; then
  echo "Error: No JAR files found to package!"
  exit 1
fi

echo "Found $jar_count JAR files to package."

# Include the JARS_README.md file in the package
if [ -f "JARS_README.md" ]; then
  echo "  - Including JARS_README.md in the package"
  cp "JARS_README.md" packaged-jars/README.md
fi

# Create a zip file with all the JARs and README
echo "Creating zip package..."
cd packaged-jars
zip -r ../galactic-expansion-v0.1.0-neoforge-1.21.5.zip *.jar README.md
cd ..

echo "Package created: galactic-expansion-v0.1.0-neoforge-1.21.5.zip"
echo "Packaging complete!"
