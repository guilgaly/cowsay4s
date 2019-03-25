#!/usr/bin/env bash

set -e

echo "[INFO] Run tests (Core)"

scala_version="$1"
echo "Scala version: $scala_version"

additional_build="$2"
echo "Additional build: $additional_build"

mill "_._[$scala_version].test"

if [[ "$additional_build" == "cli" ]]; then
  echo "[INFO] Running tests (CLI)"
  mill cli.test
fi
