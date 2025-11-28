#!/bin/sh

docker run --name keycloak-postgres -e POSTGRES_DB=keycloak -e POSTGRES_USER=keycloak -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres