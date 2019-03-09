#!/usr/bin/env bash

set -e

echo "[INFO] Run tests (Core)"

scala_flavor="$1"
echo "Scala flavor: $scala_flavor"

scala_version="$2"
echo "Scala version: $scala_version"

additional_build="$3"
echo "Additional build: $additional_build"

mill "_.$scala_flavor[$scala_version].test"

if [[ "$additional_build" == "cli" ]]; then
  echo "[INFO] Running tests (CLI)"
  mill cli.test
fi
