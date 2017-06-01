#!/usr/bin/env bash
mvn clean install &&
docker build . -t anonml-docmgmt &&
docker run -p 9001:9001 anonml-docmgmt