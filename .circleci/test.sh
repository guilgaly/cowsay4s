#!/usr/bin/env bash

set -e

scala_version="$1"
echo "Scala version: $scala_version"
additional_build="$2"
echo "Additional build: $additional_build"

echo "[INFO] Check the source format"
mill __.reformat
git diff --exit-code || (cat >> /dev/stderr <<EOF
[ERROR] Scalafmt check failed, see differences above.
To fix, format your sources using 'mill __.reformat'.
EOF
false
)

echo "[INFO] Run tests (Core)"
mill "_._[$scala_version].test"

if [[ "$additional_build" == "cli" ]]; then
  echo "[INFO] Running tests (CLI)"
  mill cli.test
fi
