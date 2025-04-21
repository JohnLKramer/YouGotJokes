#!/bin/sh

docker compose up -d --wait test-db
./gradlew check
