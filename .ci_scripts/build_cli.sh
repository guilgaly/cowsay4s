#!/usr/bin/env bash

set -e

echo "[INFO] Check the source format"
mill __.reformat
git diff --exit-code || (cat >> /dev/stderr <<EOF
[ERROR] Scalafmt check failed, see differences above.
To fix, format your sources using 'mill __.reformat'.
EOF
false
)

echo "[INFO] Running tests (CLI)"
mill cli.test
