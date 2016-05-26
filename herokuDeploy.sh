#!/bin/bash
git pull
mvn -Pprod
mvn -Pprod package
