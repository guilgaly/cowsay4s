#!/usr/bin/env bash

set -e

echo "[INFO] Running tests (Core)"

scala_flavor="$1"
echo "Scala flavor: $scala_flavor"

scala_version="$2"
echo "Scala version: $scala_version"

mill "core.$scala_flavor[$scala_version].test"
