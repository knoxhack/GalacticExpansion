#!/bin/bash

# Create a directory for the packaged JARs
rm -rf ./packaged-jars
mkdir -p ./packaged-jars

# Copy all the module JARs with proper naming
cp ./core/build/libs/galacticcore-0.1.0.jar ./packaged-jars/core_galacticcore-0.1.0.jar
cp ./biotech/build/libs/galacticbiotech-0.1.0.jar ./packaged-jars/biotech_galacticbiotech-0.1.0.jar
cp ./energy/build/libs/galacticenergy-0.1.0.jar ./packaged-jars/energy_galacticenergy-0.1.0.jar
cp ./machinery/build/libs/galacticmachinery-0.1.0.jar ./packaged-jars/machinery_galacticmachinery-0.1.0.jar
cp ./power/build/libs/galacticpower-0.1.0.jar ./packaged-jars/power_galacticpower-0.1.0.jar
cp ./space/build/libs/galacticspace-0.1.0.jar ./packaged-jars/space_galacticspace-0.1.0.jar
cp ./robotics/build/libs/galacticrobotics-0.1.0.jar ./packaged-jars/robotics_galacticrobotics-0.1.0.jar
cp ./construction/build/libs/galacticconstruction-0.1.0.jar ./packaged-jars/construction_galacticconstruction-0.1.0.jar
cp ./utilities/build/libs/galacticutilities-0.1.0.jar ./packaged-jars/utilities_galacticutilities-0.1.0.jar
cp ./vehicles/build/libs/galacticvehicles-0.1.0.jar ./packaged-jars/vehicles_galacticvehicles-0.1.0.jar
cp ./weaponry/build/libs/galacticweaponry-0.1.0.jar ./packaged-jars/weaponry_galacticweaponry-0.1.0.jar

# Create a ZIP file containing all the JARs
cd ./packaged-jars
zip -r ../galactic-expansion-v0.1.0-neoforge-1.21.5.zip *.jar
cd ..

echo "JAR files packaged successfully in galactic-expansion-v0.1.0-neoforge-1.21.5.zip"