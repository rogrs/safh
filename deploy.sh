#!/bin/bash
git pull
mvn package -Pprod -DskipTests
