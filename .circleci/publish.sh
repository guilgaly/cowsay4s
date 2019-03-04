#!/usr/bin/env bash

set -e

scala_flavor="$1"
echo "Scala flavor: $scala_flavor"

scala_version="$2"
echo "Scala version: $scala_version"

if [[ "$CIRCLE_BRANCH" == "master" ]]; then

  echo "[INFO] Set up GPG signing key"
  gpg --version
  gpg --batch --passphrase "$GPG_KEY_ENC_PASSWORD" --output .circleci/key.asc --decrypt .circleci/key.asc.enc
  gpg --batch --passphrase "$GPG_PASSWORD" --import .circleci/key.asc
  rm .circleci/key.asc

  echo "[INFO] Publish artifacts"

  mill mill.scalalib.PublishModule/publishAll \
    --sonatypeCreds "guilgaly:$SONATYPE_PASSWORD" \
    --signed "true" \
    --publishArtifacts "_.$scala_flavor[$scala_version].publishArtifacts"

else

  echo "[INFO] Skip publish step"

fi
