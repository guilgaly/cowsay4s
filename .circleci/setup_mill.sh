#!/usr/bin/env bash

set -e

echo "[INFO] Set up mill"

mill_version="$1"
echo "Mill version: $mill_version"

if [[ -e /tmp/mill/mill ]]; then
  echo "mill binary already present"
else
  echo "downloading mill binary"
  mkdir /tmp/mill
  curl -L -o /tmp/mill/mill "https://github.com/lihaoyi/mill/releases/download/$mill_version/$mill_version"
fi

chmod +x /tmp/mill/mill
echo 'export PATH="/tmp/mill:$PATH"' >> $BASH_ENV
