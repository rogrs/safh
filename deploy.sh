#!/bin/bash
git pull
mvn package -DskipTests=true -B -Pprod
