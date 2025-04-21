#!/bin/sh

docker compose up -d --wait db
./gradlew :app:bootRun
