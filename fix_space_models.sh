#!/bin/bash
for f in $(find space/src/main/resources/assets/galacticspace/models/item/ -type f -name "*.json"); do
  echo "Fixing $f"
  sed -i 's/"layer0": "galactic-space:/"layer0": "galacticspace:/g' "$f"
done
