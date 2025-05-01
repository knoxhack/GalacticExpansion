#!/bin/bash

export JAVA_HOME=/nix/store/sziqmjk1i28cxcr5x29jbz3dzhiz1pii-openjdk-headless-21+35

# Compile just the core module first to check if ResourceLocation is fixed
echo "Compiling core module..."
./gradlew :core:compileJava -q

if [ $? -eq 0 ]; then
    echo "Core module compiles successfully!"
else
    echo "Core module failed to compile."
    exit 1
fi

# Compile biotech module
echo "Compiling biotech module..."
./gradlew :biotech:compileJava -q

if [ $? -eq 0 ]; then
    echo "Biotech module compiles successfully!"
else
    echo "Biotech module failed to compile."
    exit 1
fi

# Compile machinery module
echo "Compiling machinery module..."
./gradlew :machinery:compileJava -q

if [ $? -eq 0 ]; then
    echo "Machinery module compiles successfully!"
else
    echo "Machinery module failed to compile."
    exit 1
fi

# Compile power module
echo "Compiling power module..."
./gradlew :power:compileJava -q

if [ $? -eq 0 ]; then
    echo "Power module compiles successfully!"
else
    echo "Power module failed to compile."
    exit 1
fi

echo "All modules compile successfully!"