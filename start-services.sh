#!/bin/bash
docker-compose -f people-app-ui/docker-compose.yml -f people-app/docker-compose.yml up --build
