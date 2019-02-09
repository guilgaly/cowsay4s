#!/usr/bin/env bash

set -e

echo "[INFO] Publish artifacts"

mill mill.scalalib.PublishModule/publishAll \
  guilgaly:$SONATYPE_PASSWORD \
  $GPG_PASSWORD \
  __.publishArtifacts
