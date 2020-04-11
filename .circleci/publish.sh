#!/usr/bin/env bash

set -e

scala_version="$1"
echo "Scala version: $scala_version"
publish_web="$2"
echo "Publish web app: $publish_web"

if [[ "$CIRCLE_BRANCH" == "master" ]]; then

  echo "[INFO] Set up GPG signing key"
  gpg --version
  gpg --batch --passphrase "$GPG_KEY_ENC_PASSWORD" --output .circleci/key.asc --decrypt .circleci/key.asc.enc
  gpg --batch --passphrase "$GPG_PASSWORD" --import .circleci/key.asc
  rm .circleci/key.asc

  echo "[INFO] Publish artifacts"

  mill --version

  mill "mill.scalalib.PublishModule/publishAll" \
    --sonatypeCreds "guilgaly:$SONATYPE_PASSWORD" \
    --signed "true" \
    --publishArtifacts "core._[$scala_version].publishArtifacts"

  if [[ "$publish_web" == "true" ]]; then
    echo "[INFO] Deploying JAR to Heroku"
    mill "web.assembly"
    heroku deploy:jar "out/web/assembly/dest/out.jar" --app "cowsay-online" --jdk "11"
  fi

else

  echo "[INFO] Skip publish step"

fi
