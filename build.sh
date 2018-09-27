#!/usr/bin/env bash

./sbt dist
rm -rf svc/*
unzip -d svc target/universal/people-app*.zip
mv svc/*/* svc/
rm svc/bin/*.bat
mv svc/bin/* svc/bin/start