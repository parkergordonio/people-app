#!/bin/bash
docker-compose -f people-app-ui/docker-compose.dev.yml -f people-app/docker-compose.yml up --build
