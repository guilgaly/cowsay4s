#!/usr/bin/env bash

set -e

scala_version="$1"
echo "Scala version: $scala_version"
build_cli="$2"
echo "Build CLI: $build_cli"
build_web="$3"
echo "Build web app: $build_web"

echo "[INFO] Check the source format"
mill reformat
git diff --exit-code || (cat >> /dev/stderr <<EOF
[ERROR] Scalafmt & Organize Imports check failed, see differences above.
To fix, format your sources using 'mill reformat'.
EOF
false
)

echo "[INFO] Run tests (Core)"
mill "_._[$scala_version].test"

if [[ "$build_cli" == "true" ]]; then
  echo "[INFO] Running tests (CLI)"
  mill "cli.test"
fi

if [[ "$build_web" == "true" ]]; then
  echo "[INFO] Running tests (web app)"
  mill "web.test"
fi
