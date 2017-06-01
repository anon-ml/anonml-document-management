#!/usr/bin/env bash
mongod&
java -Djava.security.egd=file:/dev/./urandom -jar /docmgmt-0.0.1-SNAPSHOT.jar